package com.edricchan.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

	private static final String TAG = SharedHelper.getTag(LoginActivity.class);
	private static int RC_SIGN_IN;
	private TextInputLayout inputEmail, inputPassword;
	private FirebaseAuth auth;
	private ProgressBar progressBar;
	private Button btnSignup, btnLogin, btnReset;
	private SignInButton signInButton;
	private GoogleSignInClient googleSignInClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Result code used to check for authentication
		RC_SIGN_IN = 9001;
		auth = FirebaseAuth.getInstance();
		// Check if there's already an authenticated user
		if (auth.getCurrentUser() != null && SharedHelper.isNetworkAvailable(this)) {
			// This activity (`LoginActivity`) shouldn't be shown to an already authenticated user
			// Instead, redirect the user to the main activity and close this activity
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
			finish();
		}
		setContentView(R.layout.activity_login);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		inputEmail = findViewById(R.id.emailLogin);
		inputPassword = findViewById(R.id.passwordLogin);
		progressBar = findViewById(R.id.progressBar);
		btnSignup = findViewById(R.id.signUpBtn);
		btnLogin = findViewById(R.id.loginBtn);
		btnReset = findViewById(R.id.resetPasswordBtn);
		signInButton = findViewById(R.id.googleSignInBtn);
		signInButton.setColorScheme(SignInButton.COLOR_DARK);
		signInButton.setSize(SignInButton.SIZE_STANDARD);
		signInButton.setOnClickListener(view -> signInWithGoogle());
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.web_client_id))
				.requestEmail()
				.build();
		googleSignInClient = GoogleSignIn.getClient(this, gso);

		btnSignup.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

		btnReset.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class)));

		btnLogin.setOnClickListener(v -> {
			String email = SharedHelper.getEditTextString(inputEmail);
			final String password = SharedHelper.getEditTextString(inputPassword);
			// Clear any previous errors
			inputEmail.setError(null);
			inputPassword.setError(null);
			if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
				if (TextUtils.isEmpty(email)) {

					inputEmail.setError("Please enter an email address");
				}
				if (TextUtils.isEmpty(password)) {
					inputPassword.setError("Please enter password");
				}
				return;
			}

			progressBar.setVisibility(View.VISIBLE);
			// Authenticate the user
			auth.signInWithEmailAndPassword(email, password)
					.addOnCompleteListener(LoginActivity.this, task -> {
						// If sign in fails, display a message to the user. If sign in succeeds
						// the auth state listener will be notified and logic to handle the
						// signed in user can be handled in the listener.
						progressBar.setVisibility(View.GONE);
						if (!task.isSuccessful()) {
							Log.e(TAG, "An error occurred while attempting to login using an email and password.", task.getException());
							// there was an error
							if (password.length() < 6) {
								inputPassword.setError(getString(R.string.minimum_password));
							} else {
								Snackbar.make(findViewById(R.id.loginActivity), "An error occurred while attempting to login. Please try again later", Snackbar.LENGTH_LONG)
										.show();
							}
						} else {
							showLoginSnackBar();
							Intent intent = new Intent(LoginActivity.this, MainActivity.class);
							startActivity(intent);
							finish();
						}
					});
		});
		checkNetwork();
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
				Log.w(TAG, "Google sign in failed", e);
			}
		}
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

	private void showLoginSnackBar() {
		if (auth.getCurrentUser() != null) {
			// TODO(Edric): Figure out a way to show this snackbar before the main activity shows
//			Snackbar.make(findViewById(android.R.id.content), String.format(getString(R.string.snackbar_user_login), auth.getCurrentUser().getEmail()), Snackbar.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), getString(R.string.snackbar_user_login, auth.getCurrentUser().getEmail()), Toast.LENGTH_SHORT).show();
		}
	}

	private void signInWithGoogle() {
		Intent signInIntent = googleSignInClient.getSignInIntent();
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}

	/**
	 * A method used to disable all functionality if no internet connection is available
	 * <p>
	 * TODO: Bring this to SharedHelper instead
	 */
	private void checkNetwork() {
		Log.d(TAG, "isNetworkAvailable: " + SharedHelper.isNetworkAvailable(this));
		if (SharedHelper.isNetworkAvailable(this)) {
			setViewsEnabled(true);
		} else {
			setViewsEnabled(false);
			Snackbar.make(findViewById(R.id.loginActivity), R.string.snackbar_internet_unavailable_login, Snackbar.LENGTH_INDEFINITE)
					.setBehavior(new NoSwipeBehavior())
					.setAction(R.string.snackbar_internet_unavailable_action, view -> checkNetwork()).show();
		}
	}

	/**
	 * Sets all views as shown/hidden
	 *
	 * @param enabled Whether to show the views
	 */
	private void setViewsEnabled(boolean enabled) {
		btnSignup.setEnabled(enabled);
		btnLogin.setEnabled(enabled);
		btnReset.setEnabled(enabled);
		signInButton.setEnabled(enabled);
		inputEmail.setEnabled(enabled);
		inputPassword.setEnabled(enabled);
	}

	/**
	 * Authorizes with Google
	 *
	 * @param acct The account
	 */
	private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
		Log.d(TAG, "firebaseAuthWithGoogle: called");

		AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
		auth.signInWithCredential(credential)
				.addOnCompleteListener(this, task -> {
					if (task.isSuccessful()) {
						// Sign in success, update UI with the signed-in user's information
						Log.d(TAG, "Successfully signed in!");
						showLoginSnackBar();
						Intent intent = new Intent(LoginActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					} else {
						// If sign in fails, display a message to the user.
						Log.e(TAG, "An error occurred", task.getException());
						Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
					}

					// ...
				})
				.addOnFailureListener(e -> {
					// If sign in fails, display a message to the user.
					Log.e(TAG, "An error occured", e);
					Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
				});
	}

}