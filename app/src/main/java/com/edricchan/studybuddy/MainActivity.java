package com.edricchan.studybuddy;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.TooltipCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.lang.System.out;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
	private static final String ACTION_ADD_NEW_TODO = "com.edricchan.studybuddy.shortcuts.ADD_NEW_TODO";
	final Context context = this;
	private View view;
	private RecyclerView mRecyclerView;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;
	private ArrayList taskItems = new ArrayList<>();
	private String[] taskTest = new String[]{"wow", "test"};
	private Date dueDateTest;
	private int testInt, RC_SIGN_IN;
	private FirebaseAuth mAuth;
	private GoogleApiClient mGoogleApiClient;
	private String userName;
	private FirebaseFirestore db = FirebaseFirestore.getInstance();
	private FirebaseUser currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		view = (View) findViewById(R.id.todo_content);
		// Checks if the add new shortcut was tapped
		if (ACTION_ADD_NEW_TODO.equals(getIntent().getAction())) {
			newTask();
		}
		//  Declare a new thread to do a preference check
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				//  Initialize SharedPreferences
				SharedPreferences getPrefs = PreferenceManager
						.getDefaultSharedPreferences(getBaseContext());

				//  Create a new boolean and preference and set it to true
				boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

				//  If the activity has never started before...
				if (isFirstStart) {

					//  Launch app intro
					final Intent i = new Intent(MainActivity.this, MyIntroActivity.class);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							startActivity(i);
						}
					});

					//  Make a new preferences editor
					SharedPreferences.Editor e = getPrefs.edit();

					//  Edit preference to make it false because we don't want this to run again
					e.putBoolean("firstStart", false);

					//  Apply changes
					e.apply();
				}
			}
		});

		// Start the thread
		t.start();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 1988);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		dueDateTest = cal.getTime();
		mRecyclerView = (RecyclerView) findViewById(R.id.recycler_list);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		mRecyclerView.setHasFixedSize(true);

		// use a linear layout manager
		mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);

		// specify an adapter (see also next example)
		mAdapter = new StudyAdapter(context, taskItems);
		mRecyclerView.setAdapter(mAdapter);

		// FAB
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				newTask();
			}
		});

		// Handles swiping down to refresh logic
		final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.recycler_swiperefresh);
		// Sets a refreshing listener
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				mAdapter.notifyDataSetChanged();
				new android.os.Handler().postDelayed(
						new Runnable() {
							@Override
							public void run() {
								swipeRefreshLayout.setRefreshing(false);

							}
						},
						1000);
			}
		});
		// Check if Android Oreo
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			setupNotificationChannels();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		mAuth = FirebaseAuth.getInstance();
		currentUser = mAuth.getCurrentUser();
		if (currentUser == null) {
			out.println("Not logged in");
			AlertDialog signInDialog = new AlertDialog.Builder(context)
					.setTitle("Sign in")
					.setMessage("To access the content, please sign in or register an account. Click the \"Sign in\" button to continue. Otherwise, click Cancel.")
					.setPositiveButton("Sign in", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
							startActivity(loginIntent);
							dialogInterface.dismiss();
						}
					})
					.setNeutralButton("Sign up", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
							startActivity(registerIntent);
							dialogInterface.dismiss();
						}
					})
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							dialogInterface.cancel();
						}
					})
					.create();
			signInDialog.show();

		} else {
			getTodos();
		}
	}

	/**
	 * Gets data from Firestore
	 */
	public void getTodos() {
		db.collection("users/" + currentUser.getUid() + "/todos")
				.get()
				.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
					@Override
					public void onSuccess(QuerySnapshot documentSnapshots) {
						if (documentSnapshots.isEmpty()) {
							System.out.println("getTodos: onSuccess: List is empty");
						} else {
							// Convert the whole Query Snapshot to a list
							// of objects directly! No need to fetch each
							// document.
							List<TaskItem> types = documentSnapshots.toObjects(TaskItem.class);

							// Add all to your list
							taskItems.addAll(types);
							Log.d("Tag", "onSuccess: " + taskItems);
						}
					}
				})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Toast.makeText(getApplicationContext(), "Error getting data!", Toast.LENGTH_LONG).show();
						Log.w("Error", e);
					}
				});
	}

	/**
	 * Used for setting up notification channels
	 * NOTE: This will only work if the device is Android Oreo or later
	 */
	public void setupNotificationChannels() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationManager notificationManager =
					(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			// Create a new list
			List<NotificationChannel> channels = new ArrayList<NotificationChannel>();

			// Todos due soon notifications
			NotificationChannel todosDueSoonChannel = new NotificationChannel("todos_due_soon", "Soon to be due todos", NotificationManager.IMPORTANCE_HIGH);
			todosDueSoonChannel.setDescription("Shows todos that are due soon");
			todosDueSoonChannel.enableLights(true);
			todosDueSoonChannel.setLightColor(Color.YELLOW);
			todosDueSoonChannel.enableVibration(true);
			todosDueSoonChannel.setShowBadge(true);
			channels.add(todosDueSoonChannel);

			// Weekly summary notifications
			NotificationChannel weeklySummaryChannel = new NotificationChannel("weekly_summary", "Weekly summary", NotificationManager.IMPORTANCE_LOW);
			weeklySummaryChannel.setDescription("Shows weekly summary notifications on your todos");
			weeklySummaryChannel.setShowBadge(true);
			channels.add(weeklySummaryChannel);

			// Syncing notifications
			NotificationChannel syncChannel = new NotificationChannel("sync", "Sync", NotificationManager.IMPORTANCE_LOW);
			syncChannel.setDescription("Shows the sync progress of settings, todos and other miscellaneous things");
			syncChannel.setShowBadge(false);
			channels.add(syncChannel);

			// Uncategorized notifications
			NotificationChannel uncategorizedChannel = new NotificationChannel("uncategorized", "Uncategorized", NotificationManager.IMPORTANCE_DEFAULT);
			uncategorizedChannel.setDescription("All other notifications which are not sorted");
			uncategorizedChannel.setShowBadge(true);
			channels.add(uncategorizedChannel);
			// Pass list to method
			notificationManager.createNotificationChannels(channels);
		}
	}

	/**
	 * Shares the app
	 */
	public void share() {
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_TEXT, R.string.share_content);
		shareIntent.setType("text/plain");
		startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share_intent_value)));
	}

	public void newTask() {
		testInt++;
		final AlertDialog.Builder newTaskDialogBuilder = new AlertDialog.Builder(context);
		LayoutInflater inflater = this.getLayoutInflater();
		View newTaskDialogView = inflater.inflate(R.layout.dialog_new, null);
		newTaskDialogBuilder.setView(newTaskDialogView);
		newTaskDialogBuilder.setTitle("New Task");
		final TextInputEditText dialogTitle = (TextInputEditText) newTaskDialogView.findViewById(R.id.custom_dialog_title);
		final TextInputEditText dialogProject = (TextInputEditText) newTaskDialogView.findViewById(R.id.custom_dialog_project);
		final ImageButton dialogDate = (ImageButton) newTaskDialogView.findViewById(R.id.custom_dialog_datepicker_btn);
		final TextView dialogDateText = (TextView) newTaskDialogView.findViewById(R.id.custom_dialog_datepicker_result);
		final TextInputEditText dialogContent = (TextInputEditText) newTaskDialogView.findViewById(R.id.custom_dialog_content);
		TooltipCompat.setTooltipText(dialogDate, "Open datetime dialog");
		dialogDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final Calendar c = Calendar.getInstance();

				DatePickerDialog dpd = new DatePickerDialog(context,
						new DatePickerDialog.OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
												  int monthOfYear, int dayOfMonth) {
								dialogDateText.setText(dayOfMonth + "/" + monthOfYear + "/" + year);

							}
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
				dpd.show();
			}
		});
		newTaskDialogBuilder.setIcon(R.drawable.ic_pencil_white_24dp);
		newTaskDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.dismiss();
				Snackbar.make(view, "New task dialog was cancelled.", Snackbar.LENGTH_LONG)
						.setAction("Undo", new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								newTask();
							}
						})
						.setDuration(6000).show();
			}
		});
		newTaskDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				final String title = dialogTitle.getText().toString();
				final String project = dialogProject.getText().toString();
				final String content = dialogContent.getText().toString();
				// TODO(Edric): Remove this unnecessary and add more coding logic
				out.println("Task name is " + title);
				Date taskDate = null;
				try {
					taskDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(dialogDateText.getText().toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				final TaskItem tempItem = new TaskItem(title, taskDate, project, content);
				taskItems.add(tempItem);
				dialogInterface.dismiss();
				Snackbar.make(view, title + " was saved to todos.", Snackbar.LENGTH_LONG)
						.setAction("Undo", new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								// TODO(Edric): Delete the task stuff and reshow the dialog
								taskItems.remove(tempItem);
								mAdapter.notifyItemRemoved(testInt);
								newTask();
							}
						})
						.setDuration(8000).show();
				db.collection("users/" + currentUser.getUid() + "/todos")
						.add(tempItem)
						.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
							@Override
							public void onSuccess(DocumentReference documentReference) {
								Log.d("Tag", "DocumentSnapshot added with ID: " + documentReference.getId());
							}
						})
						.addOnFailureListener(new OnFailureListener() {
							@Override
							public void onFailure(@NonNull Exception e) {
								Log.w("Tag", "Error adding document: ", e);
							}
						});
			}
		});
		AlertDialog newTaskDialog = newTaskDialogBuilder.create();
		newTaskDialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
			case R.id.action_settings:
				Intent prefsIntent = new Intent(this, SettingsActivity.class);
				startActivity(prefsIntent);
				break;
			case R.id.action_about:
				String aboutDialogText = getString(R.string.about_dialog_text);
				AlertDialog.Builder aboutDialogBuilder = new AlertDialog.Builder(context);
				aboutDialogBuilder.setTitle("About this app");
				aboutDialogBuilder.setMessage(aboutDialogText);
				aboutDialogBuilder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
					}
				});
				aboutDialogBuilder.setNeutralButton("Visit Source Code", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						String githubUrl = "https://github.com/Chan4077/StudyBuddy";
						Intent githubIntent = new Intent();
						githubIntent.setAction(Intent.ACTION_VIEW);
						githubIntent.setData(Uri.parse(githubUrl));
						startActivity(githubIntent);
					}
				});
				AlertDialog aboutDialog = aboutDialogBuilder.create();
				aboutDialog.show();
				break;
			case R.id.action_share:
				share();
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		Log.d("Tag", "onConnectionFailed:" + connectionResult);
	}
}
