package com.edricchan.studybuddy.extensions.firebase.auth

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseUserMetadata
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.tasks.await
import java.time.Instant

/**
 * Returns the timestamp at which this account was created as dictated by the server clock
 * as an [Instant].
 */
inline val FirebaseUserMetadata.creationInstant get() = Instant.ofEpochMilli(creationTimestamp)

/** Returns the last signin timestamp as dictated by the server clock as an [Instant]. */
inline val FirebaseUserMetadata.lastSignInInstant get() = Instant.ofEpochMilli(lastSignInTimestamp)

/**
 * Deletes the user record from your Firebase project's database. If the operation is
 * successful, the user will be signed out.
 *
 * **Important**: this is a security sensitive operation that requires the user to have
 * recently signed in. If this requirement isn't met, ask the user to authenticate again
 * and later call [FirebaseUser.reauthenticate].
 *
 * This method is an alias of `delete().await()`.
 * @see FirebaseUser.delete
 */
suspend inline fun FirebaseUser.deleteAsync() {
    delete().await()
}

/**
 * Reauthenticates the user with the given credential asynchronously. This is useful
 * for operations that require a recent sign-in, to prevent or resolve a
 * [com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException].
 *
 * This method is an alias of `reauthenticate(credential).await()`.
 * @param credential The credential to reauthenticate with.
 * @see FirebaseUser.reauthenticate
 */
suspend inline fun FirebaseUser.reauthenticateAsync(credential: AuthCredential) {
    reauthenticate(credential).await()
}

/**
 * Manually refreshes the data of the current user (for example, attached providers,
 * display name, and so on).
 *
 * This method is an alias of `reload().await()`.
 * @see FirebaseUser.reload
 */
suspend inline fun FirebaseUser.reloadAsync() {
    reload().await()
}

/**
 * Updates the email address of the user asynchronously.
 *
 * **Important:** this is a security sensitive operation that requires the user to
 * have recently signed in. If this requirement isn't met, ask the user to authenticate
 * again and later call [FirebaseUser.reauthenticate].
 *
 * In addition, note that the original email address recipient will receive an email that
 * allows them to revoke the email address change, in order to protect them from account hijacking.
 *
 * This method is an alias of `updateEmail(email).await()`.
 * @param email The new email address.
 * @see FirebaseUser.updateEmail
 */
suspend inline fun FirebaseUser.updateEmailAsync(email: String) {
    updateEmail(email).await()
}

/**
 * Updates the password of the user asynchronously.
 *
 * **Important:** this is a security sensitive operation that requires the user to
 * have recently signed in. If this requirement isn't met, ask the user to authenticate
 * again and later call [FirebaseUser.reauthenticate].
 *
 * Anonymous users who update both their email and password will no longer be anonymous.
 * They will be able to log in with these credentials.
 *
 * This method is an alias of `updatePassword(password).await()`.
 * @param password The new password.
 * @see FirebaseUser.updatePassword
 */
suspend inline fun FirebaseUser.updatePasswordAsync(password: String) {
    updatePassword(password).await()
}

/**
 * Updates the user profile information asynchronously.
 *
 * This method is an alias of `updateProfile(request).await()`, and allows for
 * trailing lambda syntax to be used.
 * @param requestInit Configuration to be used to create a new [UserProfileChangeRequest].
 * @see FirebaseUser.updateProfile
 */
suspend inline fun FirebaseUser.updateProfileAsync(
    requestInit: UserProfileChangeRequest.Builder.() -> Unit
) {
    updateProfile(userProfileChangeRequest(requestInit)).await()
}
