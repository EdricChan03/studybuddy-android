package com.edricchan.studybuddy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

public class AccountActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
	private FirebaseAuth mAuth;
	private FirebaseUser mUser;
	private String TAG = SharedHelper.getTag(AccountActivity.class);
	private Button mAccountActions;
	private Button mActionSignInButton;
	private ImageView mAvatarImageView;
	private TextView mDisplayNameTextView;
	private TextView mEmailTextView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mAvatarImageView = findViewById(R.id.accountAvatar);
		mDisplayNameTextView = findViewById(R.id.accountName);
		mEmailTextView = findViewById(R.id.accountEmail);
		mActionSignInButton = findViewById(R.id.accountActionSignIn);
		mAccountActions = findViewById(R.id.accountActions);
		mAuth = FirebaseAuth.getInstance();

		mActionSignInButton.setOnClickListener(v -> {
			Intent signInIntent = new Intent(this, LoginActivity.class);
			startActivity(signInIntent);
			finish();
		});
		mAccountActions
				.setOnClickListener(v -> {
					MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
					builder.setTitle(R.string.account_activity_account_actions)
							.setItems(R.array.account_activity_account_actions_array, (dialog, which) -> {
								switch (which) {
									case 0:
										deleteAccount(dialog);
										break;
									case 1:
										signOut(dialog);
										break;
									case 2:
										updateEmail(dialog);
										break;
									case 3:
										updateName(dialog);
										break;
									case 4:
										updatePassword(dialog);
										break;
									case 5:
										updateProfilePicture(dialog);
										break;
									default:
										Log.w(TAG, "Unknown item clicked! Index was at " + which);
										break;
								}
							})
							.show();
				});
	}

	@Override
	protected void onStart() {
		super.onStart();
		mUser = mAuth.getCurrentUser();
		if (mUser == null) {
			mDisplayNameTextView.setVisibility(View.GONE);
			mEmailTextView.setVisibility(View.GONE);
			Toast.makeText(this, "Not signed in!", Toast.LENGTH_SHORT).show();
			mAccountActions.setVisibility(View.GONE);
			mActionSignInButton.setVisibility(View.VISIBLE);
			mAvatarImageView.setVisibility(View.GONE);
		} else {
			mAccountActions.setVisibility(View.VISIBLE);
			mActionSignInButton.setVisibility(View.GONE);
			mDisplayNameTextView.setText(mUser.getDisplayName());
			mEmailTextView.setText(mUser.getEmail());
			mAvatarImageView.setVisibility(View.VISIBLE);
			Glide.with(this)
					.load(mUser.getPhotoUrl())
					.into(mAvatarImageView);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
			case R.id.action_debug:
				Intent debugIntent = new Intent(this, DebugActivity.class);
				startActivity(debugIntent);
				return true;
			case R.id.action_refresh_credentials:
				refreshCredentials();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
		FirebaseUser user = auth.getCurrentUser();
		if (user == null) {
			mDisplayNameTextView.setVisibility(View.GONE);
			mEmailTextView.setVisibility(View.GONE);
			Toast.makeText(this, "Not signed in!", Toast.LENGTH_SHORT).show();
			mAccountActions.setVisibility(View.GONE);
			mActionSignInButton.setVisibility(View.VISIBLE);
			mAvatarImageView.setVisibility(View.GONE);
		} else {
			mAccountActions.setVisibility(View.VISIBLE);
			mActionSignInButton.setVisibility(View.GONE);
			mDisplayNameTextView.setText(user.getDisplayName());
			mEmailTextView.setText(user.getEmail());
			mAvatarImageView.setVisibility(View.VISIBLE);
			Glide.with(this)
					.load(user.getPhotoUrl())
					.into(mAvatarImageView);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_account, menu);
		// Check if build is debug
		if (!BuildConfig.DEBUG) {
			menu.removeItem(R.id.action_debug);
		}
		return true;
	}

	private void deleteAccount(DialogInterface parentDialog) {
		MaterialAlertDialogBuilder confirmBuilder = new MaterialAlertDialogBuilder(this);
		confirmBuilder.setTitle(R.string.account_activity_delete_account_dialog_title)
				.setNegativeButton(R.string.dialog_action_cancel, (dialog, which) -> dialog.dismiss())
				.setPositiveButton(R.string.dialog_action_delete_account, (dialog, which) -> mUser.delete()
						.addOnCompleteListener(task -> {
							if (task.isSuccessful()) {
								Toast.makeText(this, "Successfully deleted account!", Toast.LENGTH_SHORT).show();
								parentDialog.dismiss();
							} else {
								Log.e(TAG, "An error occurred when attempting to delete the current user.", task.getException());
								if (task.getException() instanceof FirebaseAuthRecentLoginRequiredException) {
									// Reauthenticate the user to continue
									// TODO: Implement reauthentication
									GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
									AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
									mUser.reauthenticate(credential)
											.addOnCompleteListener(reAuthTask -> {
												if (reAuthTask.isSuccessful()) {
													Toast.makeText(this, "Successfully reauthenticated account!", Toast.LENGTH_SHORT).show();
													deleteAccount(parentDialog);
												} else {
													Log.e(TAG, "An error occurred while attempting to reauthenticate:", reAuthTask.getException());
												}
											});
								} else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
									Toast.makeText(this, "Current user is either disabled, deleted, or has invalid credentials", Toast.LENGTH_LONG).show();
									parentDialog.dismiss();
								}
							}
						}))
				.show();
	}

	private void refreshCredentials() {
		Snackbar.make(findViewById(R.id.constraintLayout), "Refreshing credentials...", Snackbar.LENGTH_SHORT).show();
		mUser.reload()
				.addOnCompleteListener(task -> {
					if (!task.isSuccessful()) {
						Log.e(TAG, "An error occurred while attempting to refresh the credentials:", task.getException());
						Snackbar.make(findViewById(R.id.constraintLayout), "An error occurred while attempting to refresh the credentials", Snackbar.LENGTH_LONG)
								.setAction("Retry", v -> refreshCredentials())
								.show();
					}
				});
	}

	private void signOut(DialogInterface parentDialog) {
		MaterialAlertDialogBuilder confirmBuilder = new MaterialAlertDialogBuilder(this);
		confirmBuilder.setTitle(R.string.account_activity_sign_out_dialog_title)
				.setNegativeButton(R.string.dialog_action_cancel, (dialog, which) -> dialog.dismiss())
				.setPositiveButton(R.string.dialog_action_sign_out, (dialog, which) -> {
					mAuth.signOut();
					Toast.makeText(this, "Successfully signed out!", Toast.LENGTH_SHORT).show();
					dialog.dismiss();
					parentDialog.dismiss();
				})
				.show();
	}

	private void updateEmail(DialogInterface parentDialog) {
		View promptDialogView = getLayoutInflater().inflate(R.layout.edit_text_dialog, null);
		final TextInputLayout textInputLayout = promptDialogView.findViewById(R.id.textInputLayout);
		SharedHelper.getEditText(textInputLayout)
				.setHint(R.string.account_activity_new_email_dialog_edittext_title);
		// TODO: Add check for email address
		MaterialAlertDialogBuilder promptBuilder = new MaterialAlertDialogBuilder(this);
		promptBuilder.setView(promptDialogView)
				.setTitle(R.string.account_activity_new_email_dialog_title)
				.setPositiveButton(R.string.dialog_action_update_email, (dialog, which) -> mUser.updateEmail(SharedHelper.getEditTextString(textInputLayout))
						.addOnCompleteListener(task -> {
							if (task.isSuccessful()) {
								Toast.makeText(this, "Successfully updated email address!", Toast.LENGTH_SHORT).show();
								dialog.dismiss();
								parentDialog.dismiss();
							} else {
								Log.e(TAG, "An error occurred when attempting to update the email address", task.getException());
							}
						}))
				.setNegativeButton(R.string.dialog_action_cancel, (dialog, which) -> dialog.dismiss())
				.show();
	}

	private void updateName(DialogInterface parentDialog) {
		View promptDialogView = getLayoutInflater().inflate(R.layout.edit_text_dialog, null);
		final TextInputLayout textInputLayout = promptDialogView.findViewById(R.id.textInputLayout);
		SharedHelper.getEditText(textInputLayout)
				.setHint(R.string.account_activity_new_name_dialog_edittext_title);
		MaterialAlertDialogBuilder promptBuilder = new MaterialAlertDialogBuilder(this);
		promptBuilder.setView(promptDialogView)
				.setTitle(R.string.account_activity_new_name_dialog_title)
				.setPositiveButton(R.string.dialog_action_update_name, (dialog, which) -> {
					UserProfileChangeRequest.Builder requestBuilder = new UserProfileChangeRequest.Builder();
					requestBuilder.setDisplayName(SharedHelper.getEditTextString(textInputLayout));
					mUser.updateProfile(requestBuilder.build())
							.addOnCompleteListener(task -> {
								if (task.isSuccessful()) {
									Toast.makeText(this, "Successfully updated name!", Toast.LENGTH_SHORT).show();
									dialog.dismiss();
									parentDialog.dismiss();
								} else {
									Log.e(TAG, "An error occurred when attempting to update the name", task.getException());
								}
							});
				})
				.setNegativeButton(R.string.dialog_action_cancel, (dialog, which) -> dialog.dismiss())
				.show();
	}

	private void updatePassword(DialogInterface parentDialog) {
		View promptDialogView = getLayoutInflater().inflate(R.layout.edit_text_dialog, null);
		final TextInputLayout textInputLayout = promptDialogView.findViewById(R.id.textInputLayout);
		SharedHelper.getEditText(textInputLayout)
				.setHint(R.string.account_activity_new_password_dialog_edittext_title);
		MaterialAlertDialogBuilder promptBuilder = new MaterialAlertDialogBuilder(this);
		promptBuilder.setView(promptDialogView)
				.setTitle(R.string.account_activity_new_password_dialog_title)
				.setPositiveButton(R.string.dialog_action_update_password, (dialog, which) -> mUser.updatePassword(SharedHelper.getEditTextString(textInputLayout))
						.addOnCompleteListener(task -> {
							if (task.isSuccessful()) {
								Toast.makeText(this, "Successfully updated password!", Toast.LENGTH_SHORT).show();
								dialog.dismiss();
								parentDialog.dismiss();
							} else {
								Log.e(TAG, "An error occurred when attempting to update the password", task.getException());
							}
						}))
				.setNegativeButton(R.string.dialog_action_cancel, (dialog, which) -> dialog.dismiss())
				.show();
	}

	private void updateProfilePicture(DialogInterface parentDialog) {
		// TODO: Add support for updating a profile picture
		MaterialAlertDialogBuilder promptBuilder = new MaterialAlertDialogBuilder(this);
		promptBuilder.setTitle(R.string.account_activity_new_profile_pic_dialog_title)
				.setMessage(R.string.account_activity_new_profile_pic_dialog_msg)
				.setPositiveButton(R.string.dialog_action_update_profile_picture, (dialog, which) -> {
					Toast.makeText(this, "Successfully updated profile picture!", Toast.LENGTH_SHORT).show();
					dialog.dismiss();
					parentDialog.dismiss();
				})
				.setNegativeButton(R.string.dialog_action_cancel, (dialog, which) -> dialog.dismiss())
				.show();
	}
}
