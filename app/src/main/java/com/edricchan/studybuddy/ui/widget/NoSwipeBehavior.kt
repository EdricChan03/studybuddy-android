package com.edricchan.studybuddy.ui.widget

import android.view.View

import com.google.android.material.snackbar.BaseTransientBottomBar

class NoSwipeBehavior : BaseTransientBottomBar.Behavior() {
    override fun canSwipeDismissView(child: View) = false
}
