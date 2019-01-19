package com.edricchan.studybuddy.providers.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.crashlytics.android.Crashlytics
import com.edricchan.studybuddy.MainActivity
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.SettingsActivity
import com.edricchan.studybuddy.utils.SharedUtils
import com.edricchan.studybuddy.interfaces.NotificationAction
import com.edricchan.studybuddy.utils.DataUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import java.util.*

class StudyBuddyMessagingService : FirebaseMessagingService() {
	private val sharedHelper = SharedUtils(this)
	// To be used for Android's Log
	private val TAG = SharedUtils.getTag(this::class.java)

	override fun onMessageReceived(remoteMessage: RemoteMessage?) {
		super.onMessageReceived(remoteMessage)
		if (remoteMessage != null) {
			val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
			val builder = NotificationCompat.Builder(this, getString(R.string.notification_channel_uncategorised_id))
			if (remoteMessage.notification != null) {
				// Check if remote message has a title
				if (remoteMessage.notification!!.title != null) {
					builder.setContentTitle(remoteMessage.notification!!.title)
					builder.setStyle(NotificationCompat.BigTextStyle().bigText(remoteMessage.notification!!.body))
				}
				// Check if the remote message has a message body
				if (remoteMessage.notification!!.body != null) {
					builder.setContentText(remoteMessage.notification!!.body)
				}
				// Check if icon property exists
				if (remoteMessage.notification!!.icon != null) {
					// Use the icon
					builder.setSmallIcon(resources.getIdentifier(remoteMessage.notification!!.icon, "drawable", packageName))
				} else {
					// Use the default icon
					builder.setSmallIcon(R.drawable.ic_notification_studybuddy_pencil_24dp)
				}
				// Check if color property exists
				if (remoteMessage.notification!!.color != null) {
					// Use the color
					builder.color = Color.parseColor(remoteMessage.notification!!.color)
				} else {
					// Use the default color
					builder.color = ContextCompat.getColor(this, R.color.colorPrimary)
				}
			}
			if (remoteMessage.data != null) {
				// Check if the data of the remote message has a notificationChannelId as well as if the device is Android Oreo and up
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
					/*
					Check if notificationChannelID exists
					Note that since the notification channel ID is already defined in the Builder above, there's no need for an else statement
					 */
					if (remoteMessage.data.containsKey("notificationChannelId")) {
						// Checks if the specified notification channel ID exists
						if (manager.getNotificationChannel(remoteMessage.data["notificationChannelId"]) == null) {
							// Create a notification channel
							val notificationChannel = NotificationChannel(remoteMessage.data["notificationChannelId"], "Notification Channel", NotificationManager.IMPORTANCE_DEFAULT)
							notificationChannel.description = "This notification channel was generated since the channel ID for the notification doesn't exist."
							manager.createNotificationChannel(notificationChannel)
						}
						// Set the channel ID
						builder.setChannelId(remoteMessage.data["notificationChannelId"]!!)
					}
				}
				// Check if the data of the remote message has a notificationActions
				if (remoteMessage.data.containsKey("notificationActions")) {
					Log.d(TAG, "notificationActions: " + remoteMessage.data["notificationActions"]!!)
					var notificationActions: List<NotificationAction> = ArrayList()
					try {
						val gson = Gson()
						val remoteNotificationActions = gson.fromJson(remoteMessage.data["notificationActions"], Array<NotificationAction>::class.java)
						notificationActions = Arrays.asList(*remoteNotificationActions)
						Log.d(TAG, "Size: " + remoteNotificationActions.size)
						Log.d(TAG, "JSON: " + remoteNotificationActions.toString())
						Log.d(TAG, "NotificationAction#actionTitle: " + remoteNotificationActions[0].actionTitle)
					} catch (e: Exception) {
						e.printStackTrace()
						Crashlytics.logException(e)
					}

					for (notificationAction in notificationActions) {
						var icon = 0
						// Initial intent
						val intent: Intent
						// Notification pending intent
						var notificationPendingIntent: PendingIntent? = null
						val drawableIcon = resources.getIdentifier(notificationAction.actionIcon, "drawable", packageName)
						// If getIdentifier is returned with 0, this means that no such drawable could be found
						if (drawableIcon != 0) {
							icon = drawableIcon
						}
						when (notificationAction.actionType) {
							DataUtils.actionNotificationsSettingsIntent -> {
								intent = Intent(this, SettingsActivity::class.java)
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
								notificationPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
							}
							else -> Log.w(TAG, "Unknown action type \"" + notificationAction.actionType + "\"!")
						}
						builder.addAction(NotificationCompat.Action(icon, notificationAction.actionTitle, notificationPendingIntent))
					}
				}
			}
			val mainActivityIntent = Intent(this, MainActivity::class.java)
			mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
			val mainPendingIntent = PendingIntent.getActivity(this, 0, mainActivityIntent, PendingIntent.FLAG_ONE_SHOT)
			builder.setContentIntent(mainPendingIntent)
			manager.notify(sharedHelper.getDynamicId(), builder.build())
		}
	}

	override fun onNewToken(token: String?) {
		Log.d(TAG, "Refreshed token: " + token!!)

		// Add token to Firebase Firestore in the user's document
		val fs = FirebaseFirestore.getInstance()
		val auth = FirebaseAuth.getInstance()
		if (auth.currentUser != null) {
			fs.document("users/" + auth.currentUser!!.uid)
					.update("registrationToken", token)
					.addOnCompleteListener { task ->
						if (task.isSuccessful) {
							Log.d(TAG, "Successfully updated token!")
						} else {
							Log.e(TAG, "An error occurred while attempting to update the token:", task.exception)
						}
					}
		} else {
			Log.w(TAG, "There's currently no logged in user! Skipping document update.")
		}
	}
}
