package com.edricchan.studybuddy.providers.fcm

import android.app.PendingIntent
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import coil.imageLoader
import coil.request.ImageRequest
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.core.compat.navigation.UriSettings
import com.edricchan.studybuddy.core.resources.notification.AppNotificationChannel
import com.edricchan.studybuddy.exts.android.buildIntent
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.interfaces.NotificationAction
import com.edricchan.studybuddy.ui.modules.main.MainActivity
import com.edricchan.studybuddy.ui.theming.dynamicColorPrimary
import com.edricchan.studybuddy.utils.NotificationUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.serialization.json.Json

class StudyBuddyMessagingService : FirebaseMessagingService() {
    private val notificationUtils = NotificationUtils.getInstance()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val manager = NotificationManagerCompat.from(this)
        val builder = NotificationCompat.Builder(
            this,
            AppNotificationChannel.Uncategorized.channelId
        )

        if (remoteMessage.notification != null) {
            if (remoteMessage.notification?.title != null) {
                builder.setContentTitle(remoteMessage.notification?.title)
                builder.setStyle(
                    NotificationCompat.BigTextStyle().bigText(remoteMessage.notification?.body)
                )
            }

            if (remoteMessage.notification?.body != null) {
                builder.setContentText(remoteMessage.notification?.body)
            }

            if (remoteMessage.notification?.icon != null) {
                val icon = resources.getIdentifier(
                    remoteMessage.notification?.icon,
                    "drawable",
                    packageName
                )
                // getIdentifier returns Resources.ID_NULL (0) if not such identifier exists
                if (icon != Resources.ID_NULL) {
                    builder.setSmallIcon(icon)
                } else {
                    // Use the default icon
                    builder.setSmallIcon(R.drawable.ic_notification_studybuddy_pencil_24dp)
                }
            } else {
                // Use the default icon
                builder.setSmallIcon(R.drawable.ic_notification_studybuddy_pencil_24dp)
            }

            builder.color = if (remoteMessage.notification?.color != null) {
                Color.parseColor(remoteMessage.notification?.color)
            } else {
                // Use the default color
                dynamicColorPrimary
            }

            // Image support was added in FCM 20.0.0
            if (remoteMessage.notification?.imageUrl != null) {
                val loader = applicationContext.imageLoader
                val req = ImageRequest.Builder(this)
                    .data(remoteMessage.notification?.imageUrl)
                    .target { result ->
                        val bitmap = (result as BitmapDrawable).bitmap
                        builder.setLargeIcon(bitmap)
                        builder.setStyle(
                            NotificationCompat.BigPictureStyle()
                                .bigPicture(bitmap)
                        )
                    }
                    .build()
                loader.enqueue(req)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (remoteMessage.notification?.channelId != null) {
                    if (manager.getNotificationChannel(remoteMessage.notification?.channelId!!) == null) {
                        Log.w(
                            TAG,
                            "No such notification channel ${remoteMessage.notification?.channelId} exists." +
                                "Assigning \"uncategorised\" channel."
                        )
                        builder.setChannelId(AppNotificationChannel.Uncategorized.channelId)
                    } else {
                        builder.setChannelId(remoteMessage.notification?.channelId!!)
                    }
                }
            }
        }

        remoteMessage.data["notificationActions"]?.let { actions ->
            Log.d(TAG, "notificationActions: $actions")
            val notificationActions: List<NotificationAction> = try {
                Json.decodeFromString(actions)
            } catch (e: Exception) {
                Log.e(TAG, "Could not parse notification actions:", e)
                Firebase.crashlytics.recordException(e)
                listOf()
            }

            for (notificationAction in notificationActions) {
                val icon = notificationAction.icon?.let {
                    resources.getIdentifier(
                        it,
                        "drawable",
                        packageName
                    )
                } ?: ResourcesCompat.ID_NULL

                val pIntent = when (notificationAction.type) {
                    // TODO: Don't hardcode action types
                    Constants.actionNotificationsSettingsIntent -> {
                        TaskStackBuilder.create(this).run {
                            addNextIntentWithParentStack(buildIntent<MainActivity>(this@StudyBuddyMessagingService) {
                                action = Intent.ACTION_VIEW
                                data = UriSettings.toUri()
                            })
                            getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                            )
                        }
                    }

                    else -> {
                        Log.w(
                            TAG,
                            "Unknown action type ${notificationAction.type} specified for $notificationAction."
                        )
                        null
                    }
                }
                builder.addAction(
                    NotificationCompat.Action(
                        icon,
                        notificationAction.title,
                        pIntent
                    )
                )
            }
        }

        builder.setContentIntent(
            PendingIntent.getActivity(
                this, 0, packageManager.getLaunchIntentForPackage(packageName),
                PendingIntent.FLAG_IMMUTABLE
            )
        )

        manager.notify(notificationUtils.incrementAndGetId(), builder.build())
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // Add token to Firebase Firestore in the user's document
        val fs = Firebase.firestore
        val auth = Firebase.auth
        if (auth.currentUser != null) {
            fs.document("users/${auth.currentUser?.uid}")
                .update("registrationToken", token)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Successfully updated token!")
                    } else {
                        Log.e(
                            TAG,
                            "An error occurred while attempting to update the token:",
                            task.exception
                        )
                    }
                }
        } else {
            Log.w(TAG, "There's currently no logged in user! Skipping document update.")
        }
    }
}
