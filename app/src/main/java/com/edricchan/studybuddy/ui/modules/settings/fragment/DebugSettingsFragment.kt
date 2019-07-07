package com.edricchan.studybuddy.ui.modules.settings.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
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
import androidx.core.content.edit
import androidx.core.content.getSystemService
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.Observer
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import androidx.work.Operation
import com.crashlytics.android.Crashlytics
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.constants.sharedprefs.UpdateInfoPrefConstants
import com.edricchan.studybuddy.extensions.*
import com.edricchan.studybuddy.interfaces.NotificationAction
import com.edricchan.studybuddy.interfaces.NotificationRequest
import com.edricchan.studybuddy.utils.SharedUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import com.takisoft.preferencex.PreferenceFragmentCompat
import java.io.IOException
import java.util.*

class DebugSettingsFragment : PreferenceFragmentCompat() {

	@Suppress("unused")
	class DebugUpdateInfoSettingsFragment : PreferenceFragmentCompat(),
			SharedPreferences.OnSharedPreferenceChangeListener {

		private lateinit var updateInfoPreferences: SharedPreferences
		private lateinit var lastCheckedForUpdatesDate: Date
		private lateinit var lastUpdatedDate: Date

		override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
			when (key) {
				UpdateInfoPrefConstants.PREF_LAST_CHECKED_FOR_UPDATES_DATE ->
					lastCheckedForUpdatesDate = sharedPreferences.getLong(key, 0L).toDate()
				UpdateInfoPrefConstants.PREF_LAST_UPDATED_DATE -> lastUpdatedDate = sharedPreferences.getLong(key, 0L).toDate()
			}
		}

		override fun onSaveInstanceState(outState: Bundle) {
			outState.putLong(LAST_CHECKED_FOR_UPDATES_DATE_TAG, lastCheckedForUpdatesDate.time)
			outState.putLong(LAST_UPDATED_DATE_TAG, lastUpdatedDate.time)
			super.onSaveInstanceState(outState)
		}

		override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
			setPreferencesFromResource(R.xml.pref_debug_update_info, rootKey)
			activity?.let {
				updateInfoPreferences = it.getSharedPreferences(UpdateInfoPrefConstants.FILE_UPDATE_INFO, Context.MODE_PRIVATE)
						.apply {
							registerOnSharedPreferenceChangeListener(this@DebugUpdateInfoSettingsFragment)
						}
			}

			if (savedInstanceState != null) {
				lastCheckedForUpdatesDate = savedInstanceState.getLong(LAST_CHECKED_FOR_UPDATES_DATE_TAG).toDate()
				lastUpdatedDate = savedInstanceState.getLong(LAST_UPDATED_DATE_TAG).toDate()
			} else {
				lastCheckedForUpdatesDate = updateInfoPreferences
						.getLong(UpdateInfoPrefConstants.PREF_LAST_CHECKED_FOR_UPDATES_DATE, 0L).toDate()
				lastUpdatedDate = updateInfoPreferences.getLong(UpdateInfoPrefConstants.PREF_LAST_UPDATED_DATE, 0L).toDate()
			}

			findPreference<Preference>(Constants.debugUpdatesLastCheckedForUpdatesDate)?.apply {
				if (lastCheckedForUpdatesDate.time != 0L) {
					if (!isEnabled) {
						isEnabled = true
					}
					summary = lastCheckedForUpdatesDate.toFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
				} else {
					// Disable preference as the app hasn't checked for updates yet
					isEnabled = false
					summary = getString(R.string.debug_activity_updates_last_checked_for_updates_date_summary_default)
				}
			}
			findPreference<Preference>(Constants.debugUpdatesClearLastCheckedForUpdatesDate)?.apply {
				onPreferenceClickListener = Preference.OnPreferenceClickListener {
					MaterialAlertDialogBuilder(context).apply {
						setTitle(R.string.debug_activity_updates_clear_last_checked_for_updates_date_confirm_dialog_title)
						setNegativeButton(R.string.dialog_action_cancel, null)
						setPositiveButton(R.string.dialog_action_clear) { dialogInterface, _ ->
							updateInfoPreferences.edit {
								remove(UpdateInfoPrefConstants.PREF_LAST_CHECKED_FOR_UPDATES_DATE)
							}
							dialogInterface.dismiss()
						}
					}.show()
					true
				}
			}

			findPreference<Preference>(Constants.debugUpdatesLastUpdatedDate)?.apply {
				if (lastUpdatedDate.time != 0L) {
					if (!isEnabled) {
						isEnabled = true
					}
					summary = lastUpdatedDate.toFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
				} else {
					// Disable preference as the app hasn't been updated yet
					isEnabled = false
					summary = getString(R.string.debug_activity_updates_last_updated_date_summary_default)
				}
			}
			findPreference<Preference>(Constants.debugUpdatesClearLastUpdatedDate)?.apply {
				setOnPreferenceClickListener {
					MaterialAlertDialogBuilder(context).apply {
						setTitle(R.string.debug_activity_updates_clear_last_updated_date_confirm_dialog_title)
						setNegativeButton(R.string.dialog_action_cancel, null)
						setPositiveButton(R.string.dialog_action_clear) { dialogInterface, _ ->
							updateInfoPreferences.edit {
								remove(UpdateInfoPrefConstants.PREF_LAST_UPDATED_DATE)
							}
							dialogInterface.dismiss()
						}
					}.show()
					return@setOnPreferenceClickListener true
				}
			}
		}

		companion object {
			// Used for saving the last checked for updates date
			private const val LAST_CHECKED_FOR_UPDATES_DATE_TAG = "lastCheckedForUpdatesDate"
			// Used for saving the last updated date
			private const val LAST_UPDATED_DATE_TAG = "lastUpdatedDate"
		}
	}

	private var mConnectivityManager: ConnectivityManager? = null
	private var mUser: FirebaseUser? = null
	private lateinit var mUtils: SharedUtils
	private lateinit var mInstanceId: FirebaseInstanceId
	private lateinit var mAuth: FirebaseAuth
	private lateinit var mCrashlytics: Crashlytics

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		mUtils = SharedUtils()
		mInstanceId = FirebaseInstanceId.getInstance()
		mCrashlytics = Crashlytics.getInstance()
		mAuth = FirebaseAuth.getInstance()
		mUser = mAuth.currentUser
		mConnectivityManager = context?.getSystemService()
	}

	override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.pref_debug, rootKey)
		findPreference<Preference>(Constants.debugDeviceInfo)?.setOnPreferenceClickListener {
			showDeviceInfoDialog()
			true
		}

		findPreference<Preference>(Constants.debugAccountInfo)?.setOnPreferenceClickListener {

			var dialogMsg = ""

			if (mUser != null) {
				dialogMsg += "Display name: ${mUser?.displayName ?: "<not set>"}"
				dialogMsg += "\nEmail: ${mUser?.email ?: "<not set>"}"
				dialogMsg += "\nMetadata:\n- Creation timestamp: ${mUser?.metadata?.creationTimestamp
						.toDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ") ?: "<not set>"}"
				dialogMsg += "\n- Last sign in timestamp: ${mUser?.metadata?.lastSignInTimestamp
						.toDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ") ?: "<not set>"}"
				dialogMsg += "\nPhone number: ${mUser?.phoneNumber ?: "<not set>"}"
				dialogMsg += "\nPhoto URL: ${mUser?.photoUrl ?: "<not set>"}"
				dialogMsg += "\nUID: ${mUser?.uid ?: "<not set>"}"
				dialogMsg += "\nIs anonymous: ${if (mUser?.isAnonymous == true) "yes" else "no"}"
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
					.setPositiveButton(R.string.dialog_action_crash) { _, _ -> mCrashlytics?.crash() }
					.show()
			true
		}

		findPreference<Preference>(Constants.debugSendNotification)?.setOnPreferenceClickListener {
			val debugSendNotificationDialogView = layoutInflater.inflate(R.layout.debug_send_fcm_notification_dialog, null)
			val bodyTextInputLayout = debugSendNotificationDialogView.findViewById<TextInputLayout>(R.id.bodyTextInputLayout)
			val channelIdTextInputLayout = debugSendNotificationDialogView
					.findViewById<TextInputLayout>(R.id.channelIdTextInputLayout)
			val colorTextInputLayout = debugSendNotificationDialogView.findViewById<TextInputLayout>(R.id.colorTextInputLayout)
			val userOrTopicTextInputLayout = debugSendNotificationDialogView
					.findViewById<TextInputLayout>(R.id.userOrTopicTextInputLayout)
			val titleTextInputLayout = debugSendNotificationDialogView.findViewById<TextInputLayout>(R.id.titleTextInputLayout)
			val ttlTextInputLayout = debugSendNotificationDialogView.findViewById<TextInputLayout>(R.id.ttlTextInputLayout)

			val priorityRadioGroup = debugSendNotificationDialogView.findViewById<RadioGroup>(R.id.priorityRadioGroup)

			val builder = MaterialAlertDialogBuilder(context!!)
			builder.setTitle(R.string.debug_activity_send_notification_title)
					.setView(debugSendNotificationDialogView)
					.setIcon(R.drawable.ic_send_outline_24dp)
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
									Log.d(TAG, "Value of priorityRadioGroup: \"high\"")
									priority = NotificationRequest.NOTIFICATION_PRIORITY_HIGH
								}
							}
							val notificationRequestBuilder = NotificationRequest.Builder(context!!)
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
							notificationRequestBuilder.create()?.let { it1 ->
								mUtils!!.sendNotificationRequest(it1)
										.addOnCompleteListener { task ->
											if (task.isSuccessful) {
												Log.d(TAG, "Successfully sent notification request to Cloud Firestore!")
												Toast.makeText(context, "Successfully sent notification request to Cloud Firestore!", Toast.LENGTH_SHORT)
														.show()
											} else {
												Toast.makeText(context, "An error occurred while attempting to send the notification request" +
														" to Cloud Firestore. Check the logcat for more details.", Toast.LENGTH_SHORT).show()
												Log.e(TAG, "An error occurred while attempting to send the notification request to Cloud Firestore:",
														task.exception)
											}
											dialog.dismiss()
										}
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
							dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = s.isEmpty() &&
									titleTextInputLayout.editTextStrValue!!.isNotEmpty() &&
									bodyTextInputLayout.editTextStrValue!!.isNotEmpty()
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
							dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = !(s.isEmpty() ||
									userOrTopicTextInputLayout.editTextStrValue!!.isEmpty() ||
									bodyTextInputLayout.editTextStrValue!!.isEmpty())
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
							dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = !(s.isEmpty() ||
									userOrTopicTextInputLayout.editTextStrValue!!.isEmpty() ||
									titleTextInputLayout.editTextStrValue!!.isEmpty())
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

		// TODO: Fix broken code
		findPreference<Preference>(Constants.debugResetInstanceId)?.setOnPreferenceClickListener {
			val builder = MaterialAlertDialogBuilder(context!!)
			builder.setTitle(R.string.debug_activity_confirm_reset_instance_id_dialog_title)
					.setMessage(R.string.debug_activity_confirm_reset_instance_id_dialog_msg)
					.setPositiveButton(R.string.dialog_action_ok) { dialog, _ ->
						try {
							mInstanceId!!.deleteInstanceId()
							Toast.makeText(context, "Successfully deleted instance ID!", Toast.LENGTH_LONG).show()
						} catch (e: IOException) {
							Toast.makeText(context, "An error occurred while deleting the device's instance ID." +
									" Please consult the logcat for the stacktrace of the exception.", Toast.LENGTH_LONG).show()
							Log.e(TAG, "An error occurred while deleting the device's instance ID: ", e)
						}

						dialog.dismiss()
					}
					.setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
					.show()
			true
		}

		findPreference<Preference>(Constants.debugUpdatesStartWorker)?.setOnPreferenceClickListener {
			SharedUtils.enqueueCheckForUpdatesWorker(requireContext())?.state
					?.observe(viewLifecycleOwner, Observer { state ->
						@SuppressLint("RestrictedApi")
						if (state.isNotNull() && state == Operation.IN_PROGRESS || state == Operation.SUCCESS) {
							Log.d(TAG, "Successfully enqueued worker request with state $state")
						}
					})
			true
		}

		findPreference<Preference>(Constants.debugOtherClearAppSettings)?.setOnPreferenceClickListener {
			MaterialAlertDialogBuilder(requireContext()).apply {
				setTitle(R.string.debug_activity_confirm_clear_app_settings_dialog_title)
				setMessage(R.string.debug_activity_confirm_clear_app_settings_dialog_msg)
				setPositiveButton(R.string.dialog_action_clear) { dialog, _ ->
					val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
					sharedPreferences.edit {
						clear()
					}

					Toast.makeText(context, "Successfully cleared app settings!", Toast.LENGTH_SHORT).show()
					dialog.dismiss()
				}
				setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
			}.show()
			true
		}
	}

	private fun createSdkInfoDialog(): MaterialAlertDialogBuilder {
		var dialogMsg = ""

		dialogMsg += "Device SDK: ${Build.VERSION.SDK_INT}"

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			val previewSdkInt = Build.VERSION.PREVIEW_SDK_INT
			if (previewSdkInt != 0) {
				dialogMsg += "\nPreview SDK: $previewSdkInt"
			}
		}

		if (Build.FINGERPRINT.isNotEmpty()) {
			dialogMsg += "\nBuild fingerprint: ${Build.FINGERPRINT}"
		}

		if (Build.MODEL.isNotEmpty()) {
			dialogMsg += "\nModel: ${Build.MODEL}"
		}

		if (Build.BOARD.isNotEmpty()) {
			dialogMsg += "\nBoard: ${Build.BOARD}"
		}

		if (Build.BOOTLOADER.isNotEmpty()) {
			dialogMsg += "\nBootloader version: ${Build.BOOTLOADER}"
		}

		if (Build.BRAND.isNotEmpty()) {
			dialogMsg += "\nBrand: ${Build.BRAND}"
		}

		if (Build.DEVICE.isNotEmpty()) {
			dialogMsg += "\nDevice: ${Build.DEVICE}"
		}

		if (Build.DISPLAY.isNotEmpty()) {
			dialogMsg += "\nDisplay: ${Build.DISPLAY}"
		}

		if (Build.HARDWARE.isNotEmpty()) {
			dialogMsg += "\nHardware name: ${Build.HARDWARE}"
		}

		if (Build.HOST.isNotEmpty()) {
			dialogMsg += "\nHost: ${Build.HOST}"
		}

		if (Build.ID.isNotEmpty()) {
			dialogMsg += "\nID: ${Build.ID}"
		}

		if (Build.MANUFACTURER.isNotEmpty()) {
			dialogMsg += "\nManufacturer: ${Build.MANUFACTURER}"
		}

		if (Build.PRODUCT.isNotEmpty()) {
			dialogMsg += "\nProduct: ${Build.PRODUCT}"
		}

		if (Build.SUPPORTED_ABIS.isNotEmpty()) {
			dialogMsg += "\nSupported ABIs: ${Build.SUPPORTED_ABIS.joinToString()}"
		}

		if (Build.TAGS.isNotEmpty()) {
			dialogMsg += "\nTags: ${Build.TAGS}"
		}

		dialogMsg += "\nTime: ${Build.TIME}"

		if (Build.TYPE.isNotEmpty()) {
			dialogMsg += "\nType: ${Build.TYPE}"
		}

		if (Build.USER.isNotEmpty()) {
			dialogMsg += "\nUser: ${Build.USER}"
		}

		if (Build.getRadioVersion().isNotEmpty()) {
			dialogMsg += "\nRadio firmware version: ${Build.getRadioVersion()}"
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
			@Suppress("DEPRECATION")
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
				.setPositiveButton(R.string.dialog_action_dismiss) { dialog, _ -> dialog.dismiss() }
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
		private val TAG = SharedUtils.getTag(DebugSettingsFragment::class.java)
	}
}
