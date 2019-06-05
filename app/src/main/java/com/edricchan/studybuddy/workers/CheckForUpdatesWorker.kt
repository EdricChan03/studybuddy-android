package com.edricchan.studybuddy.workers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.extensions.workmanager.dataOf
import com.edricchan.studybuddy.receivers.ActionButtonReceiver
import com.edricchan.studybuddy.utils.SharedUtils
import com.github.javiersantos.appupdater.AppUpdaterUtils
import com.github.javiersantos.appupdater.enums.AppUpdaterError
import com.github.javiersantos.appupdater.enums.UpdateFrom
import com.github.javiersantos.appupdater.objects.Update

class CheckForUpdatesWorker(
		private val appContext: Context,
		workerParams: WorkerParameters
) : Worker(appContext, workerParams) {
	private val TAG = SharedUtils.getTag(this::class.java)
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
			result = Result.failure(dataOf("errorType" to errorName))
		}

	}
	private val appUpdaterUtils = SharedUtils.getUpdateJsonUrl(appContext)?.let { jsonUrl ->
		AppUpdaterUtils(appContext).apply {
			setUpdateFrom(UpdateFrom.JSON)
			setUpdateJSON(jsonUrl)
			withListener(appUpdateListener)
		}
	}

	private val notificationManager = NotificationManagerCompat.from(appContext)
	private var notificationBuilder: NotificationCompat.Builder? = null

	override fun doWork(): Result {
		showInProgressNotification()
		appUpdaterUtils?.start()
		return result
	}

	private fun showInProgressNotification() {
		notificationBuilder = NotificationCompat.Builder(
				appContext,
				appContext.getString(R.string.notification_channel_update_status_id)
		).apply {
			setSmallIcon(R.drawable.ic_notification_system_update_24dp)
			setContentTitle(appContext.getString(R.string.notification_check_update))
			priority = NotificationCompat.PRIORITY_DEFAULT
			setProgress(100, 0, true)
			color = ContextCompat.getColor(appContext, R.color.colorPrimary)
			setOngoing(true)
		}
		notificationBuilder?.build()?.let { notificationManager.notify(Constants.notificationCheckForUpdatesId, it) }
	}

	private fun showNoUpdatesNotification() {
		notificationBuilder?.apply {
			setContentTitle(appContext.getString(R.string.notification_no_updates))
			setProgress(0, 0, false)
			setOngoing(false)
		}
		notificationBuilder?.build()?.let { notificationManager.notify(Constants.notificationCheckForUpdatesId, it) }
	}

	private fun showUpdateAvailableNotification(update: Update?) {
		// New update
		val intentAction = Intent(appContext, ActionButtonReceiver::class.java)

		intentAction.putExtra("action", Constants.actionNotificationsStartDownloadReceiver)
		intentAction.putExtra("downloadUrl", update?.urlToDownload.toString())
		intentAction.putExtra("version", update?.latestVersion)
		val pIntentDownload = PendingIntent.getBroadcast(appContext, 1, intentAction, PendingIntent.FLAG_UPDATE_CURRENT)
		notificationBuilder?.apply {
			setContentTitle(appContext.getString(R.string.notification_new_update_title))
			setContentText(appContext.getString(R.string.notification_new_update_text, update?.latestVersion))
			setProgress(0, 0, false)
			setOngoing(false)
			setChannelId(appContext.getString(R.string.notification_channel_update_available_id))
			addAction(NotificationCompat.Action(R.drawable.ic_download_24dp, "Download", pIntentDownload))
		}
		notificationBuilder?.build()?.let { notificationManager.notify(Constants.notificationCheckForUpdatesId, it) }
	}


}
