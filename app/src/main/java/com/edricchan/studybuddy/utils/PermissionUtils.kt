package com.edricchan.studybuddy.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * Utility class for permission-related functions.
 * @property context The context to be used.
 */
class PermissionUtils(
    val context: Context
) {
    /**
     * Checks if the specified [permission] is granted.
     * @param permission The permission to check.
     * @return `true` if the specified permission is granted, `false` otherwise
     */
    fun checkPermissionGranted(permission: String) = ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED

    companion object {
        /**
         * Creates a new instance of the [PermissionUtils] class with the specified arguments.
         * @param context The context to be used.
         */
        fun getInstance(context: Context) = PermissionUtils(context)
    }
}