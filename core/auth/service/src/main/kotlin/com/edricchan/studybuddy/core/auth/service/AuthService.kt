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
    suspend fun @receiver:UiContext Context.signInWithGoogle(
        credentialOptions: List<CredentialOption>
    ): Boolean

    //#region Update operations
    /**
     * Interface used to represent the possible result of an update profile operation.
     * @see updateEmail
     * @see updatePassword
     * @see updateDisplayName
     */
    sealed interface UpdateProfileResult {
        /** The update operation succeeded. */
        data object Success : UpdateProfileResult

        /** There is no currently signed-in user. */
        data object NotSignedIn : UpdateProfileResult

        /** An error occurred when attempting to perform the update operation. */
        data class Error(val error: Throwable) : UpdateProfileResult
    }

    /**
     * Updates the currently signed-in user's email to the specified [newEmail].
     *
     * Note that reauthentication may be required to perform this operation -
     * check [UpdateProfileResult.Error] accordingly.
     */
    suspend fun updateEmail(newEmail: String): UpdateProfileResult

    /**
     * Updates the currently signed-in user's password to the specified [newPassword].
     *
     * Note that reauthentication may be required to perform this operation -
     * check [UpdateProfileResult.Error] accordingly.
     */
    suspend fun updatePassword(newPassword: String): UpdateProfileResult

    /**
     * Updates the currently signed-in user's display name to the specified [newName].
     *
     * Note that reauthentication may be required to perform this operation -
     * check [UpdateProfileResult.Error] accordingly.
     */
    suspend fun updateDisplayName(newName: String): UpdateProfileResult
    //#endregion

    //#region Nuclear operations
    /**
     * Requests for the user's account to be deleted.
     *
     * Note that reauthentication may be required to perform this operation -
     * check [UpdateProfileResult.Error] accordingly.
     */
    suspend fun requestDelete(): UpdateProfileResult
    //#endregion
}
