package com.edricchan.studybuddy.ui.widget

import android.content.Context
import android.content.DialogInterface
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ListAdapter
import androidx.annotation.*
import androidx.appcompat.app.AlertDialog
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.utils.SharedUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout

class PromptAlertDialogBuilder(context: Context) : MaterialAlertDialogBuilder(context) {
	/**
	 * The internal dialog view
	 */
	var promptDialogView: View = LayoutInflater.from(context).inflate(R.layout.edit_text_dialog, null)
	private set
	var textInputLayout: TextInputLayout = promptDialogView.findViewById(R.id.textInputLayout)
	private set
	var textInputEditText = textInputLayout.editText
	private set
	private var TAG = SharedUtils.getTag(this::class.java)

	override fun create(): AlertDialog {
		super.setView(promptDialogView)
		return super.create()
	}

	override fun setBackground(background: Drawable): PromptAlertDialogBuilder {
		return super.setBackground(background) as PromptAlertDialogBuilder
	}

	override fun setBackgroundInsetStart(@Px backgroundInsetStart: Int): PromptAlertDialogBuilder {
		return super.setBackgroundInsetStart(backgroundInsetStart) as PromptAlertDialogBuilder
	}

	override fun setBackgroundInsetTop(@Px backgroundInsetTop: Int): PromptAlertDialogBuilder {
		return super.setBackgroundInsetTop(backgroundInsetTop) as PromptAlertDialogBuilder
	}

	override fun setBackgroundInsetEnd(@Px backgroundInsetEnd: Int): PromptAlertDialogBuilder {
		return super.setBackgroundInsetEnd(backgroundInsetEnd) as PromptAlertDialogBuilder
	}

	override fun setBackgroundInsetBottom(@Px backgroundInsetBottom: Int): PromptAlertDialogBuilder {
		return super.setBackgroundInsetBottom(backgroundInsetBottom) as PromptAlertDialogBuilder
	}

	override fun setTitle(@StringRes titleId: Int): PromptAlertDialogBuilder {
		return super.setTitle(titleId) as PromptAlertDialogBuilder
	}

	override fun setTitle(title: CharSequence?): PromptAlertDialogBuilder {
		return super.setTitle(title) as PromptAlertDialogBuilder
	}

	override fun setCustomTitle(customTitleView: View?): PromptAlertDialogBuilder {
		return super.setCustomTitle(customTitleView) as PromptAlertDialogBuilder
	}

	override fun setMessage(@StringRes messageId: Int): PromptAlertDialogBuilder {
		return super.setMessage(messageId) as PromptAlertDialogBuilder
	}

	override fun setMessage(message: CharSequence?): PromptAlertDialogBuilder {
		return super.setMessage(message) as PromptAlertDialogBuilder
	}

	override fun setIcon(@DrawableRes iconId: Int): PromptAlertDialogBuilder {
		return super.setIcon(iconId) as PromptAlertDialogBuilder
	}

	override fun setIcon(icon: Drawable?): PromptAlertDialogBuilder {
		return super.setIcon(icon) as PromptAlertDialogBuilder
	}

	override fun setIconAttribute(@AttrRes attrId: Int): PromptAlertDialogBuilder {
		return super.setIconAttribute(attrId) as PromptAlertDialogBuilder
	}

	override fun setPositiveButton(
			@StringRes textId: Int, listener: DialogInterface.OnClickListener): PromptAlertDialogBuilder {
		return super.setPositiveButton(textId, listener) as PromptAlertDialogBuilder
	}

	override fun setPositiveButton(
			text: CharSequence, listener: DialogInterface.OnClickListener): PromptAlertDialogBuilder {
		return super.setPositiveButton(text, listener) as PromptAlertDialogBuilder
	}

	override fun setPositiveButtonIcon(icon: Drawable): PromptAlertDialogBuilder {
		return super.setPositiveButtonIcon(icon) as PromptAlertDialogBuilder
	}

	override fun setNegativeButton(
			@StringRes textId: Int, listener: DialogInterface.OnClickListener): PromptAlertDialogBuilder {
		return super.setNegativeButton(textId, listener) as PromptAlertDialogBuilder
	}

	override fun setNegativeButton(
			text: CharSequence, listener: DialogInterface.OnClickListener): PromptAlertDialogBuilder {
		return super.setNegativeButton(text, listener) as PromptAlertDialogBuilder
	}

	override fun setNegativeButtonIcon(icon: Drawable): PromptAlertDialogBuilder {
		return super.setNegativeButtonIcon(icon) as PromptAlertDialogBuilder
	}

	override fun setNeutralButton(
			@StringRes textId: Int, listener: DialogInterface.OnClickListener): PromptAlertDialogBuilder {
		return super.setNeutralButton(textId, listener) as PromptAlertDialogBuilder
	}

	override fun setNeutralButton(
			text: CharSequence, listener: DialogInterface.OnClickListener): PromptAlertDialogBuilder {
		return super.setNeutralButton(text, listener) as PromptAlertDialogBuilder
	}

	override fun setNeutralButtonIcon(icon: Drawable): PromptAlertDialogBuilder {
		return super.setNeutralButtonIcon(icon) as PromptAlertDialogBuilder
	}

	override fun setCancelable(cancelable: Boolean): PromptAlertDialogBuilder {
		return super.setCancelable(cancelable) as PromptAlertDialogBuilder
	}

	override fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener): PromptAlertDialogBuilder {
		return super.setOnCancelListener(onCancelListener) as PromptAlertDialogBuilder
	}

	override fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener): PromptAlertDialogBuilder {
		return super.setOnDismissListener(onDismissListener) as PromptAlertDialogBuilder
	}

	override fun setOnKeyListener(onKeyListener: DialogInterface.OnKeyListener): PromptAlertDialogBuilder {
		return super.setOnKeyListener(onKeyListener) as PromptAlertDialogBuilder
	}

	override fun setItems(
			@ArrayRes itemsId: Int, listener: DialogInterface.OnClickListener): PromptAlertDialogBuilder {
		return super.setItems(itemsId, listener) as PromptAlertDialogBuilder
	}

	override fun setItems(items: Array<CharSequence>, listener: DialogInterface.OnClickListener): PromptAlertDialogBuilder {
		return super.setItems(items, listener) as PromptAlertDialogBuilder
	}

	override fun setAdapter(
			adapter: ListAdapter, listener: DialogInterface.OnClickListener): PromptAlertDialogBuilder {
		return super.setAdapter(adapter, listener) as PromptAlertDialogBuilder
	}

	override fun setCursor(
			cursor: Cursor, listener: DialogInterface.OnClickListener, labelColumn: String): PromptAlertDialogBuilder {
		return super.setCursor(cursor, listener, labelColumn) as PromptAlertDialogBuilder
	}

	override fun setMultiChoiceItems(
			@ArrayRes itemsId: Int, checkedItems: BooleanArray, listener: DialogInterface.OnMultiChoiceClickListener): PromptAlertDialogBuilder {
		return super.setMultiChoiceItems(itemsId, checkedItems, listener) as PromptAlertDialogBuilder
	}

	override fun setMultiChoiceItems(
			items: Array<CharSequence>, checkedItems: BooleanArray, listener: DialogInterface.OnMultiChoiceClickListener): PromptAlertDialogBuilder {
		return super.setMultiChoiceItems(items, checkedItems, listener) as PromptAlertDialogBuilder
	}

	override fun setMultiChoiceItems(
			cursor: Cursor,
			isCheckedColumn: String,
			labelColumn: String,
			listener: DialogInterface.OnMultiChoiceClickListener): PromptAlertDialogBuilder {
		return super.setMultiChoiceItems(cursor, isCheckedColumn, labelColumn, listener) as PromptAlertDialogBuilder
	}

	override fun setSingleChoiceItems(
			@ArrayRes itemsId: Int, checkedItem: Int, listener: DialogInterface.OnClickListener): PromptAlertDialogBuilder {
		return super.setSingleChoiceItems(itemsId, checkedItem, listener) as PromptAlertDialogBuilder
	}

	override fun setSingleChoiceItems(
			cursor: Cursor, checkedItem: Int, labelColumn: String, listener: DialogInterface.OnClickListener): PromptAlertDialogBuilder {
		return super.setSingleChoiceItems(cursor, checkedItem, labelColumn, listener) as PromptAlertDialogBuilder
	}

	override fun setSingleChoiceItems(
			items: Array<CharSequence>, checkedItem: Int, listener: DialogInterface.OnClickListener): PromptAlertDialogBuilder {
		return super.setSingleChoiceItems(items, checkedItem, listener) as PromptAlertDialogBuilder
	}

	override fun setSingleChoiceItems(
			adapter: ListAdapter, checkedItem: Int, listener: DialogInterface.OnClickListener): PromptAlertDialogBuilder {
		return super.setSingleChoiceItems(adapter, checkedItem, listener) as PromptAlertDialogBuilder
	}

	override fun setOnItemSelectedListener(
			listener: AdapterView.OnItemSelectedListener): PromptAlertDialogBuilder {
		return super.setOnItemSelectedListener(listener) as PromptAlertDialogBuilder
	}

	override fun setView(layoutResId: Int): PromptAlertDialogBuilder {
		return super.setView(layoutResId) as PromptAlertDialogBuilder
	}

	override fun setView(view: View): PromptAlertDialogBuilder {
		return super.setView(view) as PromptAlertDialogBuilder
	}
}