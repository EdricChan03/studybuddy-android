package com.edricchan.studybuddy.core.auth.service

import android.content.Context
import androidx.annotation.UiContext
import androidx.credentials.CredentialOption
import com.edricchan.studybuddy.core.auth.model.User
import kotlinx.coroutines.flow.Flow

interface AuthService {
    /** The currently signed-in [User] data as an observable [Flow], or `null` if not signed in. */
    val userFlow: Flow<User?>

    /** The current signed-in [User] data, or `null` if not signed in. */
    val currentUser: User?

    /** Whether the user is signed in. */
    val isSignedIn: Boolean get() = currentUser != null

    /** Whether the user is signed in, as a observable [Flow]. */
    val isSignedInFlow: Flow<Boolean>

    /** Signs out the user with the receiver [Context]. */
    suspend fun Context.signOut()

    /**
     * Signs in the user with the given [email] and [password].
     * @return Whether the operation was successful.
     */
    suspend fun signIn(email: String, password: String): Boolean

    /**
     * Signs in the user using Google OAuth with the provided [credentialOptions].
     * @receiver A UI [Context] for showing the sign in dialog.
     * @return Whether the operation was successful.
     */
    suspend fun @receiver:UiContext Context.signInWithGoogle(credentialOptions: List<CredentialOption>): Boolean
}
