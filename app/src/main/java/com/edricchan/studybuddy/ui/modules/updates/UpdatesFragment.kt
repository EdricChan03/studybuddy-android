package com.edricchan.studybuddy.ui.modules.updates

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.result.registerForActivityResult
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.edit
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.constants.MimeTypeConstants
import com.edricchan.studybuddy.core.settings.updates.UpdateInfoPrefConstants
import com.edricchan.studybuddy.databinding.FragUpdatesBinding
import com.edricchan.studybuddy.exts.android.perms.checkPermissionGranted
import com.edricchan.studybuddy.exts.android.showToast
import com.edricchan.studybuddy.exts.android.startActivity
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.exts.material.dialog.showMaterialAlertDialog
import com.edricchan.studybuddy.ui.common.SnackBarData
import com.edricchan.studybuddy.ui.common.fragment.ViewBindingFragment
import com.edricchan.studybuddy.utils.androidx.core.menuProvider
import com.edricchan.studybuddy.utils.getUpdateJsonUrl
import com.edricchan.studybuddy.utils.network.isMeteredNetwork
import com.github.javiersantos.appupdater.AppUpdaterUtils
import com.github.javiersantos.appupdater.enums.AppUpdaterError
import com.github.javiersantos.appupdater.enums.UpdateFrom
import com.github.javiersantos.appupdater.objects.Update

class UpdatesFragment : ViewBindingFragment<FragUpdatesBinding>(FragUpdatesBinding::inflate) {
    // Whether the user has pressed the "check for updates" menu item
    private var isChecking = false
    private lateinit var appUpdate: Update
    private lateinit var preferences: SharedPreferences

    // SharedPreferences used for the storing of info on when the app was last updated
    private lateinit var updateInfoPreferences: SharedPreferences

    private val requestWriteExternalStoragePermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) { isGranted ->
            if (isGranted) showUpdateDialog()
            else Log.d(TAG, "User denied the permission")
        }

    private fun requestWriteExternalStoragePermission() {
        requestWriteExternalStoragePermissionLauncher.launch()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = requireContext().defaultSharedPreferences
        updateInfoPreferences =
            requireContext().getSharedPreferences(
                UpdateInfoPrefConstants.FILE_UPDATE_INFO,
                Context.MODE_PRIVATE
            )

        binding.emptyStateCtaBtn.setOnClickListener { checkForUpdates() }
    }

    override val menuProvider = menuProvider(
        menuResId = R.menu.menu_updates,
        onPrepareMenu = {
            it.findItem(R.id.action_check_for_updates).isEnabled = !isChecking
        }
    ) {
        when (it.itemId) {
            R.id.action_check_for_updates -> {
                Log.d(TAG, "Check for updates clicked!")
                showSnackBar(
                    R.string.update_snackbar_checking,
                    SnackBarData.Duration.Short
                )

                checkForUpdates()
                true
            }

            else -> false
        }
    }

    private fun getDownloadFile(fileName: String) =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .resolve(fileName)

    // TODO: Clean up code
    private fun downloadUpdate(
        downloadUrl: String,
        version: String,
        ignoreMobileDataSetting: Boolean = false,
        showMobileDataWarning: Boolean = true,
        downloadAgain: Boolean = false
    ) {
        // The file name that the downloaded APK will use
        val fileName = getString(R.string.download_apk_name, version)

        // Check if the user wants to redownload the APK file or if the user does not have the APK
        // already downloaded
        if (downloadAgain || !getDownloadFile(fileName).exists()) {
            // Check if the user has clicked on "download anyway" on the dialog that showed, or
            // check if the user is using a cellular network and has disabled downloading updates
            // over cellular
            if (requireContext().isMeteredNetwork &&
                (!ignoreMobileDataSetting && !preferences.getBoolean(
                    Constants.prefUpdatesDownloadOverMetered,
                    false
                )) && showMobileDataWarning
            ) {
                // Show a dialog warning the user that cellular network is on as the user has
                // disabled downloading updates over cellular
                requireContext().showMaterialAlertDialog {
                    setTitle(R.string.update_activity_cannot_download_cellular_dialog_title)
                    setMessage(R.string.update_activity_cannot_download_cellular_dialog_msg)
                    setNegativeButton(R.string.dialog_action_cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    setPositiveButton(R.string.update_activity_cannot_download_cellular_dialog_positive_btn) { dialog, _ ->
                        dialog.dismiss()
                        // Call the function again, but skip the mobile data warning
                        downloadUpdate(
                            downloadUrl,
                            version,
                            ignoreMobileDataSetting = true,
                            showMobileDataWarning = true,
                            downloadAgain = false
                        )
                    }
                }
            } else {
                // Construct a request to download from a URI
                val request = DownloadManager.Request(downloadUrl.toUri()).apply {
                    // Don't download over roaming
                    // TODO: Add setting for this
                    setAllowedOverRoaming(false)
                    setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                    // Set the file type so that the package installer can open the file
                    setMimeType(MimeTypeConstants.appPackageArchiveMime)
                }

                val manager = requireContext().getSystemService<DownloadManager>()
                manager?.enqueue(request)

                // Create a receiver to intercept when the download is complete
                val downloadCompleteReceiver = object : BroadcastReceiver() {
                    override fun onReceive(context: Context?, intent: Intent?) {
                        Log.d(TAG, "Download complete!")
                        context?.showToast("Download complete!", Toast.LENGTH_SHORT)
                        installUpdate(fileName)
                    }
                }

                // Lastly, register the receiver with the intents that it accepts
                ContextCompat.registerReceiver(
                    requireContext(),
                    downloadCompleteReceiver,
                    IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                    ContextCompat.RECEIVER_NOT_EXPORTED
                )
            }
        } else {
            requireContext().showMaterialAlertDialog {
                setTitle(R.string.update_activity_update_already_downloaded_dialog_title)
                setMessage(R.string.update_activity_update_already_downloaded_dialog_msg)
                setPositiveButton(R.string.update_activity_update_already_downloaded_dialog_positive_btn) { dialog, _ ->
                    installUpdate(fileName)
                    dialog.dismiss()
                }
                setNegativeButton(R.string.update_activity_update_already_downloaded_dialog_negative_btn) { _, _ ->
                    if (getDownloadFile(fileName).delete()) {
                        showToast(
                            R.string.update_activity_delete_update_success_toast,
                            Toast.LENGTH_LONG
                        )
                        downloadUpdate(
                            downloadUrl,
                            version,
                            ignoreMobileDataSetting = false,
                            showMobileDataWarning = true
                        )
                    } else {
                        showToast(
                            R.string.update_activity_cannot_delete_update_toast,
                            Toast.LENGTH_LONG
                        )
                    }
                }
            }
        }
    }

    private fun startInstallIntent(fileName: String) {
        requireContext().startActivity {
            action = Intent.ACTION_VIEW
            setDataAndType(
                FileProvider.getUriForFile(
                    requireContext(),
                    "${BuildConfig.APPLICATION_ID}.provider",
                    getDownloadFile(fileName)
                ),
                MimeTypeConstants.appPackageArchiveMime
            )
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        updateInfoPreferences.edit {
            putLong(
                UpdateInfoPrefConstants.PREF_LAST_UPDATED_DATE,
                System.currentTimeMillis()
            )
        }
    }

    private fun installUpdate(fileName: String) {
        // See https://android-developers.googleblog.com/2017/08/making-it-safer-to-get-apps-on-android-o.html
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O ||
            requireContext().packageManager.canRequestPackageInstalls()
        ) return startInstallIntent(fileName)

        // User has not allowed the app as an unknown app source
        requireContext().showMaterialAlertDialog {
            setMessage(R.string.update_activity_enable_unknown_sources_dialog_msg)
            setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
            setNeutralButton(R.string.dialog_action_retry) { dialog, _ ->
                dialog.dismiss()
                // Call this method again
                installUpdate(fileName)
            }
            setPositiveButton(R.string.update_activity_enable_unknown_sources_dialog_positive_btn) { _, _ ->
                // Create an intent to take the user to Settings for unknown app sources
                requireContext().startActivity(
                    Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                    "package:${requireContext().packageName}".toUri()
                )
            }
            // Mark it as non-dismissible
            setCancelable(false)
        }
    }

    private fun showUpdateDialog() {
        requireContext().showMaterialAlertDialog {
            setTitle(getString(R.string.update_dialog_title_new, appUpdate.latestVersion))
            setIcon(R.drawable.ic_system_update_24dp)
            setMessage("What's new:\n${appUpdate.releaseNotes}")
            setNegativeButton(android.R.string.cancel, null)
            setPositiveButton(R.string.update_dialog_positive_btn_text) { _, _ ->
                // Check if the write external storage permission has been granted
                if (requireContext().checkPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    return@setPositiveButton downloadUpdate(
                        appUpdate.urlToDownload.toString(),
                        appUpdate.latestVersion
                    )

                // User has pressed "Deny" to the permission prompt
                if (
                    shouldShowRequestPermissionRationale(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    requireContext().showMaterialAlertDialog {
                        setTitle(R.string.update_perm_rationale_dialog_title)
                        setMessage(R.string.update_perm_rationale_dialog_msg)
                        setNegativeButton(R.string.update_perm_rationale_dialog_deny) { dialog, _ ->
                            dialog.dismiss()
                        }
                        setPositiveButton(R.string.update_perm_rationale_dialog_grant) { _, _ ->
                            requestWriteExternalStoragePermission()
                        }
                    }
                } else {
                    // Request permissions for writing to the external storage
                    requestWriteExternalStoragePermission()
                }
            }
            setOnDismissListener {
                // Mark the check for updates menu item as selectable again
                isChecking = false
                requireActivity().invalidateMenu()
            }
            setOnCancelListener {
                // Mark the check for updates menu item as selectable again
                isChecking = false
                requireActivity().invalidateMenu()
            }
        }
    }

    private fun checkForUpdates() {
        // Save last updated status
        updateInfoPreferences.edit {
            Log.d(TAG, "Setting last checked for updates date...")
            putLong(
                UpdateInfoPrefConstants.PREF_LAST_CHECKED_FOR_UPDATES_DATE,
                System.currentTimeMillis()
            )
        }
        isChecking = true
        requireActivity().invalidateMenu()
        AppUpdaterUtils(requireContext())
            .setUpdateFrom(UpdateFrom.JSON)
            .setUpdateJSON(requireContext().getUpdateJsonUrl())
            .withListener(object : AppUpdaterUtils.UpdateListener {
                override fun onSuccess(update: Update, updateAvailable: Boolean?) {
                    appUpdate = update
                    if (update.latestVersionCode == BuildConfig.VERSION_CODE && !updateAvailable!!) {
                        // User is running latest version
                        showSnackBar(
                            R.string.update_snackbar_latest,
                            SnackBarData.Duration.Short
                        )
                    } else {
                        showUpdateDialog()
                    }
                }

                override fun onFailed(appUpdaterError: AppUpdaterError) {
                    isChecking = false
                    requireActivity().invalidateMenu()
                    val snackbarMsgRes = when (appUpdaterError) {
                        AppUpdaterError.NETWORK_NOT_AVAILABLE ->
                            R.string.update_snackbar_error_no_internet

                        AppUpdaterError.JSON_ERROR -> R.string.update_snackbar_error_malformed
                        else -> R.string.update_snackbar_error
                    }

                    showSnackBar(
                        snackbarMsgRes,
                        SnackBarData.Duration.Long,
                        SnackBarData.Action(
                            getString(R.string.dialog_action_retry),
                            ::checkForUpdates
                        )
                    )
                }
            })
            .start()
    }
}
