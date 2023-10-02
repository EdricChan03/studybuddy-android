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
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.content.getSystemService
import androidx.core.text.isDigitsOnly
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.SwitchPreferenceCompat
import androidx.work.Operation
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.constants.sharedprefs.DevModePrefConstants
import com.edricchan.studybuddy.constants.sharedprefs.UpdateInfoPrefConstants
import com.edricchan.studybuddy.databinding.DebugSendFcmNotificationDialogBinding
import com.edricchan.studybuddy.exts.android.getSerializableCompat
import com.edricchan.studybuddy.exts.android.showToast
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.exts.datetime.formatISO
import com.edricchan.studybuddy.exts.firebase.auth.creationInstant
import com.edricchan.studybuddy.exts.firebase.auth.lastSignInInstant
import com.edricchan.studybuddy.exts.material.dialog.materialAlertDialogBuilder
import com.edricchan.studybuddy.exts.material.dialog.showMaterialAlertDialog
import com.edricchan.studybuddy.exts.material.textfield.editTextStrValue
import com.edricchan.studybuddy.interfaces.NotificationAction
import com.edricchan.studybuddy.interfaces.NotificationRequest
import com.edricchan.studybuddy.ui.modules.debug.DebugModalBottomSheetActivity
import com.edricchan.studybuddy.ui.modules.settings.fragment.featureflags.FeatureFlagsSettingsFragment
import com.edricchan.studybuddy.ui.preference.MaterialPreferenceFragment
import com.edricchan.studybuddy.ui.theming.applyDynamicTheme
import com.edricchan.studybuddy.ui.theming.isDynamicColorAvailable
import com.edricchan.studybuddy.ui.theming.prefDynamicTheme
import com.edricchan.studybuddy.utils.enqueueUniqueCheckForUpdatesWorker
import com.edricchan.studybuddy.utils.firebase.sendToFirestoreAsync
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.installations.ktx.installations
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.Instant


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

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
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

            savedInstanceState?.run {
                lastCheckedForUpdatesInstant = savedInstanceState.getSerializableCompat(
                    LAST_CHECKED_FOR_UPDATES_DATE_TAG
                )
                lastUpdatedInstant = savedInstanceState.getSerializableCompat(
                    LAST_UPDATED_DATE_TAG
                )
            } ?: run {
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
                    requireContext().showMaterialAlertDialog {
                        setTitle(R.string.debug_activity_updates_clear_last_checked_for_updates_date_confirm_dialog_title)
                        setNegativeButton(R.string.dialog_action_cancel, null)
                        setPositiveButton(R.string.dialog_action_clear) { dialogInterface, _ ->
                            updateInfoPreferences.edit {
                                remove(UpdateInfoPrefConstants.PREF_LAST_CHECKED_FOR_UPDATES_DATE)
                            }
                            dialogInterface.dismiss()
                        }
                    }
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
                    requireContext().showMaterialAlertDialog {
                        setTitle(R.string.debug_activity_updates_clear_last_updated_date_confirm_dialog_title)
                        setNegativeButton(R.string.dialog_action_cancel, null)
                        setPositiveButton(R.string.dialog_action_clear) { dialogInterface, _ ->
                            updateInfoPreferences.edit {
                                remove(UpdateInfoPrefConstants.PREF_LAST_UPDATED_DATE)
                            }
                            dialogInterface.dismiss()
                        }
                    }
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
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var installations: FirebaseInstallations
    private lateinit var devModeOpts: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        user = auth.currentUser
        firestore = Firebase.firestore
        installations = Firebase.installations
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
                    requireContext().showMaterialAlertDialog {
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
                    }
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
            showAcctInfoDialog()
            true
        }

        findPreference<Preference>(Constants.debugSendNotification)?.setOnPreferenceClickListener {
            showNotifRequestDialog()
            true
        }

        findPreference<Preference>(Constants.debugResetInstanceId)?.setOnPreferenceClickListener {
            showResetInstanceIdDialog()
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
            showClearAppSettingsDialog()
            true
        }
    }

    private fun showAcctInfoDialog() {
        requireContext().showMaterialAlertDialog {
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
        }
    }

    private fun showNotifRequestDialog() {
        val dialogBinding =
            DebugSendFcmNotificationDialogBinding.inflate(layoutInflater, null, false)

        val dialog = requireContext().showMaterialAlertDialog {
            setTitle(R.string.debug_activity_send_notification_title)
            setView(dialogBinding.root)
            setIcon(R.drawable.ic_send_outline_24dp)
            setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
            setPositiveButton(R.string.dialog_action_send) { _, _ ->
                with(dialogBinding) {
                    if (userOrTopicTextInputLayout.editTextStrValue.isNotEmpty() &&
                        titleTextInputLayout.editTextStrValue.isNotEmpty() &&
                        bodyTextInputLayout.editTextStrValue.isNotEmpty()
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
                            if (bodyTextInputLayout.editTextStrValue.isNotEmpty()) {
                                notificationBody = bodyTextInputLayout.editTextStrValue
                            }
                            if (channelIdTextInputLayout.editTextStrValue.isNotEmpty()
                            ) {
                                notificationChannelId =
                                    channelIdTextInputLayout.editTextStrValue
                            }
                            if (colorTextInputLayout.editTextStrValue.isNotEmpty()) {
                                notificationColor = colorTextInputLayout.editTextStrValue
                            }
                            if (priority.isNotEmpty()) {
                                notificationPriority = priority
                            }
                            if (titleTextInputLayout.editTextStrValue.isNotEmpty()) {
                                notificationTitle = titleTextInputLayout.editTextStrValue
                            }
                            if (userOrTopicTextInputLayout.editTextStrValue.isNotEmpty()
                            ) {
                                userOrTopic = userOrTopicTextInputLayout.editTextStrValue
                            }
                            if (ttlTextInputLayout.editTextStrValue.isNotEmpty() && ttlTextInputLayout.editTextStrValue.isDigitsOnly()
                            ) {
                                notificationTtl = ttlTextInputLayout.editTextStrValue.toLong()
                            }
                            val notificationSettingsAction = NotificationAction.build {
                                title = "Configure Notifications"
                                icon = "ic_settings_24dp"
                                type = Constants.actionNotificationsSettingsIntent
                            }
                            notificationActions?.add(notificationSettingsAction)
                        }
                        sendNotificationRequest(notificationRequest)
                    } else {
                        this@DebugSettingsFragment.showToast(
                            "Please fill in the form!",
                            Toast.LENGTH_SHORT
                        )
                    }
                }
            }
        }

        // Initially disable the button
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = false
        with(dialogBinding) {
            fun textWatcher(textInputLayout: TextInputLayout) = { e: Editable? ->
                textInputLayout.error =
                    if (e.isNullOrEmpty()) "This field is required" else null
                // Update confirm button state
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled =
                    userOrTopicTextInputLayout.editTextStrValue.isNotEmpty() &&
                        titleTextInputLayout.editTextStrValue.isNotEmpty() &&
                        bodyTextInputLayout.editTextStrValue.isNotEmpty()
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
    }

    private fun sendNotificationRequest(request: NotificationRequest) {
        try {
            lifecycleScope.launch {
                request.sendToFirestoreAsync(firestore)
            }
            Log.d(
                TAG,
                "Successfully sent notification request to Cloud Firestore!"
            )
            this@DebugSettingsFragment.showToast(
                "Successfully sent notification request to Cloud Firestore!",
                Toast.LENGTH_SHORT
            )
        } catch (e: Exception) {
            this@DebugSettingsFragment.showToast(
                "An error occurred while attempting to send the notification request" +
                    " to Cloud Firestore. Check the logcat for more details.",
                Toast.LENGTH_SHORT
            )
            Log.e(
                TAG,
                "An error occurred while attempting to send the notification request to Cloud Firestore:",
                e
            )
        }
    }

    private fun showResetInstanceIdDialog() {
        requireContext().showMaterialAlertDialog {
            setTitle(R.string.debug_activity_confirm_reset_instance_id_dialog_title)
            setMessage(R.string.debug_activity_confirm_reset_instance_id_dialog_msg)
            setPositiveButton(R.string.dialog_action_ok) { dialog, _ ->
                deleteInstanceId()
                dialog.dismiss()
            }
            setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
        }.show()
    }

    private fun deleteInstanceId() {
        lifecycleScope.launch {
            try {
                installations.delete().await()
                this@DebugSettingsFragment.showToast(
                    "Successfully deleted instance ID!",
                    Toast.LENGTH_LONG
                )
            } catch (e: Exception) {
                this@DebugSettingsFragment.showToast(
                    "An error occurred while deleting the device's instance ID." +
                        " Please consult the logcat for the stacktrace of the exception.",
                    Toast.LENGTH_LONG
                )
                Log.e(
                    TAG,
                    "An error occurred while deleting the device's instance ID: ",
                    e
                )

            }
        }
    }

    private fun showClearAppSettingsDialog() {
        requireContext().showMaterialAlertDialog {
            setTitle(R.string.debug_activity_confirm_clear_app_settings_dialog_title)
            setMessage(R.string.debug_activity_confirm_clear_app_settings_dialog_msg)
            setPositiveButton(R.string.dialog_action_clear) { dialog, _ ->
                val sharedPreferences = requireContext().defaultSharedPreferences
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
        }
    }

    private val String.orUnset get() = ifEmpty { "<Unset>" }

    @ChecksSdkIntAtLeast(parameter = 0, lambda = 1)
    private inline fun fromApi(minVersion: Int, action: () -> Any) =
        if (Build.VERSION.SDK_INT >= minVersion) action().toString() else "<Not available>"

    private fun createSdkInfoDialog() = requireContext().materialAlertDialogBuilder {
        setTitle(R.string.debug_activity_device_sdk_info_dialog_title)
        setMessage(
            """
            Device SDK: ${Build.VERSION.SDK_INT} (${
                fromApi(Build.VERSION_CODES.R) { Build.VERSION.RELEASE_OR_CODENAME }
            })
            Preview SDK: ${
                fromApi(Build.VERSION_CODES.M) {
                    Build.VERSION.PREVIEW_SDK_INT
                }
            }
            Build fingerprint: ${Build.FINGERPRINT.orUnset}
            Model: ${Build.MODEL.orUnset}
            Board: ${Build.BOARD.orUnset}
            Bootloader version: ${Build.BOOTLOADER.orUnset}
            Brand: ${Build.BRAND.orUnset}
            Device: ${Build.DEVICE.orUnset}
            Display: ${Build.DISPLAY.orUnset}
            Fingerprint: ${Build.FINGERPRINT.orUnset}
            Hardware: ${Build.HARDWARE.orUnset}
            Host: ${Build.HOST.orUnset}
            Id: ${Build.ID.orUnset}
            Manufacturer: ${Build.MANUFACTURER.orUnset}
            Product: ${Build.PRODUCT.orUnset}
            Supported ABIs: ${Build.SUPPORTED_ABIS.joinToString()}
            Tags: ${Build.TAGS.orUnset}
            Time: ${Build.TIME}
            Type: ${Build.TYPE.orUnset}
            User: ${Build.USER}
            Radio firmware version: ${Build.getRadioVersion().orUnset}
            """.trimIndent()
        )
        setPositiveButton(R.string.dialog_action_dismiss) { dialog, _ -> dialog.dismiss() }
    }

    @SuppressLint("SwitchIntDef")
    private fun createNightModeInfoDialog() = requireContext().materialAlertDialogBuilder {
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
        requireContext().materialAlertDialogBuilder {
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
        requireContext().materialAlertDialogBuilder {
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
        requireContext().showMaterialAlertDialog {
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
        }
    }
}
