package com.edricchan.studybuddy.core.auth.service.firebase

import android.content.Context
import android.util.Log
import androidx.annotation.UiContext
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.exceptions.ClearCredentialException
import com.edricchan.studybuddy.core.auth.credentials.signInWithGoogleCredentials
import com.edricchan.studybuddy.core.auth.model.User
import com.edricchan.studybuddy.core.auth.model.firebase.toUser
import com.edricchan.studybuddy.core.auth.service.AuthService
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.exts.firebase.auth.awaitSignInWithEmailAndPassword
import com.edricchan.studybuddy.exts.firebase.auth.currentUserFlow
import com.edricchan.studybuddy.exts.firebase.auth.deleteAsync
import com.edricchan.studybuddy.exts.firebase.auth.updateEmailAsync
import com.edricchan.studybuddy.exts.firebase.auth.updatePasswordAsync
import com.edricchan.studybuddy.exts.firebase.auth.updateProfileAsync
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** Implementation of [AuthService] backed by [FirebaseAuth]. */
class FirebaseAuthServiceImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthService {
    override val userFlow: Flow<User?> = auth.currentUserFlow.map { user -> user?.toUser() }

    override val currentUser: User?
        get() = auth.currentUser?.toUser()

    override val isSignedInFlow: Flow<Boolean> = userFlow.map { it != null }

    override suspend fun Context.signOut() {
        auth.signOut()
        try {
            val clearRequest = ClearCredentialStateRequest()
            CredentialManager.create(this).clearCredentialState(clearRequest)
        } catch (e: ClearCredentialException) {
            Log.e(
                this@FirebaseAuthServiceImpl.TAG,
                "Couldn't clear user credentials: ${e.localizedMessage}"
            )
        }
    }

    override suspend fun signIn(email: String, password: String): Boolean =
        auth.awaitSignInWithEmailAndPassword(email, password).user != null

    override suspend fun @receiver:UiContext Context.signInWithGoogle(
        credentialOptions: List<CredentialOption>
    ): Boolean = auth.signInWithGoogleCredentials(
        context = this,
        credentialOptions = credentialOptions
    ).user != null

    private inline fun updateProfile(updateFn: (FirebaseUser) -> Unit): AuthService.UpdateProfileResult {
        val user = auth.currentUser ?: return AuthService.UpdateProfileResult.NotSignedIn

        return runCatching { updateFn(user) }
            .fold(
                onSuccess = { AuthService.UpdateProfileResult.Success },
                onFailure = AuthService.UpdateProfileResult::Error
            )
    }

    override suspend fun updateEmail(newEmail: String): AuthService.UpdateProfileResult =
        updateProfile { it.updateEmailAsync(newEmail) }

    override suspend fun updatePassword(newPassword: String): AuthService.UpdateProfileResult =
        updateProfile { it.updatePasswordAsync(newPassword) }

    override suspend fun updateDisplayName(newName: String): AuthService.UpdateProfileResult =
        updateProfile { it.updateProfileAsync { displayName = newName } }

    override suspend fun requestDelete(): AuthService.UpdateProfileResult = updateProfile {
        it.deleteAsync()
    }
}
