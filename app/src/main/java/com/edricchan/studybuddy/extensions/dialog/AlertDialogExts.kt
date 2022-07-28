package com.edricchan.studybuddy.extensions.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Creates an instance of [MaterialAlertDialogBuilder] using the specified [configuration][init].
 *
 * To create an alert dialog as a [androidx.appcompat.app.AlertDialog], use [createMaterialAlertDialog].
 * To show the dialog, use [showMaterialAlertDialog] instead.
 * @param init Configuration to be passed to [MaterialAlertDialogBuilder].
 * @return The created [MaterialAlertDialogBuilder].
 * @see MaterialAlertDialogBuilder
 */
fun Context.materialAlertDialogBuilder(init: MaterialAlertDialogBuilder.() -> Unit) =
    MaterialAlertDialogBuilder(this).apply(init)

/**
 * Creates a Material Design alert dialog using the specified [configuration][init].
 *
 * To show the dialog, use [showMaterialAlertDialog] instead.
 * @param init Configuration to be passed to [MaterialAlertDialogBuilder].
 * @return The created [AlertDialog].
 * @see MaterialAlertDialogBuilder
 */
fun Context.createMaterialAlertDialog(init: MaterialAlertDialogBuilder.() -> Unit) =
    materialAlertDialogBuilder(init).create()

/**
 * Creates and shows a Material Design alert dialog using the specified
 * [dialog builder configuration][builderInit], and [dialog configuration][dialogInit], if any.
 * Optionally, additional [dialog configuration][postDialogInit] can be set after the dialog is
 * shown.
 * @param dialogInit Configuration to be passed to [AlertDialog] before it is shown.
 * @param postDialogInit Configuration to be passed to [AlertDialog] after it is shown, if any.
 * @param builderInit Configuration to be passed to [MaterialAlertDialogBuilder].
 * @return The shown [AlertDialog].
 * @see MaterialAlertDialogBuilder
 */
fun Context.showMaterialAlertDialog(
    dialogInit: AlertDialog.() -> Unit = {},
    postDialogInit: AlertDialog.() -> Unit = {},
    builderInit: MaterialAlertDialogBuilder.() -> Unit
) =
    createMaterialAlertDialog(builderInit).apply(dialogInit).apply { show() }.apply(postDialogInit)
