package com.edricchan.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

	private TextInputLayout inputEmail, inputPassword;
	private MaterialButton btnSignIn, btnSignUp;
	private ProgressBar progressBar;
	private FirebaseAuth auth;
	private static final String TAG = SharedHelper.getTag(RegisterActivity.class);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//Get Firebase auth instance
		auth = FirebaseAuth.getInstance();

		btnSignIn = findViewById(R.id.signInBtn);
		btnSignUp = findViewById(R.id.signUpBtn);
		inputEmail = findViewById(R.id.emailRegister);
		inputPassword = findViewById(R.id.passwordRegister);
		progressBar = findViewById(R.id.progressBar);

		btnSignIn.setOnClickListener(v -> {
			startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
			finish();
		});

		btnSignUp.setOnClickListener(v -> {

			String email = SharedHelper.getEditTextString(inputEmail).trim();
			String password = SharedHelper.getEditTextString(inputPassword).trim();

			if (TextUtils.isEmpty(email)) {
				Snackbar.make(findViewById(R.id.registerActivity), "Please enter an email address", Snackbar.LENGTH_LONG)
						.show();
				return;
			}

			if (TextUtils.isEmpty(password)) {
				Snackbar.make(findViewById(R.id.registerActivity), "Please enter a password!", Snackbar.LENGTH_LONG)
						.show();
				return;
			}

			if (password.length() < 6) {
				Snackbar.make(findViewById(R.id.registerActivity), "A minimum of 6 characters is required for a password", Snackbar.LENGTH_LONG)
						.show();
				return;
			}

			progressBar.setVisibility(View.VISIBLE);
			//create user
			auth.createUserWithEmailAndPassword(email, password)
					.addOnCompleteListener(RegisterActivity.this, task -> {
						progressBar.setVisibility(View.GONE);
						if (task.isSuccessful()) {
							startActivity(new Intent(RegisterActivity.this, MainActivity.class));
							finish();
						} else {
							Snackbar.make(findViewById(R.id.registerActivity), "An error occurred while authenticating. Try again later.", Snackbar.LENGTH_LONG)
									.show();
						}
					});

		});
		checkNetwork();
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

	private void checkNetwork() {
		if (SharedHelper.isNetworkAvailable(this)) {
			btnSignUp.setEnabled(true);
			btnSignIn.setEnabled(true);
			inputEmail.setEnabled(true);
			inputPassword.setEnabled(true);
		} else {
			btnSignUp.setEnabled(false);
			btnSignIn.setEnabled(false);
			inputEmail.setEnabled(false);
			inputPassword.setEnabled(false);
			Snackbar.make(findViewById(R.id.registerActivity), "No internet connection available. Some actions are disabled", Snackbar.LENGTH_INDEFINITE)
					.setBehavior(new NoSwipeBehavior())
					.setAction("Retry", view -> checkNetwork()).show();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		progressBar.setVisibility(View.GONE);
	}
}