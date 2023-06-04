package com.edricchan.studybuddy.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * Utility class for permission-related functions.
 * @property context The context to be used.
 */
@Deprecated("Use the appropriate extension functions instead")
class PermissionUtils(
    private val context: Context
) {
    /**
     * Checks if the specified [permission] is granted.
     * @param permission The permission to check.
     * @return `true` if the specified permission is granted, `false` otherwise
     */
    @Deprecated(
        "Use the extension function instead",
        ReplaceWith(
            "context.checkPermissionGranted(permission)",
            "com.edricchan.studybuddy.utils.checkPermissionGranted"
        )
    )
    fun checkPermissionGranted(permission: String) = context.checkPermissionGranted(permission)

    companion object {
        /**
         * Creates a new instance of the [PermissionUtils] class with the specified arguments.
         * @param context The context to be used.
         */
        @Suppress("DeprecatedCallableAddReplaceWith")
        @Deprecated("Use the appropriate extension functions instead")
        fun getInstance(context: Context) = PermissionUtils(context)
    }
}

/**
 * Checks if the specified [permission] is granted.
 * @param permission The permission to check.
 * @return `true` if the specified permission is granted, `false` otherwise
 */
fun Context.checkPermissionGranted(permission: String) = ContextCompat.checkSelfPermission(
    this,
    permission
) == PackageManager.PERMISSION_GRANTED

/**
 * Checks if the specified [permission] is denied.
 * @param permission The permission to check.
 * @return `true` if the specified permission is denied, `false` otherwise
 */
fun Context.checkPermissionDenied(permission: String) = ContextCompat.checkSelfPermission(
    this,
    permission
) == PackageManager.PERMISSION_DENIED
