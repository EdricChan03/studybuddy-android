package com.edricchan.studybuddy.exts.firebase.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Retrieves the user's Firestore document
 * @param fs An instance of [FirebaseFirestore]
 * @return The user's Firestore document
 */
fun FirebaseUser?.getUserDocument(fs: FirebaseFirestore) =
    fs.document("users/${this?.uid}")

/**
 * Retrieves the user's Firestore document.
 */
val FirebaseUser?.userDocument: DocumentReference
    get() = this.getUserDocument(Firebase.firestore)

/**
 * Suspends the current coroutine creating a user using the email + password authentication method
 * with the given [email] and [password].
 * @return The result of the user creation request.
 * @see FirebaseAuth.createUserWithEmailAndPassword
 */
suspend fun FirebaseAuth.awaitCreateUserWithEmailAndPassword(
    email: String, password: String
): AuthResult = createUserWithEmailAndPassword(email, password).await()

/**
 * Suspends the current coroutine attempting to sign in via the email + password authentication
 * method, using the given [email] and [password].
 * @return The result of the sign in request.
 * @see FirebaseAuth.signInWithEmailAndPassword
 */
suspend fun FirebaseAuth.awaitSignInWithEmailAndPassword(
    email: String, password: String
): AuthResult = signInWithEmailAndPassword(email, password).await()

/**
 * Suspends the current coroutine and attempts to sign in to Google using the given [account].
 * @return The result of the sign in request.
 * @see FirebaseAuth.signInWithCredential
 * @see GoogleAuthProvider
 */
suspend fun FirebaseAuth.awaitSignInWithGoogle(account: GoogleSignInAccount): AuthResult {
    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
    return signInWithCredential(credential).await()
}

/**
 * Suspends the current coroutine and attempts to sign in to Google using the given [credential].
 * @return The result of the sign in request.
 * @see FirebaseAuth.signInWithCredential
 * @see GoogleAuthProvider
 * @see GoogleIdTokenCredential
 */
suspend fun FirebaseAuth.awaitSignInWithGoogle(credential: GoogleIdTokenCredential): AuthResult {
    val firCredential = GoogleAuthProvider.getCredential(credential.idToken, null)
    return signInWithCredential(firCredential).await()
}

/** Gets the [currently signed-in user][FirebaseAuth.getCurrentUser] as a [Flow]. */
val FirebaseAuth.currentUserFlow: Flow<FirebaseUser?>
    get() = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        addAuthStateListener(listener)
        awaitClose { removeAuthStateListener(listener) }
    }
