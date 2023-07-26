package com.edricchan.studybuddy.ui.widget.prompt

import android.content.Context
import android.content.DialogInterface
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ListAdapter
import androidx.annotation.ArrayRes
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.edricchan.studybuddy.ui.widget.databinding.EditTextDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MaterialPromptDialogBuilder(context: Context) : MaterialAlertDialogBuilder(context) {
    private val binding = EditTextDialogBinding.inflate(
        LayoutInflater.from(context)
    )

    /** The dialog view. */
    val promptDialogView = binding.root

    /** The inner [TextInputLayout]. */
    val textInputLayout = binding.textInputLayout

    /** Configures the inner [TextInputLayout]. */
    fun textInputLayout(init: TextInputLayout.() -> Unit) {
        textInputLayout.apply(init)
    }

    /** The inner [TextInputEditText]. */
    val textInputEditText = binding.textInputEditText

    /** Configures the inner [TextInputEditText]. */
    fun textInputEditText(init: TextInputEditText.() -> Unit) {
        textInputEditText.apply(init)
    }

    /** Retrieves the [TextInputEditText]'s text. */
    val promptText get() = textInputEditText.text

    /** Retrieves the [TextInputEditText]'s text as a string. */
    val promptTextStr get() = textInputEditText.text?.toString()

    override fun create(): AlertDialog {
        super.setView(binding.root)
        return super.create()
    }

    override fun setBackground(background: Drawable?): MaterialPromptDialogBuilder {
        return super.setBackground(background) as MaterialPromptDialogBuilder
    }

    override fun setBackgroundInsetStart(@Px backgroundInsetStart: Int): MaterialPromptDialogBuilder {
        return super.setBackgroundInsetStart(backgroundInsetStart) as MaterialPromptDialogBuilder
    }

    override fun setBackgroundInsetTop(@Px backgroundInsetTop: Int): MaterialPromptDialogBuilder {
        return super.setBackgroundInsetTop(backgroundInsetTop) as MaterialPromptDialogBuilder
    }

    override fun setBackgroundInsetEnd(@Px backgroundInsetEnd: Int): MaterialPromptDialogBuilder {
        return super.setBackgroundInsetEnd(backgroundInsetEnd) as MaterialPromptDialogBuilder
    }

    override fun setBackgroundInsetBottom(@Px backgroundInsetBottom: Int): MaterialPromptDialogBuilder {
        return super.setBackgroundInsetBottom(backgroundInsetBottom) as MaterialPromptDialogBuilder
    }

    override fun setTitle(@StringRes titleId: Int): MaterialPromptDialogBuilder {
        return super.setTitle(titleId) as MaterialPromptDialogBuilder
    }

    override fun setTitle(title: CharSequence?): MaterialPromptDialogBuilder {
        return super.setTitle(title) as MaterialPromptDialogBuilder
    }

    override fun setCustomTitle(customTitleView: View?): MaterialPromptDialogBuilder {
        return super.setCustomTitle(customTitleView) as MaterialPromptDialogBuilder
    }

    override fun setMessage(@StringRes messageId: Int): MaterialPromptDialogBuilder {
        return super.setMessage(messageId) as MaterialPromptDialogBuilder
    }

    override fun setMessage(message: CharSequence?): MaterialPromptDialogBuilder {
        return super.setMessage(message) as MaterialPromptDialogBuilder
    }

    override fun setIcon(@DrawableRes iconId: Int): MaterialPromptDialogBuilder {
        return super.setIcon(iconId) as MaterialPromptDialogBuilder
    }

    override fun setIcon(icon: Drawable?): MaterialPromptDialogBuilder {
        return super.setIcon(icon) as MaterialPromptDialogBuilder
    }

    override fun setIconAttribute(@AttrRes attrId: Int): MaterialPromptDialogBuilder {
        return super.setIconAttribute(attrId) as MaterialPromptDialogBuilder
    }

    override fun setPositiveButton(
        @StringRes textId: Int, listener: DialogInterface.OnClickListener?
    ): MaterialPromptDialogBuilder {
        return super.setPositiveButton(textId, listener) as MaterialPromptDialogBuilder
    }

    override fun setPositiveButton(
        text: CharSequence?, listener: DialogInterface.OnClickListener?
    ): MaterialPromptDialogBuilder {
        return super.setPositiveButton(text, listener) as MaterialPromptDialogBuilder
    }

    override fun setPositiveButtonIcon(icon: Drawable?): MaterialPromptDialogBuilder {
        return super.setPositiveButtonIcon(icon) as MaterialPromptDialogBuilder
    }

    override fun setNegativeButton(
        @StringRes textId: Int, listener: DialogInterface.OnClickListener?
    ): MaterialPromptDialogBuilder {
        return super.setNegativeButton(textId, listener) as MaterialPromptDialogBuilder
    }

    override fun setNegativeButton(
        text: CharSequence?, listener: DialogInterface.OnClickListener?
    ): MaterialPromptDialogBuilder {
        return super.setNegativeButton(text, listener) as MaterialPromptDialogBuilder
    }

    override fun setNegativeButtonIcon(icon: Drawable?): MaterialPromptDialogBuilder {
        return super.setNegativeButtonIcon(icon) as MaterialPromptDialogBuilder
    }

    override fun setNeutralButton(
        @StringRes textId: Int, listener: DialogInterface.OnClickListener?
    ): MaterialPromptDialogBuilder {
        return super.setNeutralButton(textId, listener) as MaterialPromptDialogBuilder
    }

    override fun setNeutralButton(
        text: CharSequence?, listener: DialogInterface.OnClickListener?
    ): MaterialPromptDialogBuilder {
        return super.setNeutralButton(text, listener) as MaterialPromptDialogBuilder
    }

    override fun setNeutralButtonIcon(icon: Drawable?): MaterialPromptDialogBuilder {
        return super.setNeutralButtonIcon(icon) as MaterialPromptDialogBuilder
    }

    override fun setCancelable(cancelable: Boolean): MaterialPromptDialogBuilder {
        return super.setCancelable(cancelable) as MaterialPromptDialogBuilder
    }

    override fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener?): MaterialPromptDialogBuilder {
        return super.setOnCancelListener(onCancelListener) as MaterialPromptDialogBuilder
    }

    override fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener?): MaterialPromptDialogBuilder {
        return super.setOnDismissListener(onDismissListener) as MaterialPromptDialogBuilder
    }

    override fun setOnKeyListener(onKeyListener: DialogInterface.OnKeyListener?): MaterialPromptDialogBuilder {
        return super.setOnKeyListener(onKeyListener) as MaterialPromptDialogBuilder
    }

    override fun setItems(
        @ArrayRes itemsId: Int, listener: DialogInterface.OnClickListener?
    ): MaterialPromptDialogBuilder {
        return super.setItems(itemsId, listener) as MaterialPromptDialogBuilder
    }

    override fun setItems(
        items: Array<CharSequence>?,
        listener: DialogInterface.OnClickListener?
    ): MaterialPromptDialogBuilder {
        return super.setItems(items, listener) as MaterialPromptDialogBuilder
    }

    override fun setAdapter(
        adapter: ListAdapter?, listener: DialogInterface.OnClickListener?
    ): MaterialPromptDialogBuilder {
        return super.setAdapter(adapter, listener) as MaterialPromptDialogBuilder
    }

    override fun setCursor(
        cursor: Cursor?, listener: DialogInterface.OnClickListener?, labelColumn: String
    ): MaterialPromptDialogBuilder {
        return super.setCursor(cursor, listener, labelColumn) as MaterialPromptDialogBuilder
    }

    override fun setMultiChoiceItems(
        @ArrayRes itemsId: Int, checkedItems: BooleanArray?,
        listener: DialogInterface.OnMultiChoiceClickListener?
    ): MaterialPromptDialogBuilder {
        return super.setMultiChoiceItems(
            itemsId,
            checkedItems,
            listener
        ) as MaterialPromptDialogBuilder
    }

    override fun setMultiChoiceItems(
        items: Array<CharSequence>?,
        checkedItems: BooleanArray?,
        listener: DialogInterface.OnMultiChoiceClickListener?
    ): MaterialPromptDialogBuilder {
        return super.setMultiChoiceItems(
            items,
            checkedItems,
            listener
        ) as MaterialPromptDialogBuilder
    }

    override fun setMultiChoiceItems(
        cursor: Cursor?,
        isCheckedColumn: String,
        labelColumn: String,
        listener: DialogInterface.OnMultiChoiceClickListener?
    ): MaterialPromptDialogBuilder {
        return super.setMultiChoiceItems(
            cursor,
            isCheckedColumn,
            labelColumn,
            listener
        ) as MaterialPromptDialogBuilder
    }

    override fun setSingleChoiceItems(
        @ArrayRes itemsId: Int, checkedItem: Int, listener: DialogInterface.OnClickListener?
    ): MaterialPromptDialogBuilder {
        return super.setSingleChoiceItems(
            itemsId,
            checkedItem,
            listener
        ) as MaterialPromptDialogBuilder
    }

    override fun setSingleChoiceItems(
        cursor: Cursor?,
        checkedItem: Int,
        labelColumn: String,
        listener: DialogInterface.OnClickListener?
    ): MaterialPromptDialogBuilder {
        return super.setSingleChoiceItems(
            cursor,
            checkedItem,
            labelColumn,
            listener
        ) as MaterialPromptDialogBuilder
    }

    override fun setSingleChoiceItems(
        items: Array<CharSequence>?, checkedItem: Int, listener: DialogInterface.OnClickListener?
    ): MaterialPromptDialogBuilder {
        return super.setSingleChoiceItems(
            items,
            checkedItem,
            listener
        ) as MaterialPromptDialogBuilder
    }

    override fun setSingleChoiceItems(
        adapter: ListAdapter?, checkedItem: Int, listener: DialogInterface.OnClickListener?
    ): MaterialPromptDialogBuilder {
        return super.setSingleChoiceItems(
            adapter,
            checkedItem,
            listener
        ) as MaterialPromptDialogBuilder
    }

    override fun setOnItemSelectedListener(
        listener: AdapterView.OnItemSelectedListener?
    ): MaterialPromptDialogBuilder {
        return super.setOnItemSelectedListener(listener) as MaterialPromptDialogBuilder
    }

    override fun setView(layoutResId: Int): MaterialPromptDialogBuilder {
        return super.setView(layoutResId) as MaterialPromptDialogBuilder
    }

    override fun setView(view: View?): MaterialPromptDialogBuilder {
        return super.setView(view) as MaterialPromptDialogBuilder
    }
}
