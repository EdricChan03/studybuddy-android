package com.edricchan.studybuddy;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPasswordActivity extends AppCompatActivity {

	private EditText inputEmail;
	private Button btnReset, btnBack;
	private FirebaseAuth auth;
	private ProgressBar progressBar;
	private static final String TAG = SharedHelper.Companion.getTag(ResetPasswordActivity.class);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_password);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		inputEmail = findViewById(R.id.email);
		btnReset = findViewById(R.id.btn_reset_password);
		btnBack = findViewById(R.id.btn_back);
		progressBar = findViewById(R.id.progressBar);

		auth = FirebaseAuth.getInstance();

		btnBack.setOnClickListener(v -> finish());

		btnReset.setOnClickListener(v -> {

			String email = inputEmail.getText().toString().trim();

			if (TextUtils.isEmpty(email)) {
				Snackbar.make(findViewById(R.id.mainView), "Please enter an email address", Snackbar.LENGTH_LONG).show();
				return;
			}

			progressBar.setVisibility(View.VISIBLE);
			auth.sendPasswordResetEmail(email)
					.addOnCompleteListener(task -> {
						if (task.isSuccessful()) {
							Snackbar.make(findViewById(R.id.mainView), "An email has been sent to the email address that you have specified", Snackbar.LENGTH_LONG)
									.show();
						} else {
							Snackbar.make(findViewById(R.id.mainView), "An error occurred while attempting to send an email. Please try again later", Snackbar.LENGTH_LONG)
									.show();
							Log.e(TAG, "An error occurred while attempting to send a reset password email.", task.getException());
						}

						progressBar.setVisibility(View.GONE);
					});
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}