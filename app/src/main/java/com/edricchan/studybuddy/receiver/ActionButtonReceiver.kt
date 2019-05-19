package com.edricchan.studybuddy.receiver

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.constants.MimeTypeConstants
import com.edricchan.studybuddy.extensions.buildIntent
import com.edricchan.studybuddy.utils.SharedUtils

class ActionButtonReceiver : BroadcastReceiver() {
	override fun onReceive(context: Context, intent: Intent) {
		when (intent.getStringExtra("action")) {
			Constants.actionNotificationsStartDownloadReceiver -> checkPermission(context, intent)
			Constants.actionNotificationsRetryCheckForUpdateReceiver -> SharedUtils.checkForUpdates(context)
		}// Register receiver for when .apk download is compete
	}

	private fun checkPermission(context: Context, intent: Intent) {
		if (SharedUtils.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, context)) {
			downloadUpdate(context, intent)
		}
	}

	private fun downloadUpdate(context: Context, intent: Intent) {
		val request = DownloadManager.Request(Uri.parse(intent.getStringExtra("downloadUrl")))
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
		request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, context.getString(R.string.download_apk_name, intent.getStringExtra("version")))
		val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
		val id = manager.enqueue(request)
		//set BroadcastReceiver to install app when .apk is downloaded
		val onComplete = object : BroadcastReceiver() {
			override fun onReceive(context1: Context, intent: Intent) {
				context.startActivity(buildIntent {
					action = Intent.ACTION_VIEW
					flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
					setDataAndType(manager.getUriForDownloadedFile(id), MimeTypeConstants.appPackageArchiveMime)
				})

				context.unregisterReceiver(this)
			}
		}
		context.applicationContext.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

	}
}
