package com.edricchan.studybuddy.settings;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceFragmentCompat;

import com.crashlytics.android.Crashlytics;
import com.edricchan.studybuddy.R;
import com.edricchan.studybuddy.SharedHelper;
import com.edricchan.studybuddy.interfaces.NotificationAction;
import com.edricchan.studybuddy.interfaces.NotificationRequest;
import com.edricchan.studybuddy.utils.DataUtil;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.Date;

public class DebugSettingsFragment extends PreferenceFragmentCompat {
	private SharedHelper mHelper;
	private FirebaseInstanceId mInstanceId;
	private FirebaseAuth mAuth;
	private FirebaseUser mUser;
	private Crashlytics mCrashlytics;
	private ConnectivityManager mConnectivityManager;
	private static final String TAG = SharedHelper.getTag(DebugSettingsFragment.class);

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHelper = new SharedHelper(getContext());
		mInstanceId = FirebaseInstanceId.getInstance();
		mCrashlytics = Crashlytics.getInstance();
		mAuth = FirebaseAuth.getInstance();
		mUser = mAuth.getCurrentUser();
		mConnectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	@Override
	public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {
		setPreferencesFromResource(R.xml.pref_debug, rootKey);
		findPreference(DataUtil.debugDeviceInfo)
				.setOnPreferenceClickListener(preference -> {
					showDeviceInfoDialog();
					return true;
				});
		findPreference(DataUtil.debugAccountInfo)
				.setOnPreferenceClickListener(preference -> {

					String dialogMsg = "";

					if (mUser != null) {
						dialogMsg += "Display name: " + mUser.getDisplayName();
						dialogMsg += "\nEmail: " + mUser.getEmail();
						dialogMsg += "\nMetadata:\n- Creation timestamp: " + new Date(mUser.getMetadata().getCreationTimestamp()).toString();
						dialogMsg += "\n- Last signed in timestamp: " + new Date(mUser.getMetadata().getLastSignInTimestamp()).toString();
						dialogMsg += "\nPhone number: " + mUser.getPhoneNumber();
						dialogMsg += "\nPhoto URL: " + mUser.getPhotoUrl();
						dialogMsg += "\nUID: " + mUser.getUid();
						dialogMsg += "\nIs anonymous: " + (mUser.isAnonymous() ? "yes" : "no");
					} else {
						dialogMsg = "No current signed-in Firebase user exists!";
					}

					AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
					builder.setTitle(R.string.debug_activity_account_info_title)
							.setMessage(dialogMsg)
							.setPositiveButton(R.string.dialog_action_dismiss, (dialog, which) -> dialog.dismiss())
							.show();
					return true;
				});
		findPreference(DataUtil.debugCrashApp)
				.setOnPreferenceClickListener(preference -> {
					AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
					builder.setTitle(R.string.debug_activity_confirm_crash_app_dialog_title)
							.setNegativeButton(R.string.dialog_action_cancel, (dialog, which) -> dialog.dismiss())
							.setPositiveButton(R.string.dialog_action_crash, (dialog, which) -> mCrashlytics.crash())
							.show();
					return true;
				});
		findPreference(DataUtil.debugSendNotification)
				.setOnPreferenceClickListener(preference -> {
					View debugSendNotificationDialogView = getLayoutInflater().inflate(R.layout.debug_send_fcm_notification_dialog, null);
					final TextInputLayout bodyTextInputLayout = debugSendNotificationDialogView.findViewById(R.id.bodyTextInputLayout);
					final TextInputLayout channelIdTextInputLayout = debugSendNotificationDialogView.findViewById(R.id.channelIdTextInputLayout);
					final TextInputLayout colorTextInputLayout = debugSendNotificationDialogView.findViewById(R.id.colorTextInputLayout);
					final TextInputLayout userOrTopicTextInputLayout = debugSendNotificationDialogView.findViewById(R.id.userOrTopicTextInputLayout);
					final TextInputLayout titleTextInputLayout = debugSendNotificationDialogView.findViewById(R.id.titleTextInputLayout);
					final TextInputLayout ttlTextInputLayout = debugSendNotificationDialogView.findViewById(R.id.ttlTextInputLayout);

					final RadioGroup priorityRadioGroup = debugSendNotificationDialogView.findViewById(R.id.priorityRadioGroup);

					AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
					builder.setTitle(R.string.debug_activity_send_notification_title)
							.setView(debugSendNotificationDialogView)
							.setIcon(R.drawable.ic_send_24dp)
							.setNegativeButton(R.string.dialog_action_cancel, (dialog, which) -> dialog.dismiss())
							.setPositiveButton(R.string.dialog_action_send, (dialog, which) -> {
								if (
										SharedHelper.getEditTextString(userOrTopicTextInputLayout).trim().length() > 0 &&
												SharedHelper.getEditTextString(titleTextInputLayout).trim().length() > 0 &&
												SharedHelper.getEditTextString(bodyTextInputLayout).trim().length() > 0
								) {
									// TODO: Cleanup code
									Log.d(TAG, "Value of bodyTextInputEditText: \"" + SharedHelper.getEditTextString(bodyTextInputLayout) + "\"");
									Log.d(TAG, "Value of channelIdTextInputEditText: \"" + SharedHelper.getEditTextString(channelIdTextInputLayout) + "\"");
									Log.d(TAG, "Value of colorTextInputLayout: \"" + SharedHelper.getEditTextString(colorTextInputLayout) + "\"");
									Log.d(TAG, "Value of userOrTopicTextInputEditText: \"" + SharedHelper.getEditTextString(userOrTopicTextInputLayout) + "\"");
									Log.d(TAG, "Value of titleTextInputEditText: \"" + SharedHelper.getEditTextString(titleTextInputLayout) + "\"");
									@NotificationRequest.NotificationPriority String priority = NotificationRequest.NOTIFICATION_PRIORITY_NORMAL;
									switch (priorityRadioGroup.getCheckedRadioButtonId()) {
										case R.id.priorityNormalRadioButton:
											Log.d(TAG, "Value of priorityRadioGroup: \"normal\"");
											priority = NotificationRequest.NOTIFICATION_PRIORITY_NORMAL;
											break;
										case R.id.priorityHighRadioButton:
											Log.d(TAG, "Value of priorityRadioGroup: \"normal\"");
											priority = NotificationRequest.NOTIFICATION_PRIORITY_HIGH;
											break;
									}
									NotificationRequest.Builder notificationRequestBuilder = new NotificationRequest.Builder();
									if (!SharedHelper.getEditTextString(bodyTextInputLayout).isEmpty()) {
										notificationRequestBuilder.setNotificationBody(SharedHelper.getEditTextString(bodyTextInputLayout));
									}
									if (!SharedHelper.getEditTextString(channelIdTextInputLayout).isEmpty()) {
										notificationRequestBuilder.setNotificationChannelId(SharedHelper.getEditTextString(channelIdTextInputLayout));
									}
									if (!SharedHelper.getEditTextString(colorTextInputLayout).isEmpty()) {
										notificationRequestBuilder.setNotificationColor(SharedHelper.getEditTextString(colorTextInputLayout));
									}
									if (!priority.isEmpty()) {
										notificationRequestBuilder.setNotificationPriority(priority);
									}
									if (!SharedHelper.getEditTextString(titleTextInputLayout).isEmpty()) {
										notificationRequestBuilder.setNotificationTitle(SharedHelper.getEditTextString(titleTextInputLayout));
									}
									if (!SharedHelper.getEditTextString(userOrTopicTextInputLayout).isEmpty()) {
										notificationRequestBuilder.setUserOrTopic(SharedHelper.getEditTextString(userOrTopicTextInputLayout));
									}
									if (!SharedHelper.getEditTextString(ttlTextInputLayout).isEmpty()) {
										notificationRequestBuilder.setNotificationTtl(Integer.parseInt(SharedHelper.getEditTextString(ttlTextInputLayout)));
									}
									NotificationAction.Builder notificationSettingsActionBuilder = new NotificationAction.Builder();
									notificationSettingsActionBuilder
											.setActionTitle("Configure Notifications")
											.setActionIcon("ic_settings_24dp")
											.setActionType(DataUtil.actionNotificationsSettingsIntent);
									notificationRequestBuilder.addNotificationAction(notificationSettingsActionBuilder.create());
									mHelper.sendNotificationRequest(notificationRequestBuilder.create())
											.addOnCompleteListener(task -> {
												if (task.isSuccessful()) {
													Log.d(TAG, "Successfully sent notification request to Cloud Firestore!");
													Toast.makeText(getContext(), "Successfully sent notification request to Cloud Firestore!", Toast.LENGTH_SHORT)
															.show();
												} else {
													Toast.makeText(getContext(), "An error occurred while attempting to send the notification request to Cloud Firestore. Check the logcat for more details.", Toast.LENGTH_SHORT).show();
													Log.e(TAG, "An error occurred while attempting to send the notification request to Cloud Firestore:", task.getException());
												}
												dialog.dismiss();
											});
								} else {
									Toast.makeText(getContext(), "Please fill in the form!", Toast.LENGTH_SHORT).show();
								}
							});
					final AlertDialog dialog = builder.create();
					// First, show the dialog
					dialog.show();
					// Initially disable the button
					dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
					final TextWatcher validatorTextWatcher = new TextWatcher() {
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count, int after) {
							// Do nothing here
						}

						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {
							// Do nothing here
						}

						@Override
						public void afterTextChanged(Editable s) {
							if (SharedHelper.getEditText(userOrTopicTextInputLayout).getText().hashCode() == s.hashCode()) {
								// Change is from user/topic TextInputLayout's TextInputEditText
								// Show error is TextInputEditText is empty
								if (TextUtils.isEmpty(s)) {
									userOrTopicTextInputLayout.setError("This is required!");
									userOrTopicTextInputLayout.setErrorEnabled(true);
								} else {
									// Remove errors
									userOrTopicTextInputLayout.setError(null);
									userOrTopicTextInputLayout.setErrorEnabled(false);
								}
								// Check if TextInputEditText is empty or if the title TextInputLayout's TextInputEditText is empty
								// or if the body TextInputLayout's TextInputEditText is empty
								if (TextUtils.isEmpty(s) || SharedHelper.getEditTextString(titleTextInputLayout).isEmpty() || SharedHelper.getEditTextString(bodyTextInputLayout).isEmpty()) {
									// TextInputEditText is empty! Disable the dialog's positive button
									dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
								} else {
									// TextInputEditText is not empty!
									dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
								}
							} else if (SharedHelper.getEditText(titleTextInputLayout).getText().hashCode() == s.hashCode()) {
								// Change is from title TextInputLayout's TextInputEditText
								// Show error is TextInputEditText is empty
								if (TextUtils.isEmpty(s)) {
									titleTextInputLayout.setError("This is required!");
									titleTextInputLayout.setErrorEnabled(true);
								} else {
									// Remove errors
									titleTextInputLayout.setError(null);
									titleTextInputLayout.setErrorEnabled(false);
								}
								// Check if TextInputEditText is empty or if the user/topic TextInputLayout's TextInputEditText is empty
								// or if the body TextInputLayout's TextInputEditText is empty
								if (TextUtils.isEmpty(s) || SharedHelper.getEditTextString(userOrTopicTextInputLayout).isEmpty() || SharedHelper.getEditTextString(bodyTextInputLayout).isEmpty()) {
									// TextInputEditText is empty! Disable the dialog's positive button
									dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
								} else {
									// TextInputEditText is not empty!
									dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
								}
							} else if (SharedHelper.getEditText(bodyTextInputLayout).getText().hashCode() == s.hashCode()) {
								// Change is from title TextInputLayout's TextInputEditText
								// Show error is TextInputEditText is empty
								if (TextUtils.isEmpty(s)) {
									bodyTextInputLayout.setError("This is required!");
									bodyTextInputLayout.setErrorEnabled(true);
								} else {
									// Remove errors
									bodyTextInputLayout.setError(null);
									bodyTextInputLayout.setErrorEnabled(false);
								}
								// Check if TextInputEditText is empty or if the user/topic TextInputLayout's TextInputEditText is empty
								// or if the title TextInputLayout's TextInputEditText is empty
								if (TextUtils.isEmpty(s) || SharedHelper.getEditTextString(userOrTopicTextInputLayout).isEmpty() || SharedHelper.getEditTextString(titleTextInputLayout).isEmpty()) {
									// TextInputEditText is empty! Disable the dialog's positive button
									dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
								} else {
									// TextInputEditText is not empty!
									dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
								}
							}
						}
					};
					// Add the watchers to the associated TextInputEditTexts
					SharedHelper.getEditText(userOrTopicTextInputLayout)
							.addTextChangedListener(validatorTextWatcher);
					SharedHelper.getEditText(titleTextInputLayout)
							.addTextChangedListener(validatorTextWatcher);
					SharedHelper.getEditText(bodyTextInputLayout)
							.addTextChangedListener(validatorTextWatcher);
					return true;
				});
		findPreference(DataUtil.debugResetInstanceId)
				.setOnPreferenceClickListener(preference -> {
					AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
					builder.setTitle(R.string.debug_activity_confirm_reset_instance_id_dialog_title)
							.setMessage(R.string.debug_activity_confirm_reset_instance_id_dialog_msg)
							.setPositiveButton(R.string.dialog_action_ok, (dialog, which) -> {
								try {
									mInstanceId.deleteInstanceId();
									Toast.makeText(getContext(), "Successfully deleted instance ID!", Toast.LENGTH_LONG).show();
								} catch (IOException e) {
									Toast.makeText(getContext(), "An error occurred while deleting the device's instance ID. Please consult the logcat for the stacktrace of the exception.", Toast.LENGTH_LONG).show();
									Log.e(TAG, "An error occurred while deleting the device's instance ID: ", e);
								}
								dialog.dismiss();
							})
							.setNegativeButton(R.string.dialog_action_cancel, (dialog, which) -> dialog.dismiss())
							.show();
					return true;
				});

	}

	private AlertDialog.Builder createSdkInfoDialog() {
		String dialogMsg = "";

		final int deviceSdk = Build.VERSION.SDK_INT;
		dialogMsg += "Device SDK: " + deviceSdk;

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
			final int previewSdkInt = Build.VERSION.PREVIEW_SDK_INT;
			if (previewSdkInt != 0) {
				dialogMsg += "\nPreview SDK: " + previewSdkInt;
			}
		}
		return new AlertDialog.Builder(getContext())
				.setTitle(R.string.debug_activity_device_sdk_info_dialog_title)
				.setMessage(dialogMsg)
				.setPositiveButton(R.string.dialog_action_dismiss, (dialog, which) -> dialog.dismiss());
	}

	@SuppressLint("SwitchIntDef")
	private AlertDialog.Builder createNightModeInfoDialog() {
		String dialogMsg = "";

		final int defaultNightMode = AppCompatDelegate.getDefaultNightMode();
		final int isLocationPermGranted = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);

		switch (defaultNightMode) {
			case AppCompatDelegate.MODE_NIGHT_AUTO:
				dialogMsg += "\nDefault night mode: Auto";
				break;
			case AppCompatDelegate.MODE_NIGHT_NO:
				dialogMsg += "\nDefault night mode: Disabled";
				break;
			case AppCompatDelegate.MODE_NIGHT_YES:
				dialogMsg += "\nDefault night mode: Enabled";
				break;
			case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
				dialogMsg += "\nDefault night mode: Using system to detect";
				break;
			default:
				dialogMsg += "\nDefault night mode: Unknown";
				break;
		}
		switch (isLocationPermGranted) {
			case PackageManager.PERMISSION_GRANTED:
				dialogMsg += "\nIs location permission granted: Yes";
				break;
			case PackageManager.PERMISSION_DENIED:
				dialogMsg += "\nIs location permission granted: No";
				break;
			default:
				dialogMsg += "\nIs location permission granted: Unknown";
				break;
		}
		return new AlertDialog.Builder(getContext())
				.setTitle(R.string.debug_activity_night_mode_info_dialog_title)
				.setMessage(dialogMsg)
				.setPositiveButton(R.string.dialog_action_dismiss, (dialog, which) -> dialog.dismiss());
	}

	private AlertDialog.Builder createConnectivityInfoDialog() {
		String dialogMsg = "";

		final boolean isNetworkMetered = mConnectivityManager.isActiveNetworkMetered();
		final boolean isNetworkActive = mConnectivityManager.isDefaultNetworkActive();
		final int isNetworkPermGranted = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_NETWORK_STATE);

		dialogMsg += "\nIs network metered: " + isNetworkMetered;
		dialogMsg += "\nIs network active: " + isNetworkActive;
		switch (isNetworkPermGranted) {
			case PackageManager.PERMISSION_GRANTED:
				dialogMsg += "\nIs network permission granted: Yes";
				break;
			case PackageManager.PERMISSION_DENIED:
				dialogMsg += "\nIs network permission granted: No";
				break;
			default:
				dialogMsg += "\nIs network permission granted: Unknown";
				break;
		}
		return new AlertDialog.Builder(getContext())
				.setTitle(R.string.debug_activity_connectivity_info_dialog_title)
				.setMessage(dialogMsg)
				.setPositiveButton(R.string.dialog_action_dismiss, (dialog, which) -> dialog.dismiss());
	}

	private void showDeviceInfoDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle(R.string.debug_activity_device_info_title)
				.setItems(R.array.debug_activity_device_info_array, (dialog, which) -> {
					switch (which) {
						case 0:
							createSdkInfoDialog().show();
							break;
						case 1:
							createConnectivityInfoDialog().show();
							break;
						case 2:
							createNightModeInfoDialog().show();
							break;
						default:
							Log.w(TAG, "Unknown item clicked! Index was at " + which);
							break;
					}
					dialog.dismiss();
				})
				.setPositiveButton(R.string.dialog_action_dismiss, (dialog, which) -> dialog.dismiss())
				.show();
	}
}
