package com.edricchan.studybuddy.exts.firebase.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
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
 * Asynchronously creates a user using the email + password authentication method,
 * with the given [email] and [password].
 * @return The result of the user creation request.
 * @see FirebaseAuth.createUserWithEmailAndPassword
 */
suspend fun FirebaseAuth.createUserWithEmailAndPasswordAsync(
    email: String, password: String
): AuthResult = createUserWithEmailAndPassword(email, password).await()

/**
 * Asynchronously signs in via the email + password authentication method, using the given
 * [email] and [password].
 * @return The result of the sign in request.
 * @see FirebaseAuth.signInWithEmailAndPassword
 */
suspend fun FirebaseAuth.signInWithEmailAndPasswordAsync(
    email: String, password: String
): AuthResult = signInWithEmailAndPassword(email, password).await()

/**
 * Asynchronously signs in to Google using the given [account].
 * @return The result of the sign in request.
 * @see FirebaseAuth.signInWithCredential
 * @see GoogleAuthProvider
 */
suspend fun FirebaseAuth.signInWithGoogleAsync(account: GoogleSignInAccount): AuthResult {
    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
    return signInWithCredential(credential).await()
}

/**
 * Gets the [currently signed-in user][FirebaseAuth.getCurrentUser] as a
 * [kotlinx.coroutines.flow.Flow].
 */
val FirebaseAuth.currentUserFlow
    get() = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        addAuthStateListener(listener)
        awaitClose { removeAuthStateListener(listener) }
    }
