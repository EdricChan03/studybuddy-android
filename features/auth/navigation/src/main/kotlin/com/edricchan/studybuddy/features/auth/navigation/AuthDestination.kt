package com.edricchan.studybuddy.features.auth.navigation

import kotlinx.serialization.Serializable

/** Type-safe destinations for the authentication feature. */
@Serializable
sealed interface AuthDestination {
    /** Root destination for all sub-destinations in the authentication graph. */
    @Serializable
    data object AuthGraphRoot : AuthDestination

    /** Destination to view the currently signed-in user's information. */
    @Serializable
    data object AccountInfo : AuthDestination

    /** Destination to sign in to the application. */
    @Serializable
    data object Login : AuthDestination

    /** Destination to create a user account. */
    @Serializable
    data object Register : AuthDestination

    /** Destination to recover a user account. */
    @Serializable
    data object Recovery : AuthDestination
}
