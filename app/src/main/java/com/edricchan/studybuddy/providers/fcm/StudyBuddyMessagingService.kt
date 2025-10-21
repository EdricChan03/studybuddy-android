package com.edricchan.studybuddy.providers.fcm

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.toColorInt
import androidx.core.net.toUri
import coil3.imageLoader
import coil3.request.ImageRequest
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.core.compat.navigation.UriSettings
import com.edricchan.studybuddy.core.resources.notification.AppNotificationChannel
import com.edricchan.studybuddy.exts.android.buildIntent
import com.edricchan.studybuddy.exts.android.createPendingIntent
import com.edricchan.studybuddy.exts.android.perms.checkPermissionGranted
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.interfaces.NotificationAction
import com.edricchan.studybuddy.ui.modules.main.MainActivity
import com.edricchan.studybuddy.ui.theming.dynamicColorPrimary
import com.edricchan.studybuddy.utils.NotificationUtils
import com.edricchan.studybuddy.utils.android.fromApi
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.firestore.firestore
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

        builder.setAutoCancel(true)

        remoteMessage.notification?.let { notification ->
            notification.title?.let { title ->
                builder.setContentTitle(title)
                builder.setStyle(
                    NotificationCompat.BigTextStyle().bigText(remoteMessage.notification?.body)
                )
            }
            notification.body?.let { body ->
                builder.setContentText(body)
            }

            builder.setSmallIcon(notification.icon?.let { icon ->
                val iconRes = resources.getIdentifier(
                    icon,
                    "drawable",
                    packageName
                )
                // getIdentifier returns Resources.ID_NULL (0) if not such identifier exists
                if (iconRes != Resources.ID_NULL) {
                    iconRes
                } else {
                    // Use the default icon
                    R.drawable.ic_notification_studybuddy_pencil_24dp
                }
                // Use the default icon
            } ?: R.drawable.ic_notification_studybuddy_pencil_24dp)

            builder.color = remoteMessage.notification?.color?.toColorInt() ?: dynamicColorPrimary

            // Image support was added in FCM 20.0.0
            notification.imageUrl?.let { imageUrl ->
                val loader = applicationContext.imageLoader
                val req = ImageRequest.Builder(this)
                    .data(imageUrl)
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

            fromApi(Build.VERSION_CODES.O) {
                // A notification channel is already set by default from the
                // NotificationCompat.Builder, so there's no else path
                remoteMessage.notification?.channelId?.let { channelId ->
                    if (manager.getNotificationChannel(channelId) != null) {
                        builder.setChannelId(channelId)
                    } else {
                        Log.w(TAG, "No such notification channel with ID $channelId exists")
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
                        createPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                        ) {
                            addNextIntentWithParentStack(buildIntent<MainActivity>(this@StudyBuddyMessagingService) {
                                action = Intent.ACTION_VIEW
                                data = UriSettings.toUri()
                            })
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

        // Checked by the extension function, see
        // https://issuetracker.google.com/issues/158721540 for feature request
        @SuppressLint("MissingPermission")
        if (checkPermissionGranted(Manifest.permission.POST_NOTIFICATIONS)) {
            manager.notify(notificationUtils.incrementAndGetId(), builder.build())
        }
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
