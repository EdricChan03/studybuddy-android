package com.edricchan.studybuddy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;

/**
 * Created by edricchan on 8/3/18.
 */

public class NewTaskActivity extends AppCompatActivity {
	static final String ACTION_NEW_TASK_FROM_SHORTCUT = "com.edricchan.studybuddy.ACTION_NEW_TASK_FROM_SHORTCUT";
	private TextInputLayout taskTitle;
	private TextInputLayout taskProject;
	private TextInputLayout taskContent;
	private Date taskDate;
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
		taskTitle = (TextInputLayout) findViewById(R.id.task_title_wrapper);
		taskProject = (TextInputLayout) findViewById(R.id.task_project_wrapper);
		taskContent = (TextInputLayout) findViewById(R.id.task_content_wrapper);
		ImageButton dialogDate = (ImageButton) findViewById(R.id.task_datepicker_button);
		final TextView dialogDateText = (TextView) findViewById(R.id.task_datepicker_textview);
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
				taskDate = SharedHelper.getDateFromDatePicker(dpd.getDatePicker());
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
			if (SharedHelper.getEditText(taskTitle).length() != 0) {
				taskTitle.setErrorEnabled(false);
				SharedHelper.addTodo(
						SharedHelper.getEditTextString(taskTitle),
						SharedHelper.getEditTextString(taskContent),
						SharedHelper.getEditTextString(taskProject).split(","),
						taskDate,
						currentUser,
						mFirestore
				)
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
				taskTitle.setError("Please enter something.");
				Snackbar.make(findViewById(R.id.new_task_view), "A task title is required.", Snackbar.LENGTH_LONG).show();
			}
		}

		return super.onOptionsItemSelected(item);
	}
}
