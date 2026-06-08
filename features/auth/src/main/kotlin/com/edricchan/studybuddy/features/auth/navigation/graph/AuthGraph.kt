package com.edricchan.studybuddy.features.auth.navigation.graph

import android.content.Context
import androidx.navigation.NavGraphBuilder
import androidx.navigation.fragment.fragment
import androidx.navigation.navigation
import com.edricchan.studybuddy.features.auth.account.compat.AccountFragment
import com.edricchan.studybuddy.features.auth.login.ui.compat.LoginFragment
import com.edricchan.studybuddy.features.auth.navigation.AuthDestination
import com.edricchan.studybuddy.features.auth.recovery.ui.compat.RecoveryFragment
import com.edricchan.studybuddy.features.auth.register.ui.compat.RegisterFragment
import kotlin.reflect.typeOf
import com.edricchan.studybuddy.features.auth.navigation.R as NavR

/**
 * Adds the authentication feature's nav-graph to the receiver [NavGraphBuilder].
 * @param context [Context] to be used to retrieve the label string resources from.
 * @see AuthDestination
 */
context(context: Context)
fun NavGraphBuilder.authGraph(
    isLoggedIn: Boolean
) = navigation<AuthDestination.AuthGraphRoot>(
    startDestination = if (isLoggedIn) AuthDestination.AccountInfo else AuthDestination.Login
) {
    fragment<AccountFragment, AuthDestination.AccountInfo>(
        typeMap = mapOf(typeOf<AuthDestination.AccountInfo.AccountAction>() to AuthDestination.AccountInfo.AccountAction.NavType)
    ) {
        label = context.getString(NavR.string.nav_auth_dest_account_info_label)
    }

    fragment<LoginFragment, AuthDestination.Login> {
        label = context.getString(NavR.string.nav_auth_dest_login_label)
    }

    fragment<RegisterFragment, AuthDestination.Register> {
        label = context.getString(NavR.string.nav_auth_dest_register_label)
    }

    fragment<RecoveryFragment, AuthDestination.Recovery> {
        label = context.getString(NavR.string.nav_auth_dest_recover_account_label)
    }
}
