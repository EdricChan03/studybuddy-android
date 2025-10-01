package com.edricchan.studybuddy.ui.widget.prompt

import android.content.Context
import android.content.DialogInterface
import android.text.Editable
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

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

/**
 * Creates and shows a Material Design prompt dialog using the specified
 * [dialog builder configuration][builderInit], and [dialog configuration][dialogInit], if any.
 * Optionally, additional [dialog configuration][postDialogInit] can be set after the dialog is
 * shown.
 *
 * This variant disables the positive button according to the result of [isValidInput], with the
 * [value of the text-input][TextInputEditText.getText] passed to the lambda.
 * @param dialogInit Configuration to be passed to [AlertDialog] before it is shown.
 * @param postDialogInit Configuration to be passed to [AlertDialog] after it is shown, if any.
 * @param builderInit Configuration to be passed to [MaterialPromptDialogBuilder].
 * @param isValidInput Lambda used to check if the user-specified input text is valid. The dialog's
 * positive button will be enabled/disabled accordingly.
 * @param defaultIsEnabled Default enabled state if the [TextInputEditText] does not have
 * an [Editable] yet.
 * @return The shown [AlertDialog].
 * @see MaterialPromptDialogBuilder
 */
inline fun Context.showMaterialPromptDialog(
    dialogInit: AlertDialog.() -> Unit = {},
    postDialogInit: AlertDialog.() -> Unit = {},
    crossinline isValidInput: (Editable) -> Boolean = CharSequence::isNotBlank,
    defaultIsEnabled: Boolean,
    builderInit: MaterialPromptDialogBuilder.() -> Unit
): AlertDialog {
    lateinit var editText: TextInputEditText
    return showMaterialPromptDialog(
        dialogInit = dialogInit,
        postDialogInit = {
            postDialogInit()
            val okBtn =
                getButton(DialogInterface.BUTTON_POSITIVE) ?: return@showMaterialPromptDialog

            okBtn.isEnabled = editText.text?.let { isValidInput(it) } ?: defaultIsEnabled
            editText.doAfterTextChanged {
                okBtn.isEnabled = editText.text?.let { isValidInput(it) } ?: defaultIsEnabled
            }
        },
        builderInit = {
            builderInit()
            editText = textInputEditText
        }
    )
}

/**
 * Creates and shows a Material Design prompt dialog using the specified
 * [dialog builder configuration][builderInit], and [dialog configuration][dialogInit], if any.
 * Optionally, additional [dialog configuration][postDialogInit] can be set after the dialog is
 * shown.
 * @param dialogInit Configuration to be passed to [AlertDialog] before it is shown.
 * @param postDialogInit Configuration to be passed to [AlertDialog] after it is shown, if any.
 * @param textInputLayoutInit Configuration to be passed to the [TextInputLayout] if any.
 * @param textInputEditTextInit Configuration to be passed to the [TextInputEditText] if any.
 * @param builderInit Configuration to be passed to [MaterialPromptDialogBuilder].
 * @return The shown [AlertDialog].
 * @see MaterialPromptDialogBuilder
 */
inline fun Context.showMaterialPromptDialog(
    dialogInit: AlertDialog.() -> Unit = {},
    postDialogInit: AlertDialog.() -> Unit = {},
    noinline textInputLayoutInit: TextInputLayout.() -> Unit = {},
    noinline textInputEditTextInit: TextInputEditText.() -> Unit = {},
    builderInit: MaterialPromptDialogBuilder.() -> Unit,
) = showMaterialPromptDialog(dialogInit, postDialogInit) {
    builderInit()
    textInputLayout(textInputLayoutInit)
    textInputEditText(textInputEditTextInit)
}
