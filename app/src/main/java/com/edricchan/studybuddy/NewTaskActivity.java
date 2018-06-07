package com.edricchan.studybuddy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

/**
 * Created by edricchan on 8/3/18.
 */

public class NewTaskActivity extends AppCompatActivity {
	static final String ACTION_NEW_TASK_FROM_SHORTCUT = "com.edricchan.studybuddy.ACTION_NEW_TASK_FROM_SHORTCUT";
	private EditText taskTitle;
	private EditText taskProject;
	private EditText taskContent;
	private FirebaseAuth mAuth;
	private FirebaseFirestore mFirestore;
	private FirebaseUser currentUser;
	private boolean allowAccess;
	private String TAG = SharedHelper.getTag(NewTaskActivity.class);

	private boolean checkSignedIn() {
		if (mAuth.getCurrentUser() != null) {
			// User is signed in
			return true;
		} else {
			// User is not signed in!
			return false;
		}
	}

	private String getEditTextString(EditText editText) {
		return editText.getText().toString();
	}

	@Override
	public void onStart() {
		super.onStart();
		currentUser = mAuth.getCurrentUser();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivty_new_task);
		mAuth = FirebaseAuth.getInstance();
		mFirestore = FirebaseFirestore.getInstance();
		if (!checkSignedIn()) {
			Toast.makeText(this, "Please sign in before continuing", Toast.LENGTH_SHORT).show();
			Intent signInIntent = new Intent(NewTaskActivity.this, LoginActivity.class);
			startActivity(signInIntent);
			allowAccess = false;
		} else {
			allowAccess = true;
		}
		taskTitle = (EditText) findViewById(R.id.task_title_edittext);
		taskProject = (EditText) findViewById(R.id.task_project_edittext);
		ImageButton dialogDate = (ImageButton) findViewById(R.id.task_datepicker_button);
		final TextView dialogDateText = (TextView) findViewById(R.id.task_datepicker_textview);
		taskContent = (EditText) findViewById(R.id.task_content_edittext);
		TooltipCompat.setTooltipText(dialogDate, "Open datetime dialog");
		dialogDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final Calendar c = Calendar.getInstance();

				DatePickerDialog dpd = new DatePickerDialog(NewTaskActivity.this,
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_new_task, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_submit && allowAccess) {
			if (taskTitle.length() != 0) {
				// Check if activity was launched from app shortcut
				if (ACTION_NEW_TASK_FROM_SHORTCUT.equals(getIntent().getAction())) {
					SharedHelper.addTodo(getEditTextString(taskTitle), getEditTextString(taskContent), getEditTextString(taskProject), currentUser, mFirestore)
							.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
								@Override
								public void onSuccess(DocumentReference documentReference) {
									Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
									Toast.makeText(NewTaskActivity.this, "Successfully added data!", Toast.LENGTH_SHORT).show();
									finish();
								}
							})
							.addOnFailureListener(new OnFailureListener() {
								@Override
								public void onFailure(@NonNull Exception e) {
									Toast.makeText(NewTaskActivity.this, "An error occured while adding data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
									Log.e(TAG, "An error occured while adding data: ", e);
								}
							});
				} else {
					Intent data = new Intent();
					data.putExtra("taskTitle", taskTitle.getText().toString());
					data.putExtra("taskProject", taskProject.getText().toString());
					data.putExtra("taskContent", taskContent.getText().toString());
					setResult(RESULT_OK, data);
					finish();
				}
			} else {
				taskTitle.setError("Please enter something.");
				Snackbar.make(findViewById(R.id.new_task_view), "A task title is required.", Snackbar.LENGTH_LONG).show();
			}
		}

		return super.onOptionsItemSelected(item);
	}
}
