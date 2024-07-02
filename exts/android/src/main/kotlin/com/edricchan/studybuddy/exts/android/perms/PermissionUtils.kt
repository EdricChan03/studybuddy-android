package com.edricchan.studybuddy.exts.android.perms

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

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
