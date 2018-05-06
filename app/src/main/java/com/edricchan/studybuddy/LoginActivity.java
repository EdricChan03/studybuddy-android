package com.edricchan.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

	private static final String TAG = SharedHelper.getTag(LoginActivity.class);
	private static int RC_SIGN_IN;
	private EditText inputEmail, inputPassword;
	private FirebaseAuth auth;
	private ProgressBar progressBar;
	private Button btnSignup, btnLogin, btnReset;
	private SignInButton signInButton;
	private GoogleSignInClient googleSignInClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RC_SIGN_IN = 9001;
		//Get Firebase auth instance
		auth = FirebaseAuth.getInstance();

		if (auth.getCurrentUser() != null) {
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
			finish();
		}

		// set the view now
		setContentView(R.layout.activity_login);

		inputEmail = (EditText) findViewById(R.id.email);
		inputPassword = (EditText) findViewById(R.id.password);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		btnSignup = (Button) findViewById(R.id.btn_signup);
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnReset = (Button) findViewById(R.id.btn_reset_password);
		signInButton = (SignInButton) findViewById(R.id.google_sign_in_btn);
		signInButton.setColorScheme(SignInButton.COLOR_DARK);
		signInButton.setSize(SignInButton.SIZE_STANDARD);
		signInButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				signInWithGoogle();
			}
		});
		// Google sign in options
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.web_client_id))
				.requestEmail()
				.build();
		googleSignInClient = GoogleSignIn.getClient(this, gso);
		//Get Firebase auth instance
		auth = FirebaseAuth.getInstance();

		btnSignup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			}
		});

		btnReset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
			}
		});

		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String email = inputEmail.getText().toString();
				final String password = inputPassword.getText().toString();

				if (TextUtils.isEmpty(email)) {
					Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
					return;
				}

				if (TextUtils.isEmpty(password)) {
					Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
					return;
				}

				progressBar.setVisibility(View.VISIBLE);

				//authenticate user
				auth.signInWithEmailAndPassword(email, password)
						.addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
							@Override
							public void onComplete(@NonNull Task<AuthResult> task) {
								// If sign in fails, display a message to the user. If sign in succeeds
								// the auth state listener will be notified and logic to handle the
								// signed in user can be handled in the listener.
								progressBar.setVisibility(View.GONE);
								if (!task.isSuccessful()) {
									// there was an error
									if (password.length() < 6) {
										inputPassword.setError(getString(R.string.minimum_password));
									} else {
										Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
									}
								} else {
									showLoginSnackbar();
									Intent intent = new Intent(LoginActivity.this, MainActivity.class);
									startActivity(intent);
									finish();
								}
							}
						});
			}
		});
	}

	private void showLoginSnackbar() {
		if (auth.getCurrentUser() != null) {
			// TODO(Edric): Figure out a way to show this snackbar before the main activity shows
//			Snackbar.make(findViewById(android.R.id.content), String.format(getString(R.string.snackbar_user_login), auth.getCurrentUser().getEmail()), Snackbar.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), String.format(getString(R.string.snackbar_user_login), auth.getCurrentUser().getEmail()), Toast.LENGTH_SHORT).show();
		}
	}

	private void signInWithGoogle() {
		Intent signInIntent = googleSignInClient.getSignInIntent();
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RC_SIGN_IN) {
			Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
			try {
				GoogleSignInAccount account = task.getResult(ApiException.class);
				firebaseAuthWithGoogle(account);
			} catch (ApiException e) {
				Log.w("FAIL", "Google sign in failed", e);
			}
		}
	}

	private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
		Log.d(TAG, "firebaseAuthWithGoogle: called");

		AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
		auth.signInWithCredential(credential)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							// Sign in success, update UI with the signed-in user's information
							Log.d(TAG, "Successfully signed in!");
							showLoginSnackbar();
							Intent intent = new Intent(LoginActivity.this, MainActivity.class);
							startActivity(intent);
							finish();
						} else {
							// If sign in fails, display a message to the user.
							Log.e(TAG, "An error occured", task.getException());
							Toast.makeText(getApplicationContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
						}

						// ...
					}
				})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						// If sign in fails, display a message to the user.
						Log.e(TAG, "An error occured", e);
						Toast.makeText(getApplicationContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
					}
				});
	}

}