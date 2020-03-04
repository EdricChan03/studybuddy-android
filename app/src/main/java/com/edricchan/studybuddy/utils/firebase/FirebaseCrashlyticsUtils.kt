package com.edricchan.studybuddy.utils.firebase

import android.util.Log
import com.crashlytics.android.Crashlytics
import com.edricchan.studybuddy.extensions.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseCrashlyticsUtils {
    companion object {
        /**
         * Enables Crashlytics user tracking for the currently logged-in user.
         *
         * @param user The current logged-in user, retrieved from [FirebaseAuth.getCurrentUser]
         * @param enabled Whether to enable user tracking.
         */
        fun setCrashlyticsUserTracking(user: FirebaseUser?, enabled: Boolean) {
            if (enabled) {
                if (user != null) {
                    Crashlytics.setUserEmail(user.email)
                    Crashlytics.setUserIdentifier(user.uid)
                    Crashlytics.setUserName(user.displayName)
                } else {
                    Log.w(TAG, "No currently logged-in user exists.")
                }
            }
        }

        /**
         * Enables Crashlytics user tracking for the currently logged-in user.
         *
         * @param auth An instance of [FirebaseAuth], retrieved from [FirebaseAuth.getInstance]
         * @param enabled Whether to enable user tracking.
         */
        fun setCrashlyticsUserTracking(auth: FirebaseAuth, enabled: Boolean) {
            setCrashlyticsUserTracking(auth.currentUser, enabled)
        }
    }
}