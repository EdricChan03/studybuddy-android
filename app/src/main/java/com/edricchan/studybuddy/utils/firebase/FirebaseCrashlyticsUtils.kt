package com.edricchan.studybuddy.utils.firebase

import android.util.Log
import com.edricchan.studybuddy.extensions.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

object FirebaseCrashlyticsUtils {
    /**
     * Enables Firebase Crashlytics user tracking for the currently logged-in user.
     *
     * @param user The current logged-in user, retrieved from [FirebaseAuth.getCurrentUser]
     * @param enabled Whether to enable user tracking.
     */
    fun setCrashlyticsUserTracking(user: FirebaseUser?, enabled: Boolean) {
        val crashlytics = Firebase.crashlytics
        if (enabled) {
            if (user != null) {
                crashlytics.setUserId(user.uid)
            } else {
                Log.w(TAG, "No currently logged-in user exists.")
            }
        } else {
            // Reset the user's ID.
            crashlytics.setUserId("")
        }
    }

    /**
     * Enables Firebase Crashlytics user tracking for the currently logged-in user.
     *
     * @param auth An instance of [FirebaseAuth].
     * @param enabled Whether to enable user tracking.
     */
    fun setCrashlyticsUserTracking(auth: FirebaseAuth, enabled: Boolean) {
        setCrashlyticsUserTracking(auth.currentUser, enabled)
    }
}
