package com.edricchan.studybuddy.core.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.compositionLocalWithComputedDefaultOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edricchan.studybuddy.core.auth.model.User
import com.edricchan.studybuddy.core.auth.model.firebase.toUser
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.Flow

/** [CompositionLocal] of the currently signed-in [User], or `null`. */
val LocalCurrentUser: ProvidableCompositionLocal<User?> =
    compositionLocalOf { null }

/** [CompositionLocal] of whether there's a currently signed-in [User]. */
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
        LocalCurrentUser provides user?.toUser(),
        LocalIsSignedIn provides (user != null),
        content = content
    )
}
