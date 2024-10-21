package com.edricchan.studybuddy.exts.material.bottomsheet

import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.updateLayoutParams
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Applies the ability for the user to expand the receiver
 * [BottomSheetDialogFragment]'s contents to be full-screen.
 */
fun BottomSheetDialogFragment.applyExpansion() {
    (dialog as BottomSheetDialog).findViewById<ViewGroup>(
        com.google.android.material.R.id.design_bottom_sheet
    )?.updateLayoutParams {
        height = WindowManager.LayoutParams.MATCH_PARENT
    }
}
