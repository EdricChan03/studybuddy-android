package com.edricchan.studybuddy;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.support.v7.widget.TooltipCompat;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.System.out;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    final Context context = this;
    private View view;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<TaskItem> taskItems = new ArrayList<>();
    private String[] taskTest = new String[]{"wow", "test"};
    private Date dueDateTest;
    private int testInt, RC_SIGN_IN;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (View) findViewById(R.id.content);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1988);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        dueDateTest = cal.getTime();

        taskItems.add(new TaskItem(0, "test", taskTest, dueDateTest));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_list);

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

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.recycler_swiperefresh);
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
        // Firebase
        RC_SIGN_IN = 9001;
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("713563449638-sc1up855asm687s55f8qi4bdrh0u18tc.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // Tooltips
        TooltipCompat.setTooltipText(fab, "New Task");
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            out.println("Not logged in");
            final AlertDialog.Builder signInDialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater = this.getLayoutInflater();
            View signInDialogView = inflater.inflate(R.layout.dialog_sign_in, null);
            signInDialogBuilder.setView(signInDialogView);
            signInDialogBuilder.setTitle("Sign in");
            final SignInButton signInButton = (SignInButton) signInDialogView.findViewById(R.id.signInDialogGoogle);
            final AlertDialog signInDialog = signInDialogBuilder.create();
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                    out.println("TEST");
                    signInDialog.dismiss();
                }
            });
            signInDialog.show();
        } else {
            // Logged in
            // TODO: Add database
            Log.d("Tag", "Successfully logged in!");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseGoogle(account);
            } else {
                // Somehow it failed
                Log.d("tag", "failed");
            }
        }
    }

    private void firebaseGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Tag", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                            builder.setMessage(user.toString())
//                                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            dialogInterface.dismiss();
//                                        }
//                                    });
//                            AlertDialog dialog = builder.create();
//                            dialog.show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Tag", "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void share() {
        final AlertDialog.Builder shareDialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = this.getLayoutInflater();
        View shareDialogView = inflater.inflate(R.layout.dialog_share, null);
        shareDialogBuilder.setView(shareDialogView);
        shareDialogBuilder.setTitle("Share");
        shareDialogBuilder.setIcon(R.drawable.ic_share_white_24dp);
        final TextInputEditText desc = (TextInputEditText) shareDialogView.findViewById(R.id.shareDialogDesc);
        shareDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Snackbar.make(view, "Share dialog was cancelled.", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                share();
                            }
                        })
                        .setDuration(6000).show();
            }
        });
        shareDialogBuilder.setPositiveButton("Share", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, desc.getText().toString());
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share_intent_value)));
            }

            ;
        });
        AlertDialog shareDialog = shareDialogBuilder.create();
        shareDialog.show();
    }

    public void newTask() {
        testInt++;
        final AlertDialog.Builder newTaskDialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = this.getLayoutInflater();
        View newTaskDialogView = inflater.inflate(R.layout.dialog_new, null);
        newTaskDialogBuilder.setView(newTaskDialogView);
        newTaskDialogBuilder.setTitle("New Task");
        final TextInputEditText name = (TextInputEditText) newTaskDialogView.findViewById(R.id.customDialogName);
        final TextInputEditText labels = (TextInputEditText) newTaskDialogView.findViewById(R.id.customDialogLabels);
        final TextInputEditText datepicker = (TextInputEditText) newTaskDialogView.findViewById(R.id.customDialogDate);
        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                DatePickerDialog dpd = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                datepicker.setText(dayOfMonth + "/" + monthOfYear + "/" + year);

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
                final String task = name.getText().toString();
                String taskLabels = labels.getText().toString();
                // TODO(Edric): Remove this unnecessary and add more coding logic
                out.println("Task name is " + task);
                out.println("Task labels are " + taskLabels);
                Date taskDate = null;
                try {
                    taskDate = new SimpleDateFormat("dd/MM/yyyy").parse(datepicker.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                final TaskItem tempItem = new TaskItem(testInt, task, taskTest, taskDate);
                taskItems.add(tempItem);
                dialogInterface.dismiss();
                Snackbar.make(view, task + " was saved to calendar.", Snackbar.LENGTH_LONG)
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
