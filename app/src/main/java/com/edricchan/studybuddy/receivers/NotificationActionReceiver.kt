package com.edricchan.studybuddy.receivers

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.constants.MimeTypeConstants
import com.edricchan.studybuddy.exts.android.buildIntent
import com.edricchan.studybuddy.exts.android.perms.checkPermissionGranted
import com.edricchan.studybuddy.exts.common.TAG

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "Received broadcast with action ${intent.getStringExtra("action")}.")
        when (intent.getStringExtra("action")) {
            Constants.actionNotificationsStartDownloadReceiver -> checkPermission(context, intent)
        }
    }

    private fun checkPermission(context: Context, intent: Intent) {
        Log.d(TAG, "Checking for external storage permission...")

        if (context.checkPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Log.d(TAG, "Permission granted. Downloading update...")
            downloadUpdate(context, intent)
        }
    }

    private fun downloadUpdate(context: Context, intent: Intent) {
        val downloadUrl = intent.getStringExtra("downloadUrl")?.toUri() ?: return
        val request = DownloadManager.Request(downloadUrl)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            context.getString(R.string.download_apk_name, intent.getStringExtra("version"))
        )
        val manager = context.getSystemService<DownloadManager>()
        val id = manager?.enqueue(request)
        //set BroadcastReceiver to install app when .apk is downloaded
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(context1: Context, intent: Intent) {
                Log.d(TAG, "Successfully downloaded update. Installing...")
                context.startActivity(buildIntent {
                    action = Intent.ACTION_VIEW
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    setDataAndType(
                        id?.let { manager.getUriForDownloadedFile(it) },
                        MimeTypeConstants.appPackageArchiveMime
                    )
                })

                context.unregisterReceiver(this)
            }
        }
        ContextCompat.registerReceiver(
            context.applicationContext,
            onComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

    }
}
