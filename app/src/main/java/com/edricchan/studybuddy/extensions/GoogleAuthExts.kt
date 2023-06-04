package com.edricchan.studybuddy.extensions

import android.content.Context
import com.edricchan.studybuddy.R
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

/**
 * Creates an [GoogleSignInOptions] object given the specified
 * [GoogleSignInOptions.Builder] configuration.
 */
inline fun buildGoogleSignInOptions(init: GoogleSignInOptions.Builder.() -> Unit = {}) =
    GoogleSignInOptions.Builder().apply(init).build()

/**
 * Creates an [GoogleSignInOptions] object given the specified
 * [GoogleSignInOptions.Builder] configuration, and an existing [options] object.
 */
inline fun buildGoogleSignInOptions(
    options: GoogleSignInOptions, init: GoogleSignInOptions.Builder.() -> Unit = {}
) = GoogleSignInOptions.Builder(options).apply(init).build()

/** Retrieves the default [GoogleSignInOptions] from the receiver [Context]. */
val Context.defaultSignInOptions
    get() = buildGoogleSignInOptions(GoogleSignInOptions.DEFAULT_SIGN_IN) {
        requestIdToken(getString(R.string.default_web_client_id))
        requestEmail()
    }
