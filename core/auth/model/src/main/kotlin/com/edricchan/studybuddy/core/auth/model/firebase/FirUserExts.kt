package com.edricchan.studybuddy.core.auth.model.firebase

import com.edricchan.studybuddy.core.auth.model.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Converts the receiver [FirebaseUser] to its [User] equivalent.
 */
fun FirebaseUser.toUser(): User = User(
    id = uid,
    displayName = displayName,
    email = email,
    photoUri = photoUrl
)

/**
 * Collects the receiver [FirebaseUser] flow as its [User.AuthState] equivalent.
 * @param scope [CoroutineScope] to be passed to [stateIn].
 */
fun Flow<FirebaseUser?>.collectAsAuthState(
    scope: CoroutineScope
): StateFlow<User.AuthState> = map {
    if (it == null) return@map User.AuthState.SignedOut
    User.AuthState.SignedIn(it.toUser())
}.stateIn(scope, SharingStarted.WhileSubscribed(), User.AuthState.Loading)
