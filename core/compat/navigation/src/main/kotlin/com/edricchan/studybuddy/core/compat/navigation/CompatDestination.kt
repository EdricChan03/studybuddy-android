package com.edricchan.studybuddy.core.compat.navigation

import androidx.annotation.Keep
import androidx.core.os.BundleCompat
import androidx.navigation.NavType
import androidx.savedstate.SavedState
import kotlinx.serialization.Serializable

/** Destinations that have yet to be migrated to Jetpack Compose. */
// TODO: Remove
@Serializable
sealed interface CompatDestination {
    // Top-level destinations

    /** Typed destination for the `DebugActivity` entry-point. */
    @Serializable
    data object Debug : CompatDestination

    /** Typed destination for the `DebugModalBottomSheetActivity` entry-point. */
    @Serializable
    data object DebugModalBottomSheet : CompatDestination

    /** Typed destination for the feature flags list entry-point. */
    @Serializable
    data object FeatureFlagsList : CompatDestination

    /** Typed destination for the updates entry-point. */
    @Serializable
    data object Updates : CompatDestination

    /** Typed destination for the settings entry-point. */
    @Serializable
    data object Settings : CompatDestination

    /** Typed destination for the help entry-point. */
    @Serializable
    data object Help : CompatDestination

    // Feature destinations

    /** Typed destination for the calendar entry-point. */
    @Serializable
    data object Calendar : CompatDestination

    /** Destinations related to the about feature. */
    @Serializable
    sealed interface About : CompatDestination {
        /** Typed destination for the about entry-point. */
        @Serializable
        data object AppAbout : About

        /** Typed destination for viewing the app's information in the device's settings. */
        @Serializable
        data object SystemAppInfo : About

        /** Typed destination for viewing the app's source code. */
        @Serializable
        data object ViewSource : About

        /** Typed destination for viewing the app's licenses. */
        @Serializable
        data object ViewLicenses : About
    }

    /** Destinations related to the auth feature. */
    @Serializable
    sealed interface Auth : CompatDestination {
        /** Typed destination for the reset password entry-point. */
        @Serializable
        data object ResetPassword : Auth

        /** Typed destination for the account entry-point. */
        @Serializable
        data class Account(val action: AccountAction? = null) : Auth {
            @Keep
            enum class AccountAction(val ids: List<String>) {
                DeleteAccount(listOf("deleteAccount", "delete-account")),
                SignOut(listOf("signOut", "sign-out")),
                UpdateEmail(listOf("updateEmail", "update-email")),
                UpdateName(listOf("updateName", "update-name")),
                UpdatePassword(listOf("updatePassword", "update-password"));

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
                                    it.name.equals(
                                        value,
                                        ignoreCase = true
                                    ) || value in it.ids
                                }
                        }
                }
            }
        }

        /** Typed destination for the login entry-point. */
        @Serializable
        data object Login : Auth

        /** Typed destination for the register entry-point. */
        @Serializable
        data object Register : Auth
    }

    /** Destinations related to the tasks feature. */
    @Serializable
    sealed interface Task : CompatDestination {
        /** Top-level entry-point for the tasks feature. */
        @Serializable
        data object Root : Task

        /** Typed destination for the new task entry-point. */
        @Serializable
        data object New : Task

        /** Typed destination for the task list entry-point. */
        @Serializable
        data object List : Task

        interface Detail {
            val taskId: String
        }

        /** Typed destination for the edit task entry-point. */
        @Serializable
        data class Edit(override val taskId: String) : Task, Detail

        /** Typed destination for the view task entry-point. */
        @Serializable
        data class View(override val taskId: String) : Task, Detail
    }
}
