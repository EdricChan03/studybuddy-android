package com.edricchan.studybuddy.core.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.compositionLocalWithComputedDefaultOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.Flow

/** [CompositionLocal] of the currently signed-in [FirebaseUser], or `null`. */
val LocalCurrentUser: ProvidableCompositionLocal<FirebaseUser?> =
    compositionLocalOf { Firebase.auth.currentUser }

/** [CompositionLocal] of whether there's a currently signed-in [FirebaseUser]. */
val LocalIsSignedIn: ProvidableCompositionLocal<Boolean> =
    compositionLocalWithComputedDefaultOf { LocalCurrentUser.currentValue != null }

/**
 * Provides the [LocalCurrentUser] and [LocalIsSignedIn] composition locals with the
 * given [userFlow].
 *
 * The [userFlow] will be collected as a state and used for [LocalCurrentUser].
 */
@Composable
fun ProvideCurrentUser(
    initialUser: FirebaseUser? = Firebase.auth.currentUser,
    userFlow: Flow<FirebaseUser?>,
    content: @Composable () -> Unit
) {
    val user by userFlow.collectAsStateWithLifecycle(initialValue = initialUser)

    CompositionLocalProvider(
        LocalCurrentUser provides user,
        LocalIsSignedIn provides (user != null),
        content = content
    )
}
