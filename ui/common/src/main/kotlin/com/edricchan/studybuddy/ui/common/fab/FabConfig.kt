package com.edricchan.studybuddy.ui.common.fab

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * [FloatingActionButton] configuration to be used for a particular
 * [androidx.fragment.app.Fragment].
 */
data class FabConfig(
    /**
     * The icon drawable resource for the [FloatingActionButton].
     * @see FloatingActionButton.setImageResource
     */
    @field:DrawableRes
    val iconRes: Int,
    /**
     * A string resource to the content description for the [FloatingActionButton].
     * @see FloatingActionButton.setContentDescription
     */
    @field:StringRes
    val contentDescriptionRes: Int,
    /**
     * Lambda to invoke when the [FloatingActionButton] is clicked.
     * @see FloatingActionButton.setOnClickListener
     */
    val onClick: () -> Unit,
    /**
     * How the [FloatingActionButton] should be aligned in the [BottomAppBar].
     * @see BottomAppBar.setFabAlignmentMode
     */
    @field:BottomAppBar.FabAlignmentMode
    val alignment: Int = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
)
