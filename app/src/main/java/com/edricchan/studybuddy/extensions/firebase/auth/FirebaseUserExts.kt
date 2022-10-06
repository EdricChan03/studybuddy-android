package com.edricchan.studybuddy.extensions.firebase.auth

import com.google.firebase.auth.FirebaseUserMetadata
import java.time.Instant

/**
 * Returns the timestamp at which this account was created as dictated by the server clock
 * as an [Instant].
 */
val FirebaseUserMetadata.creationInstant get() = Instant.ofEpochMilli(creationTimestamp)

/** Returns the last signin timestamp as dictated by the server clock as an [Instant]. */
val FirebaseUserMetadata.lastSignInInstant get() = Instant.ofEpochMilli(lastSignInTimestamp)
