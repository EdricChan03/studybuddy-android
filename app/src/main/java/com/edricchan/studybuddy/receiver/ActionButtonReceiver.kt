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
import com.edricchan.studybuddy.utils.SharedUtils
import com.edricchan.studybuddy.utils.DataUtil

class ActionButtonReceiver : BroadcastReceiver() {
	override fun onReceive(context: Context, intent: Intent) {
		val TAG = SharedUtils.getTag(this::class.java)
		val action = intent.getStringExtra("action")
		when (action) {
			DataUtil.actionNotificationsStartDownloadReceiver -> checkPermission(context, intent)
			DataUtil.actionNotificationsRetryCheckForUpdateReceiver -> SharedUtils.checkForUpdates(context)
		}// Register receiver for when .apk download is compete
	}

	fun checkPermission(context: Context, intent: Intent) {
		if (SharedUtils.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, context)) {
			downloadUpdate(context, intent)
		}
	}

	fun downloadUpdate(context: Context, intent: Intent) {
		val request = DownloadManager.Request(Uri.parse(intent.getStringExtra("downloadUrl")))
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
		request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, context.getString(R.string.download_apk_name, intent.getStringExtra("version")))
		val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
		val id = manager.enqueue(request)
		//set BroadcastReceiver to install app when .apk is downloaded
		val onComplete = object : BroadcastReceiver() {
			override fun onReceive(context1: Context, intent: Intent) {
				val install = Intent(Intent.ACTION_VIEW)
				install.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
				install.setDataAndType(manager.getUriForDownloadedFile(id), "application/vnd.android.package-archive")
				context.startActivity(install)

				context.unregisterReceiver(this)
			}
		}
		context.applicationContext.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

	}
}
