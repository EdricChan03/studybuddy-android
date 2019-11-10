package com.edricchan.studybuddy.utils

import com.google.android.gms.auth.api.signin.GoogleSignInClient

class AuthUtils {
	/**
	 * The current Google Sign in client.
	 */
	var signInClient: GoogleSignInClient? = null

	companion object {
		val instance = AuthUtils()
	}
}