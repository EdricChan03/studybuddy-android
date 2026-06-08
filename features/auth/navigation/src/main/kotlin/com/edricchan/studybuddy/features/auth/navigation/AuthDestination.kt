package com.edricchan.studybuddy.features.auth.navigation

import androidx.annotation.Keep
import androidx.core.os.BundleCompat
import androidx.navigation.NavType
import androidx.savedstate.SavedState
import kotlinx.serialization.Serializable

/** Type-safe destinations for the authentication feature. */
@Serializable
sealed interface AuthDestination {
    /** Root destination for all sub-destinations in the authentication graph. */
    @Serializable
    data object AuthGraphRoot : AuthDestination

    /** Destination to view the currently signed-in user's information. */
    @Serializable
    data class AccountInfo(val action: AccountAction? = null) : AuthDestination {
        /** The requested account action to perform, if any. */
        @Keep
        enum class AccountAction(val kebabId: String) {
            DeleteAccount("delete-account"),
            SignOut("sign-out"),
            UpdateEmail("update-email"),
            UpdateName("update-name"),
            UpdatePassword("update-password");

            companion object {
                // SerializableType has a runtime check to assert that Enums are not passed,
                // and EnumType is not extendable, so we have to make do with our own
                // NavType
                val NavType =
                    object : NavType<AccountAction>(isNullableAllowed = true) {
                        override fun get(
                            bundle: SavedState,
                            key: String
                        ): AccountAction? {
                            return BundleCompat.getSerializable(
                                bundle,
                                key,
                                AccountAction::class.java
                            )
                        }

                        override fun put(
                            bundle: SavedState,
                            key: String,
                            value: AccountAction
                        ) {
                            bundle.putSerializable(key, value)
                        }

                        override fun parseValue(value: String): AccountAction =
                            AccountAction.entries.first {
                                it.name.equals(value, ignoreCase = true) ||
                                    it.kebabId.equals(value, ignoreCase = true)
                            }
                    }
            }
        }
    }

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
