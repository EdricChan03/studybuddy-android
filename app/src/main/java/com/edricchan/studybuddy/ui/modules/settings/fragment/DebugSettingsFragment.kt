package com.edricchan.studybuddy.ui.modules.settings.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.content.getSystemService
import androidx.core.text.isDigitsOnly
import androidx.core.widget.doAfterTextChanged
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import androidx.work.Operation
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.constants.sharedprefs.DevModePrefConstants
import com.edricchan.studybuddy.constants.sharedprefs.UpdateInfoPrefConstants
import com.edricchan.studybuddy.databinding.DebugSendFcmNotificationDialogBinding
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.extensions.editTextStrValue
import com.edricchan.studybuddy.extensions.firebase.auth.creationInstant
import com.edricchan.studybuddy.extensions.firebase.auth.lastSignInInstant
import com.edricchan.studybuddy.extensions.format
import com.edricchan.studybuddy.extensions.showToast
import com.edricchan.studybuddy.interfaces.NotificationAction
import com.edricchan.studybuddy.interfaces.NotificationRequest
import com.edricchan.studybuddy.ui.modules.debug.DebugModalBottomSheetActivity
import com.edricchan.studybuddy.ui.modules.settings.fragment.featureflags.FeatureFlagsSettingsFragment
import com.edricchan.studybuddy.ui.preference.MaterialPreferenceFragment
import com.edricchan.studybuddy.ui.theming.applyDynamicTheme
import com.edricchan.studybuddy.ui.theming.isDynamicColorAvailable
import com.edricchan.studybuddy.ui.theming.prefDynamicTheme
import com.edricchan.studybuddy.utils.enqueueUniqueCheckForUpdatesWorker
import com.edricchan.studybuddy.utils.firebase.FirebaseMessagingUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.installations.ktx.installations
import com.google.firebase.ktx.Firebase
import java.time.Instant
import java.time.format.DateTimeFormatter

private fun Instant.formatISO() = format(DateTimeFormatter.ISO_INSTANT)

class DebugSettingsFragment : MaterialPreferenceFragment() {

    @Suppress("unused")
    class DebugUpdateInfoSettingsFragment : MaterialPreferenceFragment(),
        SharedPreferences.OnSharedPreferenceChangeListener {

        private lateinit var updateInfoPreferences: SharedPreferences
        private var lastCheckedForUpdatesInstant: Instant? = null
        private var lastUpdatedInstant: Instant? = null

        private fun setLastCheckedForUpdates(lastCheckedForUpdatesMs: Long) {
            lastCheckedForUpdatesInstant =
                lastCheckedForUpdatesMs.takeIf { it <= DEFAULT_INSTANT }
                    ?.let { Instant.ofEpochMilli(it) }
        }

        private fun setLastUpdated(lastUpdatedMs: Long) {
            lastUpdatedInstant =
                lastUpdatedMs.takeIf { it <= DEFAULT_INSTANT }?.let { Instant.ofEpochMilli(it) }
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
            when (key) {
                UpdateInfoPrefConstants.PREF_LAST_CHECKED_FOR_UPDATES_DATE ->
                    setLastCheckedForUpdates(sharedPreferences.getLong(key, DEFAULT_INSTANT))

                UpdateInfoPrefConstants.PREF_LAST_UPDATED_DATE ->
                    setLastUpdated(sharedPreferences.getLong(key, DEFAULT_INSTANT))
            }
        }

        override fun onSaveInstanceState(outState: Bundle) {
            outState.putSerializable(
                LAST_CHECKED_FOR_UPDATES_DATE_TAG,
                lastCheckedForUpdatesInstant
            )
            outState.putSerializable(LAST_UPDATED_DATE_TAG, lastUpdatedInstant)
            super.onSaveInstanceState(outState)
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.pref_debug_update_info, rootKey)
            activity?.let {
                updateInfoPreferences = it.getSharedPreferences(
                    UpdateInfoPrefConstants.FILE_UPDATE_INFO,
                    Context.MODE_PRIVATE
                )
                    .apply {
                        registerOnSharedPreferenceChangeListener(this@DebugUpdateInfoSettingsFragment)
                    }
            }

            if (savedInstanceState != null) {
                lastCheckedForUpdatesInstant =
                    (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        savedInstanceState.getSerializable(
                            LAST_CHECKED_FOR_UPDATES_DATE_TAG,
                            Instant::class.java
                        )
                    else savedInstanceState.getSerializable(LAST_CHECKED_FOR_UPDATES_DATE_TAG) as? Instant)
                lastUpdatedInstant =
                    (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        savedInstanceState.getSerializable(
                            LAST_UPDATED_DATE_TAG,
                            Instant::class.java
                        )
                    else savedInstanceState.getSerializable(LAST_UPDATED_DATE_TAG) as? Instant)
            } else {
                setLastCheckedForUpdates(
                    updateInfoPreferences
                        .getLong(
                            UpdateInfoPrefConstants.PREF_LAST_CHECKED_FOR_UPDATES_DATE,
                            DEFAULT_INSTANT
                        )
                )
                setLastUpdated(
                    updateInfoPreferences
                        .getLong(UpdateInfoPrefConstants.PREF_LAST_UPDATED_DATE, DEFAULT_INSTANT)
                )
            }

            findPreference<Preference>(Constants.debugUpdatesLastCheckedForUpdatesDate)?.apply {
                isEnabled = lastCheckedForUpdatesInstant != null
                summary =
                    lastCheckedForUpdatesInstant?.formatISO() ?: getString(
                        R.string.debug_activity_updates_last_checked_for_updates_date_summary_default
                    )
            }
            findPreference<Preference>(Constants.debugUpdatesClearLastCheckedForUpdatesDate)?.apply {
                setOnPreferenceClickListener {
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
                isEnabled = lastUpdatedInstant != null
                summary = lastUpdatedInstant?.formatISO()
                    ?: getString(R.string.debug_activity_updates_last_updated_date_summary_default)
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

            private const val DEFAULT_INSTANT = -1L
        }
    }

    private var user: FirebaseUser? = null
    private var connectivityManager: ConnectivityManager? = null
    private lateinit var installations: FirebaseInstallations
    private lateinit var auth: FirebaseAuth
    private lateinit var devModeOpts: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installations = Firebase.installations
        auth = Firebase.auth
        user = auth.currentUser
        connectivityManager = requireContext().getSystemService()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_debug, rootKey)
        devModeOpts = requireContext().getSharedPreferences(
            DevModePrefConstants.FILE_DEV_MODE,
            Context.MODE_PRIVATE
        )
        findPreference<SwitchPreferenceCompat>(Constants.debugDevModeEnabled)
            ?.setOnPreferenceChangeListener { _, newValue ->
                var returnResult = true
                if (newValue == false) {
                    MaterialAlertDialogBuilder(requireContext()).apply {
                        setTitle(R.string.dev_mode_confirm_disable_dialog_title)
                        setMessage(R.string.dev_mode_confirm_disable_dialog_msg)
                        setNegativeButton(R.string.dialog_action_cancel) { dialog, _ ->
                            returnResult = false
                            dialog.dismiss()
                        }
                        setPositiveButton(R.string.dialog_action_disable) { dialog, _ ->
                            devModeOpts.edit {
                                putBoolean(DevModePrefConstants.DEV_MODE_ENABLED, false)
                                this@DebugSettingsFragment.showToast(
                                    R.string.dev_mode_off, Toast.LENGTH_LONG
                                )
                            }
                            dialog.dismiss()
                        }
                    }.show()
                } else if (newValue == true) {
                    devModeOpts.edit {
                        putBoolean(DevModePrefConstants.DEV_MODE_ENABLED, true)
                        this@DebugSettingsFragment.showToast(
                            R.string.dev_mode_on,
                            Toast.LENGTH_SHORT
                        )
                    }
                }
                Log.d(TAG, "Current value of return result: $returnResult")
                returnResult
            }
        findPreference<Preference>(Constants.debugFeatureFlags)?.fragment =
            FeatureFlagsSettingsFragment::class.java.name
        findPreference<Preference>(Constants.debugUpdatesUpdateMetadata)?.fragment =
            DebugUpdateInfoSettingsFragment::class.java.name
        findPreference<Preference>(Constants.debugOtherModalBottomSheetTesting)?.intent =
            Intent(context, DebugModalBottomSheetActivity::class.java)
        findPreference<Preference>(Constants.debugDeviceInfo)?.setOnPreferenceClickListener {
            showDeviceInfoDialog()
            true
        }

        findPreference<Preference>(Constants.debugAccountInfo)?.setOnPreferenceClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle(R.string.debug_activity_account_info_title)
                if (user != null)
                    setMessage(
                        """
                        Display name: ${user?.displayName ?: "<not set>"}
                        Email: ${user?.email ?: "<not set>"}
                        Metadata:
                        - Creation timestamp: ${
                            user?.metadata?.creationInstant
                                ?.formatISO() ?: "<not set>"
                        }
                        - Last sign in timestamp: ${
                            user?.metadata?.lastSignInInstant
                                ?.formatISO() ?: "<not set>"
                        }
                        Phone number: ${user?.phoneNumber ?: "<not set>"}
                        Photo URL: ${user?.photoUrl ?: "<not set>"}
                        UID: ${user?.uid ?: "<not set>"}
                        Is anonymous: ${user?.isAnonymous}
                    """.trimIndent()
                    )
                else setMessage("No current signed-in Firebase user exists!")
                setPositiveButton(R.string.dialog_action_dismiss) { dialog, _ -> dialog.dismiss() }
            }.show()
            true
        }

        findPreference<Preference>(Constants.debugSendNotification)?.setOnPreferenceClickListener {
            val dialogBinding =
                DebugSendFcmNotificationDialogBinding.inflate(layoutInflater, null, false)

            val builder = MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle(R.string.debug_activity_send_notification_title)
                setView(dialogBinding.root)
                setIcon(R.drawable.ic_send_outline_24dp)
                setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
                setPositiveButton(R.string.dialog_action_send) { dialog, _ ->
                    with(dialogBinding) {
                        if (userOrTopicTextInputLayout.editTextStrValue!!.isNotEmpty() &&
                            titleTextInputLayout.editTextStrValue!!.isNotEmpty() &&
                            bodyTextInputLayout.editTextStrValue!!.isNotEmpty()
                        ) {
                            // TODO: Cleanup code
                            Log.d(
                                TAG,
                                "Value of bodyTextInputEditText: ${bodyTextInputLayout.editTextStrValue}"
                            )
                            Log.d(
                                TAG,
                                "Value of channelIdTextInputEditText: ${channelIdTextInputLayout.editTextStrValue}"
                            )
                            Log.d(
                                TAG,
                                "Value of colorTextInputLayout: ${colorTextInputLayout.editTextStrValue}"
                            )
                            Log.d(
                                TAG,
                                "Value of userOrTopicTextInputEditText: ${userOrTopicTextInputLayout.editTextStrValue}"
                            )
                            Log.d(
                                TAG,
                                "Value of titleTextInputEditText: ${titleTextInputLayout.editTextStrValue}"
                            )
                            @NotificationRequest.NotificationPriority var priority =
                                NotificationRequest.NOTIFICATION_PRIORITY_NORMAL
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
                            val notificationRequest = NotificationRequest.build {
                                if (bodyTextInputLayout.editTextStrValue != null && bodyTextInputLayout.editTextStrValue!!.isNotEmpty()) {
                                    notificationBody = bodyTextInputLayout.editTextStrValue!!
                                }
                                if (channelIdTextInputLayout.editTextStrValue != null &&
                                    channelIdTextInputLayout.editTextStrValue!!.isNotEmpty()
                                ) {
                                    notificationChannelId =
                                        channelIdTextInputLayout.editTextStrValue!!
                                }
                                if (colorTextInputLayout.editTextStrValue != null && colorTextInputLayout.editTextStrValue!!.isNotEmpty()) {
                                    notificationColor = colorTextInputLayout.editTextStrValue!!
                                }
                                if (priority.isNotEmpty()) {
                                    notificationPriority = priority
                                }
                                if (titleTextInputLayout.editTextStrValue != null && titleTextInputLayout.editTextStrValue!!.isNotEmpty()) {
                                    notificationTitle = titleTextInputLayout.editTextStrValue!!
                                }
                                if (userOrTopicTextInputLayout.editTextStrValue != null &&
                                    userOrTopicTextInputLayout.editTextStrValue!!.isNotEmpty()
                                ) {
                                    userOrTopic = userOrTopicTextInputLayout.editTextStrValue!!
                                }
                                if (ttlTextInputLayout.editTextStrValue != null && ttlTextInputLayout.editTextStrValue!!.isNotEmpty() &&
                                    ttlTextInputLayout.editTextStrValue!!.isDigitsOnly()
                                ) {
                                    notificationTtl = ttlTextInputLayout.editTextStrValue!!.toLong()
                                }
                                val notificationSettingsAction = NotificationAction.build {
                                    title = "Configure Notifications"
                                    icon = "ic_settings_24dp"
                                    type = Constants.actionNotificationsSettingsIntent
                                }
                                notificationActions?.add(notificationSettingsAction)
                            }
                            FirebaseMessagingUtils.sendNotificationRequest(notificationRequest)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d(
                                            TAG,
                                            "Successfully sent notification request to Cloud Firestore!"
                                        )
                                        this@DebugSettingsFragment.showToast(
                                            "Successfully sent notification request to Cloud Firestore!",
                                            Toast.LENGTH_SHORT
                                        )
                                    } else {
                                        this@DebugSettingsFragment.showToast(
                                            "An error occurred while attempting to send the notification request" +
                                                " to Cloud Firestore. Check the logcat for more details.",
                                            Toast.LENGTH_SHORT
                                        )
                                        Log.e(
                                            TAG,
                                            "An error occurred while attempting to send the notification request to Cloud Firestore:",
                                            task.exception
                                        )
                                    }
                                    dialog.dismiss()
                                }
                        } else {
                            this@DebugSettingsFragment.showToast(
                                "Please fill in the form!",
                                Toast.LENGTH_SHORT
                            )
                        }
                    }
                }
            }

            val dialog = builder.create()
            // First, show the dialog
            dialog.show()
            // Initially disable the button
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = false
            with(dialogBinding) {
                fun textWatcher(textInputLayout: TextInputLayout) = { e: Editable? ->
                    textInputLayout.error =
                        if (e.isNullOrEmpty()) "This field is required" else null
                    // Update confirm button state
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled =
                        userOrTopicTextInputLayout.editTextStrValue!!.isNotEmpty() &&
                            titleTextInputLayout.editTextStrValue!!.isNotEmpty() &&
                            bodyTextInputLayout.editTextStrValue!!.isNotEmpty()
                }

                // Add the watchers to the associated TextInputEditTexts
                userOrTopicTextInputLayout.editText?.doAfterTextChanged(
                    textWatcher(
                        userOrTopicTextInputLayout
                    )
                )
                titleTextInputLayout.editText?.doAfterTextChanged(textWatcher(titleTextInputLayout))
                bodyTextInputLayout.editText?.doAfterTextChanged(textWatcher(bodyTextInputLayout))
            }
            true
        }

        findPreference<Preference>(Constants.debugResetInstanceId)?.setOnPreferenceClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle(R.string.debug_activity_confirm_reset_instance_id_dialog_title)
                setMessage(R.string.debug_activity_confirm_reset_instance_id_dialog_msg)
                setPositiveButton(R.string.dialog_action_ok) { dialog, _ ->
                    installations.delete().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            this@DebugSettingsFragment.showToast(
                                "Successfully deleted instance ID!",
                                Toast.LENGTH_LONG
                            )
                        } else {
                            this@DebugSettingsFragment.showToast(
                                "An error occurred while deleting the device's instance ID." +
                                    " Please consult the logcat for the stacktrace of the exception.",
                                Toast.LENGTH_LONG
                            )
                            Log.e(
                                TAG,
                                "An error occurred while deleting the device's instance ID: ",
                                task.exception
                            )
                        }
                    }
                    dialog.dismiss()
                }
                setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
            }.show()
            true
        }

        findPreference<Preference>(Constants.debugUpdatesStartWorker)?.setOnPreferenceClickListener {
            requireContext().enqueueUniqueCheckForUpdatesWorker()?.state
                ?.observe(viewLifecycleOwner) { state ->
                    if (state is Operation.State.IN_PROGRESS || state is Operation.State.SUCCESS) {
                        Log.d(TAG, "Successfully enqueued worker request with state $state")
                    }
                }
            true
        }

        findPreference<Preference>(Constants.debugOtherClearAppSettings)?.setOnPreferenceClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle(R.string.debug_activity_confirm_clear_app_settings_dialog_title)
                setMessage(R.string.debug_activity_confirm_clear_app_settings_dialog_msg)
                setPositiveButton(R.string.dialog_action_clear) { dialog, _ ->
                    val sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(requireContext())
                    sharedPreferences.edit {
                        clear()
                    }

                    this@DebugSettingsFragment.showToast(
                        "Successfully cleared app settings!",
                        Toast.LENGTH_SHORT
                    )
                    dialog.dismiss()
                }
                setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
            }.show()
            true
        }
    }

    private fun createSdkInfoDialog() = MaterialAlertDialogBuilder(requireContext()).apply {
        setTitle(R.string.debug_activity_device_sdk_info_dialog_title)
        setMessage(
            """
            Device SDK: ${Build.VERSION.SDK_INT} (${Build.VERSION.RELEASE_OR_CODENAME})
            Preview SDK: ${Build.VERSION.PREVIEW_SDK_INT}
            Build fingerprint: ${Build.FINGERPRINT.ifEmpty { "<Unset>" }}
            Model: ${Build.MODEL.ifEmpty { "<Unset>" }}
            Board: ${Build.BOARD.ifEmpty { "<Unset>" }}
            Bootloader version: ${Build.BOOTLOADER.ifEmpty { "<Unset>" }}
            Brand: ${Build.BRAND.ifEmpty { "<Unset>" }}
            Device: ${Build.DEVICE.ifEmpty { "<Unset>" }}
            Display: ${Build.DISPLAY.ifEmpty { "<Unset>" }}
            Fingerprint: ${Build.FINGERPRINT.ifEmpty { "<Unset>" }}
            Hardware: ${Build.HARDWARE.ifEmpty { "<Unset>" }}
            Host: ${Build.HOST.ifEmpty { "<Unset>" }}
            Id: ${Build.ID.ifEmpty { "<Unset>" }}
            Manufacturer: ${Build.MANUFACTURER.ifEmpty { "<Unset>" }}
            Product: ${Build.PRODUCT.ifEmpty { "<Unset>" }}
            Supported ABIs: ${Build.SUPPORTED_ABIS.joinToString()}
            Tags: ${Build.TAGS.ifEmpty { "<Unset>" }}
            Time: ${Build.TIME}
            Type: ${Build.TYPE.ifEmpty { "<Unset>" }}
            User: ${Build.USER}
            Radio firmware version: ${Build.getRadioVersion().ifEmpty { "<Unset>" }}
            """.trimIndent()
        )
        setPositiveButton(R.string.dialog_action_dismiss) { dialog, _ -> dialog.dismiss() }
    }

    @SuppressLint("SwitchIntDef")
    private fun createNightModeInfoDialog() = MaterialAlertDialogBuilder(requireContext()).apply {
        setTitle(R.string.debug_activity_night_mode_info_dialog_title)

        val defaultNightMode = AppCompatDelegate.getDefaultNightMode()

        @Suppress("DEPRECATION")
        val nightModeMap = mapOf(
            AppCompatDelegate.MODE_NIGHT_AUTO to "Auto (Light after sunrise, dark after sunset)",
            AppCompatDelegate.MODE_NIGHT_NO to "Disabled",
            AppCompatDelegate.MODE_NIGHT_YES to "Enabled",
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM to "Using system to detect",
            AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY to "Enabled with battery saver"
        )

        val isLocationPermGranted =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        setMessage(
            """
            Default night mode: ${nightModeMap[defaultNightMode] ?: "Unknown"}
            Is location permission granted: ${isLocationPermGranted == PackageManager.PERMISSION_GRANTED}
            """.trimIndent()
        )
        setPositiveButton(R.string.dialog_action_dismiss) { dialog, _ -> dialog.dismiss() }
    }

    private fun createDynamicThemeInfoDialog() =
        MaterialAlertDialogBuilder(requireContext()).apply {
            val fragContext = this@DebugSettingsFragment.requireContext()

            setTitle(R.string.debug_activity_dynamic_theme_info_dialog_title)
            setMessage(
                """
                Is dynamic colour supported? $isDynamicColorAvailable
                Is dynamic colour preference checked? ${fragContext.prefDynamicTheme}
            """.trimIndent()
            )
            setPositiveButton(R.string.dialog_action_dismiss) { dialog, _ -> dialog.dismiss() }
            setNeutralButton(
                R.string.debug_activity_dynamic_theme_info_dialog_toggle_btn
            ) { _, _ ->
                fragContext.apply {
                    prefDynamicTheme = !prefDynamicTheme
                    applyDynamicTheme()
                }
                activity?.recreate()
            }
        }

    private fun createConnectivityInfoDialog() =
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(R.string.debug_activity_connectivity_info_dialog_title)
            val isNetworkMetered = connectivityManager!!.isActiveNetworkMetered
            val isNetworkActive = connectivityManager!!.isDefaultNetworkActive
            val isNetworkPermGranted =
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_NETWORK_STATE
                )
            setMessage(
                """
                Is network metered: $isNetworkMetered
                Is network active: $isNetworkActive
                Is network permission granted: ${isNetworkPermGranted == PackageManager.PERMISSION_GRANTED}
            """.trimIndent()
            )
            setPositiveButton(R.string.dialog_action_dismiss) { dialog, _ -> dialog.dismiss() }
        }

    private fun showDeviceInfoDialog() {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(R.string.debug_activity_device_info_title)
            setItems(R.array.debug_activity_device_info_array) { dialog, which ->
                when (which) {
                    0 -> createSdkInfoDialog().show()
                    1 -> createConnectivityInfoDialog().show()
                    2 -> createNightModeInfoDialog().show()
                    3 -> createDynamicThemeInfoDialog().show()
                    else -> Log.w(TAG, "Unknown item clicked! Index was at $which")
                }
                dialog.dismiss()
            }
            setPositiveButton(R.string.dialog_action_dismiss) { dialog, _ -> dialog.dismiss() }
        }.show()
    }
}
