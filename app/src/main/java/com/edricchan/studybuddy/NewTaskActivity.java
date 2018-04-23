package com.edricchan.studybuddy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.TooltipCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by edricchan on 8/3/18.
 */

public class NewTaskActivity extends AppCompatActivity {
	private EditText taskTitle;
	private EditText taskProject;
	private EditText taskContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivty_new_task);
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
		if (item.getItemId() == R.id.action_submit) {
			Intent data = new Intent();
			if (taskTitle.length() != 0) {
				data.putExtra("taskTitle", taskTitle.getText().toString());
				data.putExtra("taskProject", taskProject.getText().toString());
				data.putExtra("taskContent", taskContent.getText().toString());
				setResult(RESULT_OK, data);
				finish();
			} else {
				taskTitle.setError("Please enter something.");
				Snackbar.make(findViewById(R.id.new_task_view), "A task title is required.", Snackbar.LENGTH_LONG).show();
			}
		}

		return super.onOptionsItemSelected(item);
	}
}
