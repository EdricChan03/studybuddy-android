package com.edricchan.studybuddy.workers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.constants.sharedprefs.UpdateInfoPrefConstants
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.extensions.buildIntent
import com.edricchan.studybuddy.receivers.NotificationActionReceiver
import com.edricchan.studybuddy.ui.modules.updates.UpdatesActivity
import com.edricchan.studybuddy.utils.getUpdateJsonUrl
import com.github.javiersantos.appupdater.AppUpdaterUtils
import com.github.javiersantos.appupdater.enums.AppUpdaterError
import com.github.javiersantos.appupdater.enums.UpdateFrom
import com.github.javiersantos.appupdater.objects.Update
import com.google.android.material.color.MaterialColors

class CheckForUpdatesWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {
    private var result = Result.success()

    private val appUpdateListener = object : AppUpdaterUtils.UpdateListener {
        override fun onSuccess(update: Update?, updateAvailable: Boolean?) {
            if (update?.latestVersionCode == BuildConfig.VERSION_CODE && updateAvailable == false) {
                showNoUpdatesNotification()
            } else {
                showUpdateAvailableNotification(update)
            }
        }

        override fun onFailed(error: AppUpdaterError?) {
            // Note: AppUpdaterError is an enum object
            val errorName = error?.name
            result = Result.failure(workDataOf("errorType" to errorName))
        }

    }
    private val appUpdaterUtils = AppUpdaterUtils(appContext).apply {
        setUpdateFrom(UpdateFrom.JSON)
        setUpdateJSON(appContext.getUpdateJsonUrl())
        withListener(appUpdateListener)
    }

    private val notificationManager = NotificationManagerCompat.from(appContext)
    private var notificationBuilder: NotificationCompat.Builder? = null

    override fun doWork(): Result {
        updateLastCheckedStatus()
        showInProgressNotification()
        appUpdaterUtils.start()
        return result
    }

    private fun updateLastCheckedStatus() {
        Log.d(TAG, "Updating last checked status...")
        appContext.getSharedPreferences(
            UpdateInfoPrefConstants.FILE_UPDATE_INFO,
            Context.MODE_PRIVATE
        )
            .edit {
                putLong(
                    UpdateInfoPrefConstants.PREF_LAST_CHECKED_FOR_UPDATES_DATE,
                    System.currentTimeMillis()
                )
            }
    }

    private fun showInProgressNotification() {
        notificationBuilder = NotificationCompat.Builder(
            appContext,
            appContext.getString(R.string.notification_channel_update_status_id)
        ).apply {
            setSmallIcon(R.drawable.ic_notification_system_update_24dp)
            setContentTitle(appContext.getString(R.string.notification_check_update))
            setCategory(NotificationCompat.CATEGORY_PROGRESS)
            priority = NotificationCompat.PRIORITY_DEFAULT
            setProgress(100, 0, true)
            color = MaterialColors.getColor(
                appContext,
                R.attr.colorPrimary,
                ContextCompat.getColor(appContext, R.color.colorPrimary)
            )
            setOngoing(true)
        }
        notificationBuilder?.build()
            ?.let { notificationManager.notify(Constants.notificationCheckForUpdatesId, it) }
    }

    private fun showNoUpdatesNotification() {
        notificationBuilder?.apply {
            setContentTitle(appContext.getString(R.string.notification_no_updates))
            setCategory(null)
            setProgress(0, 0, false)
            setOngoing(false)
        }
        notificationBuilder?.build()
            ?.let { notificationManager.notify(Constants.notificationCheckForUpdatesId, it) }
    }

    private fun showUpdateAvailableNotification(update: Update?) {
        // New update
        val intentAction = Intent(appContext, NotificationActionReceiver::class.java)

        intentAction.putExtra("action", Constants.actionNotificationsStartDownloadReceiver)
        intentAction.putExtra("downloadUrl", update?.urlToDownload.toString())
        intentAction.putExtra("version", update?.latestVersion)
        val pIntentDownload = PendingIntent.getBroadcast(
            appContext,
            1,
            intentAction,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val contentIntent = buildIntent<UpdatesActivity>(appContext) {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pContentIntent = PendingIntent.getActivity(appContext, 0, contentIntent, 0)

        notificationBuilder?.apply {
            setContentTitle(appContext.getString(R.string.notification_new_update_title))
            setContentText(
                appContext.getString(
                    R.string.notification_new_update_text,
                    update?.latestVersion
                )
            )
            setContentIntent(pContentIntent)
            setProgress(0, 0, false)
            setCategory(null)
            setOngoing(false)
            setChannelId(appContext.getString(R.string.notification_channel_update_available_id))
            addAction(
                NotificationCompat.Action(
                    R.drawable.ic_download_24dp,
                    "Download",
                    pIntentDownload
                )
            )
        }
        notificationBuilder?.build()
            ?.let { notificationManager.notify(Constants.notificationCheckForUpdatesId, it) }
    }


}
