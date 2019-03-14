package com.edricchan.studybuddy.ui.modules.settings.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.crashlytics.android.Crashlytics
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.extensions.editTextStrValue
import com.edricchan.studybuddy.interfaces.NotificationAction
import com.edricchan.studybuddy.interfaces.NotificationRequest
import com.edricchan.studybuddy.utils.Constants
import com.edricchan.studybuddy.utils.SharedUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import java.io.IOException
import java.util.*

class DebugSettingsFragment : PreferenceFragmentCompat() {
	private var mUtils: SharedUtils? = null
	private var mInstanceId: FirebaseInstanceId? = null
	private var mAuth: FirebaseAuth? = null
	private var mUser: FirebaseUser? = null
	private var mCrashlytics: Crashlytics? = null
	private var mConnectivityManager: ConnectivityManager? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		mUtils = SharedUtils(context!!)
		mInstanceId = FirebaseInstanceId.getInstance()
		mCrashlytics = Crashlytics.getInstance()
		mAuth = FirebaseAuth.getInstance()
		mUser = mAuth!!.currentUser
		mConnectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
	}

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.pref_debug, rootKey)
		findPreference<Preference>(Constants.debugDeviceInfo)?.setOnPreferenceClickListener {
			showDeviceInfoDialog()
			true
		}
		findPreference<Preference>(Constants.debugAccountInfo)?.setOnPreferenceClickListener {

			var dialogMsg = ""

			if (mUser != null) {
				if (mUser!!.displayName != null) {
					dialogMsg += "Display name: " + mUser!!.displayName!!
				}
				if (mUser!!.email != null) {
					dialogMsg += "\nEmail: " + mUser!!.email!!
				}
				dialogMsg += "\nMetadata:\n- Creation timestamp: " + Date(mUser!!.metadata!!.creationTimestamp).toString()
				dialogMsg += "\n- Last signed in timestamp: " + Date(mUser!!.metadata!!.lastSignInTimestamp).toString()
				if (mUser!!.phoneNumber != null) {
					dialogMsg += "\nPhone number: " + mUser!!.phoneNumber!!
				}
				if (mUser!!.photoUrl != null) {
					dialogMsg += "\nPhoto URL: " + mUser!!.photoUrl!!
				}
				dialogMsg += "\nUID: " + mUser!!.uid
				dialogMsg += "\nIs anonymous: " + if (mUser!!.isAnonymous) "yes" else "no"
			} else {
				dialogMsg = "No current signed-in Firebase user exists!"
			}

			val builder = MaterialAlertDialogBuilder(context!!)
			builder.setTitle(R.string.debug_activity_account_info_title)
					.setMessage(dialogMsg)
					.setPositiveButton(R.string.dialog_action_dismiss) { dialog, _ -> dialog.dismiss() }
					.show()
			true
		}
		findPreference<Preference>(Constants.debugCrashApp)?.setOnPreferenceClickListener {
			val builder = MaterialAlertDialogBuilder(context!!)
			builder.setTitle(R.string.debug_activity_confirm_crash_app_dialog_title)
					.setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
					.setPositiveButton(R.string.dialog_action_crash) { _, _ -> mCrashlytics!!.crash() }
					.show()
			true
		}
		findPreference<Preference>(Constants.debugSendNotification)?.setOnPreferenceClickListener {
			val debugSendNotificationDialogView = layoutInflater.inflate(R.layout.debug_send_fcm_notification_dialog, null)
			val bodyTextInputLayout = debugSendNotificationDialogView.findViewById<TextInputLayout>(R.id.bodyTextInputLayout)
			val channelIdTextInputLayout = debugSendNotificationDialogView.findViewById<TextInputLayout>(R.id.channelIdTextInputLayout)
			val colorTextInputLayout = debugSendNotificationDialogView.findViewById<TextInputLayout>(R.id.colorTextInputLayout)
			val userOrTopicTextInputLayout = debugSendNotificationDialogView.findViewById<TextInputLayout>(R.id.userOrTopicTextInputLayout)
			val titleTextInputLayout = debugSendNotificationDialogView.findViewById<TextInputLayout>(R.id.titleTextInputLayout)
			val ttlTextInputLayout = debugSendNotificationDialogView.findViewById<TextInputLayout>(R.id.ttlTextInputLayout)

			val priorityRadioGroup = debugSendNotificationDialogView.findViewById<RadioGroup>(R.id.priorityRadioGroup)

			val builder = MaterialAlertDialogBuilder(context!!)
			builder.setTitle(R.string.debug_activity_send_notification_title)
					.setView(debugSendNotificationDialogView)
					.setIcon(R.drawable.ic_send_24dp)
					.setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
					.setPositiveButton(R.string.dialog_action_send) { dialog, _ ->
						if (userOrTopicTextInputLayout.editTextStrValue!!.isNotEmpty() &&
								titleTextInputLayout.editTextStrValue!!.isNotEmpty() &&
								bodyTextInputLayout.editTextStrValue!!.isNotEmpty()) {
							// TODO: Cleanup code
							Log.d(TAG, "Value of bodyTextInputEditText: ${bodyTextInputLayout.editTextStrValue}")
							Log.d(TAG, "Value of channelIdTextInputEditText: ${channelIdTextInputLayout.editTextStrValue}")
							Log.d(TAG, "Value of colorTextInputLayout: ${colorTextInputLayout.editTextStrValue}")
							Log.d(TAG, "Value of userOrTopicTextInputEditText: ${userOrTopicTextInputLayout.editTextStrValue}")
							Log.d(TAG, "Value of titleTextInputEditText: ${titleTextInputLayout.editTextStrValue}")
							@NotificationRequest.NotificationPriority var priority = NotificationRequest.NOTIFICATION_PRIORITY_NORMAL
							when (priorityRadioGroup.checkedRadioButtonId) {
								R.id.priorityNormalRadioButton -> {
									Log.d(TAG, "Value of priorityRadioGroup: \"normal\"")
									priority = NotificationRequest.NOTIFICATION_PRIORITY_NORMAL
								}
								R.id.priorityHighRadioButton -> {
									Log.d(TAG, "Value of priorityRadioGroup: \"normal\"")
									priority = NotificationRequest.NOTIFICATION_PRIORITY_HIGH
								}
							}
							val notificationRequestBuilder = NotificationRequest.Builder()
							if (bodyTextInputLayout.editTextStrValue!!.isNotEmpty()) {
								notificationRequestBuilder.setNotificationBody(bodyTextInputLayout.editTextStrValue!!)
							}
							if (channelIdTextInputLayout.editTextStrValue!!.isNotEmpty()) {
								notificationRequestBuilder.setNotificationChannelId(channelIdTextInputLayout.editTextStrValue!!)
							}
							if (colorTextInputLayout.editTextStrValue!!.isNotEmpty()) {
								notificationRequestBuilder.setNotificationColor(colorTextInputLayout.editTextStrValue!!)
							}
							if (priority.isNotEmpty()) {
								notificationRequestBuilder.setNotificationPriority(priority)
							}
							if (titleTextInputLayout.editTextStrValue!!.isNotEmpty()) {
								notificationRequestBuilder.setNotificationTitle(titleTextInputLayout.editTextStrValue!!)
							}
							if (userOrTopicTextInputLayout.editTextStrValue!!.isNotEmpty()) {
								notificationRequestBuilder.setUserOrTopic(userOrTopicTextInputLayout.editTextStrValue!!)
							}
							if (ttlTextInputLayout.editTextStrValue!!.isNotEmpty() && ttlTextInputLayout.editTextStrValue!!.isDigitsOnly()) {
								notificationRequestBuilder.setNotificationTtl(ttlTextInputLayout.editTextStrValue!!.toInt())
							}
							val notificationSettingsActionBuilder = NotificationAction.Builder()
							notificationSettingsActionBuilder
									.setActionTitle("Configure Notifications")
									.setActionIcon("ic_settings_24dp")
									.setActionType(Constants.actionNotificationsSettingsIntent)
							notificationRequestBuilder.addNotificationAction(notificationSettingsActionBuilder.create()!!)
							mUtils!!.sendNotificationRequest(notificationRequestBuilder.create())
									.addOnCompleteListener { task ->
										if (task.isSuccessful) {
											Log.d(TAG, "Successfully sent notification request to Cloud Firestore!")
											Toast.makeText(context, "Successfully sent notification request to Cloud Firestore!", Toast.LENGTH_SHORT)
													.show()
										} else {
											Toast.makeText(context, "An error occurred while attempting to send the notification request to Cloud Firestore. Check the logcat for more details.", Toast.LENGTH_SHORT).show()
											Log.e(TAG, "An error occurred while attempting to send the notification request to Cloud Firestore:", task.exception)
										}
										dialog.dismiss()
									}
						} else {
							Toast.makeText(context, "Please fill in the form!", Toast.LENGTH_SHORT).show()
						}
					}
			val dialog = builder.create()
			// First, show the dialog
			dialog.show()
			// Initially disable the button
			dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = false
			val validatorTextWatcher = object : TextWatcher {
				override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
					// Do nothing here
				}

				override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
					// Do nothing here
				}

				override fun afterTextChanged(s: Editable) {
					when {
						userOrTopicTextInputLayout.editText?.text.hashCode() == s.hashCode() -> {
							// Change is from user/topic TextInputLayout's TextInputEditText
							// Show error is TextInputEditText is empty
							if (TextUtils.isEmpty(s)) {
								userOrTopicTextInputLayout.error = "This is required!"
								userOrTopicTextInputLayout.isErrorEnabled = true
							} else {
								// Remove errors
								userOrTopicTextInputLayout.error = null
								userOrTopicTextInputLayout.isErrorEnabled = false
							}
							// Check if TextInputEditText is empty or if the title TextInputLayout's TextInputEditText is empty
							// or if the body TextInputLayout's TextInputEditText is empty
							dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = s.isEmpty() && titleTextInputLayout.editTextStrValue!!.isNotEmpty() && bodyTextInputLayout.editTextStrValue!!.isNotEmpty()
						}
						titleTextInputLayout.editText?.text.hashCode() == s.hashCode() -> {
							// Change is from title TextInputLayout's TextInputEditText
							// Show error is TextInputEditText is empty
							if (TextUtils.isEmpty(s)) {
								titleTextInputLayout.error = "This is required!"
								titleTextInputLayout.isErrorEnabled = true
							} else {
								// Remove errors
								titleTextInputLayout.error = null
								titleTextInputLayout.isErrorEnabled = false
							}
							// Check if TextInputEditText is empty or if the user/topic TextInputLayout's TextInputEditText is empty
							// or if the body TextInputLayout's TextInputEditText is empty
							dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = !(s.isEmpty() || userOrTopicTextInputLayout.editTextStrValue!!.isEmpty() || bodyTextInputLayout.editTextStrValue!!.isEmpty())
						}
						bodyTextInputLayout.editText?.text.hashCode() == s.hashCode() -> {
							// Change is from title TextInputLayout's TextInputEditText
							// Show error is TextInputEditText is empty
							if (TextUtils.isEmpty(s)) {
								bodyTextInputLayout.error = "This is required!"
								bodyTextInputLayout.isErrorEnabled = true
							} else {
								// Remove errors
								bodyTextInputLayout.error = null
								bodyTextInputLayout.isErrorEnabled = false
							}
							// Check if TextInputEditText is empty or if the user/topic TextInputLayout's TextInputEditText is empty
							// or if the title TextInputLayout's TextInputEditText is empty
							dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = !(s.isEmpty() || userOrTopicTextInputLayout.editTextStrValue!!.isEmpty() || titleTextInputLayout.editTextStrValue!!.isEmpty())
						}
					}
				}
			}
			// Add the watchers to the associated TextInputEditTexts
			userOrTopicTextInputLayout.editText?.addTextChangedListener(validatorTextWatcher)
			titleTextInputLayout.editText?.addTextChangedListener(validatorTextWatcher)
			bodyTextInputLayout.editText?.addTextChangedListener(validatorTextWatcher)
			true
		}
		findPreference<Preference>(Constants.debugResetInstanceId)?.setOnPreferenceClickListener {
					val builder = MaterialAlertDialogBuilder(context!!)
					builder.setTitle(R.string.debug_activity_confirm_reset_instance_id_dialog_title)
							.setMessage(R.string.debug_activity_confirm_reset_instance_id_dialog_msg)
							.setPositiveButton(R.string.dialog_action_ok) { dialog, _ ->
								try {
									mInstanceId!!.deleteInstanceId()
									Toast.makeText(context, "Successfully deleted instance ID!", Toast.LENGTH_LONG).show()
								} catch (e: IOException) {
									Toast.makeText(context, "An error occurred while deleting the device's instance ID. Please consult the logcat for the stacktrace of the exception.", Toast.LENGTH_LONG).show()
									Log.e(TAG, "An error occurred while deleting the device's instance ID: ", e)
								}

								dialog.dismiss()
							}
							.setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
							.show()
					true
				}

	}

	private fun createSdkInfoDialog(): MaterialAlertDialogBuilder {
		var dialogMsg = ""

		val deviceSdk = Build.VERSION.SDK_INT
		dialogMsg += "Device SDK: $deviceSdk"

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
			val previewSdkInt = Build.VERSION.PREVIEW_SDK_INT
			if (previewSdkInt != 0) {
				dialogMsg += "\nPreview SDK: $previewSdkInt"
			}
		}
		return MaterialAlertDialogBuilder(context!!)
				.setTitle(R.string.debug_activity_device_sdk_info_dialog_title)
				.setMessage(dialogMsg)
				.setPositiveButton(R.string.dialog_action_dismiss) { dialog, which -> dialog.dismiss() }
	}

	@SuppressLint("SwitchIntDef")
	private fun createNightModeInfoDialog(): MaterialAlertDialogBuilder {
		var dialogMsg = ""

		val defaultNightMode = AppCompatDelegate.getDefaultNightMode()
		val isLocationPermGranted = ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION)

		dialogMsg += when (defaultNightMode) {
			// Note: This is deprecated in the latest version of appcompat
			AppCompatDelegate.MODE_NIGHT_AUTO -> "\nDefault night mode: Auto (Light after sunrise, dark after sunset)"
			AppCompatDelegate.MODE_NIGHT_NO -> "\nDefault night mode: Disabled"
			AppCompatDelegate.MODE_NIGHT_YES -> "\nDefault night mode: Enabled"
			AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> "\nDefault night mode: Using system to detect"
			AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY -> "\nDefault night mode: Enabled with battery saver"

			else -> "\nDefault night mode: Unknown"
		}
		dialogMsg += when (isLocationPermGranted) {
			PackageManager.PERMISSION_GRANTED -> "\nIs location permission granted: Yes"
			PackageManager.PERMISSION_DENIED -> "\nIs location permission granted: No"
			else -> "\nIs location permission granted: Unknown"
		}
		return MaterialAlertDialogBuilder(context!!)
				.setTitle(R.string.debug_activity_night_mode_info_dialog_title)
				.setMessage(dialogMsg)
				.setPositiveButton(R.string.dialog_action_dismiss) { dialog, which -> dialog.dismiss() }
	}

	private fun createConnectivityInfoDialog(): MaterialAlertDialogBuilder {
		var dialogMsg = ""

		val isNetworkMetered = mConnectivityManager!!.isActiveNetworkMetered
		val isNetworkActive = mConnectivityManager!!.isDefaultNetworkActive
		val isNetworkPermGranted = ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_NETWORK_STATE)

		dialogMsg += "\nIs network metered: $isNetworkMetered"
		dialogMsg += "\nIs network active: $isNetworkActive"
		dialogMsg += when (isNetworkPermGranted) {
			PackageManager.PERMISSION_GRANTED -> "\nIs network permission granted: Yes"
			PackageManager.PERMISSION_DENIED -> "\nIs network permission granted: No"
			else -> "\nIs network permission granted: Unknown"
		}
		return MaterialAlertDialogBuilder(context!!)
				.setTitle(R.string.debug_activity_connectivity_info_dialog_title)
				.setMessage(dialogMsg)
				.setPositiveButton(R.string.dialog_action_dismiss) { dialog, which -> dialog.dismiss() }
	}

	private fun showDeviceInfoDialog() {
		val builder = MaterialAlertDialogBuilder(context!!)
		builder.setTitle(R.string.debug_activity_device_info_title)
				.setItems(R.array.debug_activity_device_info_array) { dialog, which ->
					when (which) {
						0 -> createSdkInfoDialog().show()
						1 -> createConnectivityInfoDialog().show()
						2 -> createNightModeInfoDialog().show()
						else -> Log.w(TAG, "Unknown item clicked! Index was at $which")
					}
					dialog.dismiss()
				}
				.setPositiveButton(R.string.dialog_action_dismiss) { dialog, _ -> dialog.dismiss() }
				.show()
	}

	companion object {
		private val TAG = SharedUtils.getTag(this::class.java)
	}
}
