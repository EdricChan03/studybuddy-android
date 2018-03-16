package com.edricchan.studybuddy;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.TooltipCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by edricchan on 8/3/18.
 */

public class NewTaskActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivty_new_task);
		TextInputEditText dialogTitle = (TextInputEditText) findViewById(R.id.custom_dialog_title);
		TextInputEditText dialogProject = (TextInputEditText) findViewById(R.id.custom_dialog_project);
		ImageButton dialogDate = (ImageButton) findViewById(R.id.custom_dialog_datepicker_btn);
		final TextView dialogDateText = (TextView) findViewById(R.id.custom_dialog_datepicker_result);
		TextInputEditText dialogContent = (TextInputEditText) findViewById(R.id.custom_dialog_content);
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
			// TODO: Add logic here
			finish();
		}

		return super.onOptionsItemSelected(item);
	}
}
