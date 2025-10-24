package com.edricchan.studybuddy.core.auth.credentials

import android.content.Context
import androidx.annotation.UiContext
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.edricchan.studybuddy.exts.firebase.auth.awaitSignInWithGoogle
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import com.edricchan.studybuddy.core.auth.gms.R as GmsR

@OptIn(ExperimentalUuidApi::class)
val Context.googleIdOption: GetGoogleIdOption
    get() = GetGoogleIdOption(
        filterByAuthorizedAccounts = true,
        autoSelectEnabled = true,
        serverClientId = getString(GmsR.string.default_web_client_id),
        nonce = Uuid.random().toString()
    )

/**
 * Attempts to parse the receiver [Credential] to its [GoogleIdTokenCredential] form, or
 * throws an [UnsupportedOperationException] if the credential is not a recognised
 * [GoogleIdTokenCredential].
 */
fun Credential.asGoogleIdTokenCredential(): GoogleIdTokenCredential = when (this) {
    is GoogleIdTokenCredential -> this

    is CustomCredential -> when (type) {
        GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
            GoogleIdTokenCredential.createFrom(data)
        }

        else -> throw UnsupportedOperationException("Credential not supported: $this")
    }

    else -> throw UnsupportedOperationException("Credential not supported: $this")
}

/**
 * Requests for a [GoogleIdTokenCredential] with the given [credentialOptions].
 * @receiver A [Context] that must be of a UI context.
 * @param credentialManager Desired [CredentialManager] to call [CredentialManager.getCredential]
 * against.
 */
suspend fun @receiver:UiContext Context.requestGoogleCredential(
    credentialOptions: List<CredentialOption> = listOf(googleIdOption),
    credentialManager: CredentialManager = CredentialManager.create(this)
): GoogleIdTokenCredential {
    val getCredentialRequest = GetCredentialRequest(credentialOptions = credentialOptions)

    val getCredentialResponse = credentialManager.getCredential(
        context = this,
        request = getCredentialRequest
    )

    return getCredentialResponse.credential.asGoogleIdTokenCredential()
}

/**
 * Signs in with the specified [credentialOptions] and [context] to be passed to
 * [requestGoogleCredential].
 * @param context A [Context] that must be of a UI context.
 * @param credentialOptions List of credential options to be passed to [requestGoogleCredential].
 * @param credentialManager Desired [CredentialManager] instance to be used in
 * [requestGoogleCredential].
 * @return The result of [awaitSignInWithGoogle].
 */
suspend fun FirebaseAuth.signInWithGoogleCredentials(
    @UiContext context: Context,
    credentialOptions: List<CredentialOption> = listOf(context.googleIdOption),
    credentialManager: CredentialManager = CredentialManager.create(context)
): AuthResult {
    val cred = context.requestGoogleCredential(
        credentialOptions = credentialOptions,
        credentialManager = credentialManager
    )

    return awaitSignInWithGoogle(cred)
}
