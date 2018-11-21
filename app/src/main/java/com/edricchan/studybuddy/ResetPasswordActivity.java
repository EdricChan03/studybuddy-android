package com.edricchan.studybuddy;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPasswordActivity extends AppCompatActivity {

	private EditText inputEmail;
	private Button btnReset, btnBack;
	private FirebaseAuth auth;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_password);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		inputEmail = (EditText) findViewById(R.id.email);
		btnReset = (Button) findViewById(R.id.btn_reset_password);
		btnBack = (Button) findViewById(R.id.btn_back);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		auth = FirebaseAuth.getInstance();

		btnBack.setOnClickListener(v -> finish());

		btnReset.setOnClickListener(v -> {

			String email = inputEmail.getText().toString().trim();

			if (TextUtils.isEmpty(email)) {
				Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
				return;
			}

			progressBar.setVisibility(View.VISIBLE);
			auth.sendPasswordResetEmail(email)
					.addOnCompleteListener(task -> {
						if (task.isSuccessful()) {
							Toast.makeText(ResetPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(ResetPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
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