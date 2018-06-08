package com.edricchan.studybuddy;

import android.view.View;

import com.google.android.material.snackbar.BaseTransientBottomBar;

public class NoSwipeBehavior extends BaseTransientBottomBar.Behavior {

	@Override
	public boolean canSwipeDismissView(View child) {
		return false;
	}
}
