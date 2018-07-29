package com.edricchan.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		//Get Firebase auth instance
		auth = FirebaseAuth.getInstance();

		btnSignIn = (MaterialButton) findViewById(R.id.signInBtn);
		btnSignUp = (MaterialButton) findViewById(R.id.signUpBtn);
		inputEmail = (TextInputLayout) findViewById(R.id.emailRegister);
		inputPassword = (TextInputLayout) findViewById(R.id.passwordRegister);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		btnSignIn.setOnClickListener(v -> {
			startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
			finish();
		});

		btnSignUp.setOnClickListener(v -> {

			String email = SharedHelper.getEditTextString(inputEmail).trim();
			String password = SharedHelper.getEditTextString(inputPassword).trim();

			if (TextUtils.isEmpty(email)) {
				Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (TextUtils.isEmpty(password)) {
				Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (password.length() < 6) {
				Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
				return;
			}

			progressBar.setVisibility(View.VISIBLE);
			//create user
			auth.createUserWithEmailAndPassword(email, password)
					.addOnCompleteListener(RegisterActivity.this, task -> {
						Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
						progressBar.setVisibility(View.GONE);
						// If sign in fails, display a message to the user. If sign in succeeds
						// the auth state listener will be notified and logic to handle the
						// signed in user can be handled in the listener.
						if (!task.isSuccessful()) {
							Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(),
									Toast.LENGTH_SHORT).show();
						} else {
							startActivity(new Intent(RegisterActivity.this, MainActivity.class));
							finish();
						}
					});

		});
		checkNetwork();
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