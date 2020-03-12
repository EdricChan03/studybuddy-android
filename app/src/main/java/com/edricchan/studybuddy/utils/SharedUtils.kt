package com.edricchan.studybuddy.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.util.Log
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceManagerFix
import androidx.work.*
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.constants.sharedprefs.DevModePrefConstants
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.extensions.buildIntent
import com.edricchan.studybuddy.interfaces.TaskItem
import com.edricchan.studybuddy.receivers.NotificationActionReceiver
import com.edricchan.studybuddy.ui.modules.updates.UpdatesActivity
import com.edricchan.studybuddy.workers.CheckForUpdatesWorker
import com.github.javiersantos.appupdater.AppUpdaterUtils
import com.github.javiersantos.appupdater.enums.AppUpdaterError
import com.github.javiersantos.appupdater.enums.UpdateFrom
import com.github.javiersantos.appupdater.objects.Update
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

/**
 * Shared utility methods
 */
class SharedUtils {
    companion object {
        /**
         * Replaces a view with an initialised fragment.
         * Note: This method checks if there's already a fragment in the view.
         *
         * @param activity       The activity.
         * @param fragment       The fragment to replace the view with. (Needs to be initialised with a `new` constructor).
         * @param viewId         The ID of the view.
         * @param tag            The tag to assign to the fragment.
         * @param addToBackStack Whether to add the fragment to the back stack.
         * @return True if the fragment was replaced, false if there's already an existing fragment.
         */
        fun replaceFragment(
            activity: AppCompatActivity,
            fragment: Fragment, @IdRes viewId: Int,
            tag: String,
            addToBackStack: Boolean
        ): Boolean {
            // Check if fragment already has been replaced
            if (activity.supportFragmentManager.findFragmentByTag(tag) !== fragment && activity.supportFragmentManager.findFragmentById(
                    viewId
                ) !== fragment
            ) {
                val transaction = activity.supportFragmentManager.beginTransaction()
                transaction.setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                transaction.replace(
                    viewId,
                    fragment,
                    tag
                )
                if (addToBackStack) {
                    transaction.addToBackStack(null)
                }
                transaction.commit()
                // Indicate that the fragment replacement has been done.
                return true
            }
            // Return false if there's already an existing fragment.
            return false
        }

        /**
         * Replaces a view with an initialised fragment.
         * Note: This method checks if there's already a fragment in the view.
         *
         * @param activity       The activity.
         * @param fragment       The fragment to replace the view with. (Needs to be initialised with a `new` constructor).
         * @param viewId         The ID of the view.
         * @param addToBackStack Whether to add the fragment to the back stack.
         * @return True if the fragment was replaced, false if there's already an existing fragment.
         */
        fun replaceFragment(
            activity: AppCompatActivity,
            fragment: Fragment, @IdRes viewId: Int,
            addToBackStack: Boolean
        ): Boolean {
            if (activity.supportFragmentManager.findFragmentById(viewId) !== fragment) {
                val transaction = activity.supportFragmentManager.beginTransaction()
                transaction.setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                transaction.replace(
                    viewId,
                    fragment
                )
                if (addToBackStack) {
                    transaction.addToBackStack(null)
                }
                transaction.commit()
                // Indicate that the fragment replacement has been done.
                return true
            }
            // Return false if there's already an existing fragment.
            return false
        }

        /**
         * Checks whether the network is available
         *
         * @param context The context
         * @return A boolean
         */
        @Deprecated("Use ConnectivityManager.getActiveNetworkInfo")
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService<ConnectivityManager>()
            val activeNetworkInfo = connectivityManager?.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

        /**
         * Checks whether the network is cellular
         *
         * @param context The context
         * @return A boolean
         * See https://stackoverflow.com/a/32771164
         * TODO: Use other way of checking for mobile data
         * TODO: Deprecate this method
         */
        fun isCellularNetworkAvailable(context: Context): Boolean {
            try {
                val cm =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork = cm.activeNetworkInfo
                if (activeNetwork != null) {
                    // connected to the mobile provider's data plan
                    return activeNetwork.type == ConnectivityManager.TYPE_MOBILE
                }
            } catch (e: Exception) {
                Log.w(
                    TAG,
                    "An error occurred while attempting to retrieve the cellular network: ",
                    e
                )
                return false
            }

            return false
        }

        /**
         * Adds a new task to the Firebase Firestore database
         *
         * @param item The task item to add
         * @param user The currently authenticated user
         * @param fs   An instance of [FirebaseFirestore]
         * @return The result.
         */
        @Deprecated(
            "Use com.edricchan.studybuddy.ui.modules.task.utils.TaskUtils#addTask instead",
            ReplaceWith(
                "TaskUtils.getInstance(user, fs).addTask(item)",
                "com.edricchan.studybuddy.ui.modules.task.utils.TaskUtils"
            )
        )
        fun addTask(
            item: TaskItem,
            user: FirebaseUser,
            fs: FirebaseFirestore
        ): Task<DocumentReference> {
            return fs.collection("users/${user.uid}/todos").add(item)
        }

        /**
         * Removes a task from the Firebase Firestore database
         *
         * @param docId The document's ID
         * @param user  The currently authenticated user
         * @param fs    An instance of [FirebaseFirestore]
         * @return The result of the deletion
         */
        @Deprecated(
            "Use com.edricchan.studybuddy.ui.modules.task.utils.TaskUtils#removeTask instead",
            ReplaceWith(
                "TaskUtils.getInstance(user, fs).removeTask(docId)",
                "com.edricchan.studybuddy.ui.modules.task.utils.TaskUtils"
            )
        )
        fun removeTask(docId: String, user: FirebaseUser, fs: FirebaseFirestore): Task<Void> {
            return fs.document("users/${user.uid}/todos/$docId").delete()
        }

        /**
         * An utility method to check for updates.
         *
         * @param context The context.
         */
        fun checkForUpdates(context: Context) {
            val notificationManager = NotificationManagerCompat.from(context)
            val notifyBuilder = NotificationCompat.Builder(
                context,
                context.getString(R.string.notification_channel_update_status_id)
            )
                .setSmallIcon(R.drawable.ic_notification_system_update_24dp)
                .setContentTitle(context.getString(R.string.notification_check_update))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setProgress(100, 0, true)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setOngoing(true)
                .setOnlyAlertOnce(true)
            notificationManager.notify(
                Constants.notificationCheckForUpdatesId,
                notifyBuilder.build()
            )
            val appUpdaterUtils = getUpdateJsonUrl(context)?.let {
                AppUpdaterUtils(context)
                    .setUpdateFrom(UpdateFrom.JSON)
                    .setUpdateJSON(it)
                    .withListener(object : AppUpdaterUtils.UpdateListener {
                        override fun onSuccess(update: Update, updateAvailable: Boolean?) {
                            if (update.latestVersionCode == BuildConfig.VERSION_CODE && (!updateAvailable!!)) {
                                // User is running latest version
                                notifyBuilder.setContentTitle(context.getString(R.string.notification_no_updates))
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                notificationManager.notify(
                                    Constants.notificationCheckForUpdatesId,
                                    notifyBuilder.build()
                                )
                            } else {
                                // New update
                                val intentAction =
                                    Intent(context, NotificationActionReceiver::class.java)
                                intentAction.putExtra(
                                    "action",
                                    Constants.actionNotificationsStartDownloadReceiver
                                )
                                intentAction.putExtra(
                                    "downloadUrl",
                                    update.urlToDownload.toString()
                                )
                                intentAction.putExtra("version", update.latestVersion)
                                val pIntentDownload = PendingIntent.getBroadcast(
                                    context,
                                    1,
                                    intentAction,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                                )

                                val contentIntent = buildIntent<UpdatesActivity>(context) {
                                    flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                                val pContentIntent =
                                    PendingIntent.getActivity(context, 0, contentIntent, 0)

                                notifyBuilder.setContentTitle(context.getString(R.string.notification_new_update_title))
                                    .setContentText(
                                        context.getString(
                                            R.string.notification_new_update_text,
                                            update.latestVersion
                                        )
                                    )
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setChannelId(context.getString(R.string.notification_channel_update_available_id))
                                    .setContentIntent(pContentIntent)
                                    .setAutoCancel(true)
                                    .addAction(
                                        NotificationCompat.Action(
                                            R.drawable.ic_download_24dp,
                                            "Download",
                                            pIntentDownload
                                        )
                                    )
                                notificationManager.notify(
                                    Constants.notificationCheckForUpdatesId,
                                    notifyBuilder.build()
                                )
                            }
                        }

                        override fun onFailed(appUpdaterError: AppUpdaterError) {
                            when (appUpdaterError) {
                                AppUpdaterError.NETWORK_NOT_AVAILABLE -> notifyBuilder.setContentTitle(
                                    context.getString(R.string.notification_updates_error_no_internet_title)
                                )
                                    .setContentText(context.getString(R.string.notification_updates_error_no_internet_text))
                                    .setSmallIcon(R.drawable.ic_wifi_strength_4_alert_24dp)
                                AppUpdaterError.JSON_ERROR -> notifyBuilder.setContentTitle(
                                    context.getString(
                                        R.string.notification_updates_error_not_found_title
                                    )
                                )
                                    .setContentText(context.getString(R.string.notification_updates_error_not_found_text))
                                    .setSmallIcon(R.drawable.ic_file_not_found_24dp)
                            }
                            val intentAction =
                                Intent(context, NotificationActionReceiver::class.java)

                            //This is optional if you have more than one buttons and want to differentiate between two
                            intentAction.putExtra(
                                "action",
                                Constants.actionNotificationsRetryCheckForUpdateReceiver
                            )
                            val pIntentRetry = PendingIntent.getBroadcast(
                                context,
                                2,
                                intentAction,
                                PendingIntent.FLAG_UPDATE_CURRENT
                            )
                            notificationManager.notify(
                                Constants.notificationCheckForUpdatesId,
                                notifyBuilder
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setChannelId(context.getString(R.string.notification_channel_update_error_id))
                                    .setColor(ContextCompat.getColor(context, R.color.colorWarn))
                                    .addAction(
                                        NotificationCompat.Action(
                                            R.drawable.ic_refresh_24dp,
                                            "Retry",
                                            pIntentRetry
                                        )
                                    )
                                    .setStyle(NotificationCompat.BigTextStyle())
                                    .build()
                            )
                        }
                    })
            }
            appUpdaterUtils?.start()
        }

        /**
         * Retrieves the JSON update URL
         * Used for [SharedUtils.checkForUpdates]
         * @param context The context
         * @param forceDebugUrl Whether to return the debug URL even if [SharedUtils.isDevMode] is [true]
         * @return The JSON update URL as a [String]
         */
        fun getUpdateJsonUrl(context: Context, forceDebugUrl: Boolean = false): String? {
            return if (isDevMode(context) || forceDebugUrl) {
                val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
                if (mPrefs.getBoolean(Constants.debugUseTestingJsonUrl, true)) {
                    mPrefs.getString(
                        Constants.debugSetCustomJsonUrl,
                        context.getString(R.string.update_json_testing_url)
                    )
                } else {
                    context.getString(R.string.update_json_release_url)
                }
            } else {
                context.getString(R.string.update_json_release_url)
            }
        }

        /**
         * Checks if the permission is granted.
         * Returns false if the permission isn't granted, true otherwise.
         *
         * @param permission The permission to check
         * @param context    The context
         * @return A boolean
         */
        @Deprecated(
            "Use com.edricchan.studybuddy.utils.PermissionUtils#checkPermissionGranted instead.",
            ReplaceWith(
                "PermissionUtils.getInstance(context).checkPermissionGranted(permission)",
                "com.edricchan.studybuddy.utils.PermissionUtils"
            )
        )
        fun checkPermissionGranted(permission: String, context: Context): Boolean {
            return ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }

        /**
         * Enqueues the [CheckForUpdatesWorker] request
         * @param context The context
         * @return The results of the enqueued worker request
         */
        fun enqueueCheckForUpdatesWorker(context: Context): Operation? {
            val sharedPreferences = PreferenceManagerFix.getDefaultSharedPreferences(context)
            val defaultInterval =
                context.resources.getInteger(R.integer.pref_check_for_updates_frequency_default_value)
            val repeatInterval = sharedPreferences.getString(
                Constants.prefUpdatesFrequency,
                defaultInterval.toString()
            )!!.toLong()
            val isMetered =
                sharedPreferences.getBoolean(Constants.prefUpdatesDownloadOverMetered, false)
            val requiresCharging =
                sharedPreferences.getBoolean(Constants.prefUpdatesDownloadOnlyWhenCharging, false)

            return enqueueCheckForUpdatesWorker(
                context,
                repeatInterval,
                isMetered,
                requiresCharging
            )
        }

        /**
         * Enqueues the [CheckForUpdatesWorker] request
         * @param context The context
         * @param repeatInterval The minimum time between repetitions
         * @param isMetered Whether the worker is allowed to execute when the device is metered
         * @param requiresCharging Whether the worker is allowed to execute if the device is charging
         * @return The results of the enqueued worker request
         */
        fun enqueueCheckForUpdatesWorker(
            context: Context,
            repeatInterval: Long,
            isMetered: Boolean,
            requiresCharging: Boolean
        ): Operation? {
            var result: Operation? = null

            if (repeatInterval > 0) {
                // Check for updates frequency is not set to manual/never
                Log.d(
                    TAG,
                    "Check for updates frequency is not set to manual/never. Creating worker request..."
                )

                val networkType = if (isMetered) NetworkType.METERED else NetworkType.UNMETERED

                val constraints = Constraints.Builder().apply {
                    setRequiredNetworkType(networkType)
                    setRequiresCharging(requiresCharging)
                }.build()

                val checkForUpdatesWorkerRequest =
                    PeriodicWorkRequestBuilder<CheckForUpdatesWorker>(
                        repeatInterval,
                        TimeUnit.HOURS
                    ).apply {
                        setConstraints(constraints)
                    }.build()

                result = WorkManager.getInstance(context).enqueue(checkForUpdatesWorkerRequest)
            } else {
                Log.d(
                    TAG,
                    "Check for updates frequency is set to manual/never. Skipping worker request..."
                )
            }
            return result
        }

        /**
         * Whether the app is in developer mode.
         * @param context The context to be used to retrieve the developer mode options from.
         * @param useSharedPrefsOnly Whether to only check from shared preferences
         * @return [true] if the app is in developer mode, [false] otherwise.
         */
        fun isDevMode(context: Context, useSharedPrefsOnly: Boolean = false): Boolean {
            val devModeOpts = context.getSharedPreferences(
                DevModePrefConstants.FILE_DEV_MODE,
                Context.MODE_PRIVATE
            )
            return if (useSharedPrefsOnly) {
                devModeOpts.getBoolean(DevModePrefConstants.DEV_MODE_ENABLED, false)
            } else {
                devModeOpts.getBoolean(
                    DevModePrefConstants.DEV_MODE_ENABLED,
                    false
                ) || BuildConfig.DEBUG
            }
        }
    }
}
