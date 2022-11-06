package com.edricchan.studybuddy.utils.firebase

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

/**
 * Enables Firebase Crashlytics user tracking for the currently logged-in user.
 *
 * @param crashlytics The instance of [FirebaseCrashlytics].
 * @param enabled Whether to enable user tracking.
 */
fun FirebaseUser.setCrashlyticsTracking(
    crashlytics: FirebaseCrashlytics = Firebase.crashlytics,
    enabled: Boolean
) {
    crashlytics.setUserId(if (enabled) uid else "")
}
