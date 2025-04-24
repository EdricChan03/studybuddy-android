package com.edricchan.studybuddy.core.auth.gms.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import com.edricchan.studybuddy.exts.common.TAG
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Deprecated("Use the Sign in With Google functionality from androidx.credentials instead")
class GoogleAuth : ActivityResultContract<GoogleSignInOptions, GoogleSignInAccount?>() {
    override fun createIntent(context: Context, input: GoogleSignInOptions): Intent {
        val gso = GoogleSignIn.getClient(context, input)
        return gso.signInIntent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): GoogleSignInAccount? {
        if (resultCode != Activity.RESULT_OK) return null

        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
        return try {
            task.getResult(ApiException::class.java)
        } catch (e: Exception) {
            Log.w(TAG, "parseResult: Could not sign in:", e)
            null
        }
    }
}
