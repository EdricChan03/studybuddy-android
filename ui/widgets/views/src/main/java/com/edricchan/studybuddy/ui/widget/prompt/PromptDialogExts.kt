package com.edricchan.studybuddy.ui.widget.prompt

import android.content.Context
import androidx.appcompat.app.AlertDialog

/**
 * Creates an instance of [MaterialPromptDialogBuilder] using the specified [configuration][init].
 *
 * To create an alert dialog as a [AlertDialog], use [createMaterialPromptDialog].
 * To show the dialog, use [showMaterialPromptDialog] instead.
 * @param init Configuration to be passed to [MaterialPromptDialogBuilder].
 * @return The created [MaterialPromptDialogBuilder].
 * @see MaterialPromptDialogBuilder
 */
inline fun Context.materialPromptDialogBuilder(init: MaterialPromptDialogBuilder.() -> Unit) =
    MaterialPromptDialogBuilder(this).apply(init)

/**
 * Creates a Material Design prompt dialog using the specified [configuration][init].
 *
 * To show the dialog, use [showMaterialPromptDialog] instead.
 * @param init Configuration to be passed to [MaterialPromptDialogBuilder].
 * @return The created [AlertDialog].
 * @see MaterialPromptDialogBuilder
 */
inline fun Context.createMaterialPromptDialog(
    init: MaterialPromptDialogBuilder.() -> Unit
) = materialPromptDialogBuilder(init).create()

/**
 * Creates and shows a Material Design prompt dialog using the specified
 * [dialog builder configuration][builderInit], and [dialog configuration][dialogInit], if any.
 * Optionally, additional [dialog configuration][postDialogInit] can be set after the dialog is
 * shown.
 * @param dialogInit Configuration to be passed to [AlertDialog] before it is shown.
 * @param postDialogInit Configuration to be passed to [AlertDialog] after it is shown, if any.
 * @param builderInit Configuration to be passed to [MaterialPromptDialogBuilder].
 * @return The shown [AlertDialog].
 * @see MaterialPromptDialogBuilder
 */
inline fun Context.showMaterialPromptDialog(
    dialogInit: AlertDialog.() -> Unit = {},
    postDialogInit: AlertDialog.() -> Unit = {},
    builderInit: MaterialPromptDialogBuilder.() -> Unit
) =
    createMaterialPromptDialog(builderInit).apply(dialogInit).apply { show() }.apply(postDialogInit)
