package com.edricchan.studybuddy.utils.gapi

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.Scope

class GAuthApiUtils {
	companion object {
		/**
		 * Utility method to check if the [signInAccount] has the specified [permissions].
		 * @param signInAccount The currently signed-in account.
		 * @param permissions The list of permissions.
		 * @return `true` if the [signInAccount] has the specified permissions, `false` otherwise
		 * @see GoogleSignIn.hasPermissions
		 */
		fun hasPermissions(signInAccount: GoogleSignInAccount, vararg permissions: Scope) = GoogleSignIn
				.hasPermissions(signInAccount, *permissions)
	}
}