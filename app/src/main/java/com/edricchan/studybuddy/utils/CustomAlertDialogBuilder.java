package com.edricchan.studybuddy.utils;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

// See https://stackoverflow.com/a/9523257
public class CustomAlertDialogBuilder extends AlertDialog.Builder {
	/**
	 * Click listeners
	 */
	private DialogInterface.OnClickListener mPositiveButtonListener = null;
	private DialogInterface.OnClickListener mNegativeButtonListener = null;
	private DialogInterface.OnClickListener mNeutralButtonListener = null;

	/**
	 * Buttons text
	 */
	private CharSequence mPositiveButtonText = null;
	private CharSequence mNegativeButtonText = null;
	private CharSequence mNeutralButtonText = null;

	private DialogInterface.OnDismissListener mOnDismissListener = null;

	private Boolean mCancelOnTouchOutside = null;

	public CustomAlertDialogBuilder(Context context) {
		super(context);
	}

	public CustomAlertDialogBuilder setOnDismissListener(DialogInterface.OnDismissListener listener) {
		mOnDismissListener = listener;
		return this;
	}

	@Override
	public CustomAlertDialogBuilder setNegativeButton(CharSequence text, DialogInterface.OnClickListener listener) {
		mNegativeButtonListener = listener;
		mNegativeButtonText = text;
		return this;
	}

	@Override
	public CustomAlertDialogBuilder setNeutralButton(CharSequence text, DialogInterface.OnClickListener listener) {
		mNeutralButtonListener = listener;
		mNeutralButtonText = text;
		return this;
	}

	@Override
	public CustomAlertDialogBuilder setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener) {
		mPositiveButtonListener = listener;
		mPositiveButtonText = text;
		return this;
	}

	@Override
	public CustomAlertDialogBuilder setNegativeButton(int textId, DialogInterface.OnClickListener listener) {
		setNegativeButton(getContext().getString(textId), listener);
		return this;
	}

	@Override
	public CustomAlertDialogBuilder setNeutralButton(int textId, DialogInterface.OnClickListener listener) {
		setNeutralButton(getContext().getString(textId), listener);
		return this;
	}

	@Override
	public CustomAlertDialogBuilder setPositiveButton(int textId, DialogInterface.OnClickListener listener) {
		setPositiveButton(getContext().getString(textId), listener);
		return this;
	}

	public CustomAlertDialogBuilder setCanceledOnTouchOutside(boolean cancelOnTouchOutside) {
		mCancelOnTouchOutside = cancelOnTouchOutside;
		return this;
	}


	@Override
	public AlertDialog create() {
		throw new UnsupportedOperationException("CustomAlertDialogBuilder.create(): use show() instead..");
	}

	@Override
	public AlertDialog show() {
		final AlertDialog alertDialog = super.create();

		DialogInterface.OnClickListener emptyOnClickListener = (dialog, which) -> {
		};


		// Enable buttons (needed for Android 1.6) - otherwise later getButton() returns null
		if (mPositiveButtonText != null) {
			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, mPositiveButtonText, emptyOnClickListener);
		}

		if (mNegativeButtonText != null) {
			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, mNegativeButtonText, emptyOnClickListener);
		}

		if (mNeutralButtonText != null) {
			alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, mNeutralButtonText, emptyOnClickListener);
		}

		// Set OnDismissListener if available
		if (mOnDismissListener != null) {
			alertDialog.setOnDismissListener(mOnDismissListener);
		}

		if (mCancelOnTouchOutside != null) {
			alertDialog.setCanceledOnTouchOutside(mCancelOnTouchOutside);
		}

		alertDialog.show();

		// Set the OnClickListener directly on the Button object, avoiding the auto-dismiss feature
		// IMPORTANT: this must be after alert.show(), otherwise the button doesn't exist..
		// If the listeners are null don't do anything so that they will still dismiss the dialog when clicked
		if (mPositiveButtonListener != null) {
			alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> mPositiveButtonListener.onClick(alertDialog, AlertDialog.BUTTON_POSITIVE));
		}

		if (mNegativeButtonListener != null) {
			alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v -> mNegativeButtonListener.onClick(alertDialog, AlertDialog.BUTTON_NEGATIVE));
		}

		if (mNeutralButtonListener != null) {
			alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> mNeutralButtonListener.onClick(alertDialog, AlertDialog.BUTTON_NEUTRAL));
		}

		return alertDialog;
	}
}
