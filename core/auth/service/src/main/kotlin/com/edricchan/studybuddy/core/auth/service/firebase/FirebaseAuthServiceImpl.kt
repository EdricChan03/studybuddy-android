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
import com.edricchan.studybuddy.exts.firebase.auth.currentUserFlow
import com.edricchan.studybuddy.exts.firebase.auth.signInWithEmailAndPasswordAsync
import com.google.firebase.auth.FirebaseAuth
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
        auth.signInWithEmailAndPasswordAsync(email, password).user != null

    override suspend fun @receiver:UiContext Context.signInWithGoogle(
        credentialOptions: List<CredentialOption>
    ): Boolean = auth.signInWithGoogleCredentials(
        context = this,
        credentialOptions = credentialOptions
    ).user != null
}
