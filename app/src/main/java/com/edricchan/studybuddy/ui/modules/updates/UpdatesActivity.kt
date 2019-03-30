package com.edricchan.studybuddy.ui.modules.updates

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.ContentView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.utils.SharedUtils
import com.github.javiersantos.appupdater.AppUpdaterUtils
import com.github.javiersantos.appupdater.enums.AppUpdaterError
import com.github.javiersantos.appupdater.enums.UpdateFrom
import com.github.javiersantos.appupdater.objects.Update
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.util.*

@ContentView(R.layout.activity_updates)
class UpdatesActivity : AppCompatActivity() {

	private var isChecking = false
	// The tag used for Log.* calls
	private val TAG = SharedUtils.getTag(this::class.java)
	private lateinit var appUpdate: Update
	private lateinit var checkForUpdatesBtn: Button
	private lateinit var emptyStateLayout: RelativeLayout
	private lateinit var preferences: SharedPreferences

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		checkForUpdatesBtn = findViewById(R.id.empty_state_view_cta)
		checkForUpdatesBtn.setOnClickListener { click -> checkForUpdates() }
		emptyStateLayout = findViewById(R.id.updates_empty_state_view)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		preferences = PreferenceManager.getDefaultSharedPreferences(this)
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.menu_updates, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId) {
			android.R.id.home -> {
				onBackPressed()
				true
			}
			R.id.action_check_for_updates -> {
				Snackbar.make(findViewById<View>(R.id.updatesView), R.string.update_snackbar_checking, Snackbar.LENGTH_SHORT)
				Log.d(TAG, "Check for updates clicked!")
						.show()
				checkForUpdates()
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}

	override fun onPrepareOptionsMenu(menu: Menu): Boolean {
		// Disable the menu item when the user has pressed it
		menu.findItem(R.id.action_check_for_updates).isEnabled = !isChecking
		return true
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
		if (requestCode == 0) {
			showUpdateDialog()
		}
	}

	private fun downloadUpdate(downloadUrl: String, version: String, mobileDataSkip: Boolean = false, downloadAgain: Boolean = false) {
		val fileName = "com.edricchan.studybuddy-$version.apk"
		if (downloadAgain || !File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + fileName).exists()) {
			if (mobileDataSkip || SharedUtils.isCellularNetworkAvailable(this)) {
				val builder = MaterialAlertDialogBuilder(this)
				builder.setTitle(R.string.update_activity_cannot_download_cellular_dialog_title)
				builder.setMessage(R.string.update_activity_cannot_download_cellular_dialog_msg)
				builder.setNegativeButton(R.string.dialog_action_cancel) { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
				builder.setPositiveButton(R.string.update_activity_cannot_download_cellular_dialog_positive_btn) { dialogInterface, i ->
					dialogInterface.dismiss()
					downloadUpdate(downloadUrl, version, true, false)
				}
				builder.show()
			} else {
				val request1 = DownloadManager.Request(Uri.parse(downloadUrl))

				request1.setAllowedOverRoaming(false)
				request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
				request1.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
				request1.setMimeType("application/vnd.android.package-archive")
				val manager1 = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
				Objects.requireNonNull(manager1).enqueue(request1)
				if (DownloadManager.STATUS_SUCCESSFUL == 8) {
					Toast.makeText(applicationContext, "Download complete!", Toast.LENGTH_SHORT).show()
					installUpdate(fileName)
				}
			}
		} else {
			val builder = MaterialAlertDialogBuilder(this)
			builder.setTitle(R.string.update_activity_update_already_downloaded_dialog_title)
			builder.setMessage(R.string.update_activity_update_already_downloaded_dialog_msg)
			builder.setPositiveButton(R.string.update_activity_update_already_downloaded_dialog_positive_btn) { dialogInterface: DialogInterface, i: Int ->
				installUpdate(fileName)
				dialogInterface.dismiss()
			}
			builder.setNegativeButton(R.string.update_activity_update_already_downloaded_dialog_negative_btn) { dialogInterface: DialogInterface, i: Int ->
				if (File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + fileName).delete()) {
					Toast.makeText(this@UpdatesActivity, R.string.update_activity_delete_update_success_toast, Toast.LENGTH_LONG).show()
					downloadUpdate(downloadUrl, version, false, true)
				} else {
					Toast.makeText(this@UpdatesActivity, R.string.update_activity_cannot_delete_update_toast, Toast.LENGTH_LONG).show()
				}
			}
			builder.show()
		}
	}

	private fun installUpdate(fileName: String) {
		// See https://android-developers.googleblog.com/2017/08/making-it-safer-to-get-apps-on-android-o.html
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

			if (this@UpdatesActivity.packageManager.canRequestPackageInstalls()) {
				val installIntent = Intent(Intent.ACTION_VIEW)
				installIntent.setDataAndType(FileProvider.getUriForFile(applicationContext, "com.edricchan.studybuddy.provider", File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + fileName)), "application/vnd.android.package-archive")
				installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
				// Allow reading the file
				installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
				startActivity(installIntent)
			} else {
				val builder = MaterialAlertDialogBuilder(this)
				builder.setMessage(R.string.update_activity_enable_unknown_sources_dialog_msg)
						.setNegativeButton(R.string.dialog_action_cancel) { dialog, which -> dialog.dismiss() }
						.setNeutralButton(R.string.dialog_action_retry) { dialog, which ->
							dialog.dismiss()
							installUpdate(fileName)
						}
						.setPositiveButton(R.string.update_activity_enable_unknown_sources_dialog_positive_btn) { dialog, which ->
							val allowUnknownAppsIntent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
							allowUnknownAppsIntent.data = Uri.parse("package:$packageName")
							startActivity(allowUnknownAppsIntent)
						}
						.setCancelable(false)
						.show()
			}
		} else {
			val installIntent = Intent(Intent.ACTION_VIEW)
			installIntent.setDataAndType(FileProvider.getUriForFile(applicationContext, "com.edricchan.studybuddy.provider", File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + fileName)), "application/vnd.android.package-archive")
			installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
			// Allow reading the file
			installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
			startActivity(installIntent)
		}
	}

	private fun showUpdateDialog() {
		// New update
		val builder = MaterialAlertDialogBuilder(this)
		builder.setTitle(getString(R.string.update_dialog_title_new, appUpdate.latestVersion))
		builder.setIcon(R.drawable.ic_system_update_24dp)
		builder.setMessage("What's new:\n" + appUpdate.releaseNotes)
		builder.setNegativeButton(android.R.string.cancel) { dialogInterface, i -> }
		builder.setPositiveButton(R.string.update_dialog_positive_btn_text) { dialogInterface, i ->
			// Check if the device is running Android Marshmallow or higher
			// Marshmallow introduces the capability for runtime permissions
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				if (SharedUtils.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, this@UpdatesActivity)) {
					downloadUpdate(appUpdate.urlToDownload.toString(), appUpdate.latestVersion)
				} else {
					if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
						val permBuilder = MaterialAlertDialogBuilder(this)
						permBuilder.setTitle(R.string.update_perm_rationale_dialog_title)
								.setMessage(R.string.update_perm_rationale_dialog_msg)
								.setNegativeButton(R.string.update_perm_rationale_dialog_deny) { dialog, which -> dialog.dismiss() }
								.setPositiveButton(R.string.update_perm_rationale_dialog_grant) { dialog, which -> requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0) }
								.show()
					} else {
						requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
					}
				}
			} else {
				downloadUpdate(appUpdate.urlToDownload.toString(), appUpdate.latestVersion)
			}
		}
		builder.setOnDismissListener { dialogInterface ->
			isChecking = false
			invalidateOptionsMenu()
		}
		builder.setOnCancelListener { dialogInterface ->
			isChecking = false
			invalidateOptionsMenu()
		}
		builder.show()
	}

	private fun checkForUpdates() {
		isChecking = true
		invalidateOptionsMenu()
		val appUpdaterUtils = AppUpdaterUtils(this)
				.setUpdateFrom(UpdateFrom.JSON)
				.setUpdateJSON(getString(R.string.testing_changelog_url))
				.withListener(object : AppUpdaterUtils.UpdateListener {
					override fun onSuccess(update: Update, updateAvailable: Boolean?) {
						appUpdate = update
						if (update.latestVersionCode == BuildConfig.VERSION_CODE && !updateAvailable!!) {
							// User is running latest version
							Snackbar.make(findViewById<View>(R.id.updatesView), R.string.update_snackbar_latest, Snackbar.LENGTH_SHORT)
									.show()
						} else {
							showUpdateDialog()
						}
					}

					override fun onFailed(appUpdaterError: AppUpdaterError) {
						isChecking = false
						invalidateOptionsMenu()
						when (appUpdaterError) {
							AppUpdaterError.NETWORK_NOT_AVAILABLE -> Snackbar.make(findViewById<View>(R.id.updatesView), R.string.update_snackbar_error_no_internet, Snackbar.LENGTH_LONG)
									.setAction(R.string.dialog_action_retry) { checkForUpdates() }
									.show()
							AppUpdaterError.JSON_ERROR -> Snackbar.make(findViewById<View>(R.id.updatesView), R.string.update_snackbar_error_malformed, Snackbar.LENGTH_LONG)
									.setAction(R.string.dialog_action_retry) { checkForUpdates() }
									.show()
							else -> Snackbar.make(findViewById<View>(R.id.updatesView), R.string.update_snackbar_error, Snackbar.LENGTH_LONG)
									.setAction(R.string.dialog_action_retry) { checkForUpdates() }
									.show()
						}
					}
				})
		appUpdaterUtils.start()
	}
}
