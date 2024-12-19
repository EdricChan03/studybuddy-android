package com.edricchan.studybuddy.core.auth.model

import android.net.Uri

/**
 * The currently signed-in user.
 * @property id The user's unique ID.
 * @property displayName The user's display name.
 * @property email The user's email address.
 * @property photoUri The user's photo URL to be used.
 */
data class User(
    val id: String,
    val displayName: String?,
    val email: String?,
    val photoUri: Uri?
) {
    /** The authentication state. */
    sealed interface AuthState {
        /** The intermediary state when the user's data hasn't completed loading. */
        data object Loading : AuthState

        /** The user has signed in with the relevant [user] data. */
        data class SignedIn(val user: User) : AuthState

        /** The user has signed out. */
        data object SignedOut : AuthState
    }
}
