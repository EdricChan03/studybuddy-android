package com.edricchan.studybuddy.core.resources.icons.animated

import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.runtime.Composable
import com.edricchan.studybuddy.core.resources.icons.AppIcons

/**
 * [AnimatedImageVector] of a visibility toggle using the appropriate
 * outlined icons.
 * @param isVisible Whether the representative state is visible. This determines what
 * animation should be played when the [AnimatedImageVector]'s state is toggled.
 */
@Suppress("UnusedReceiverParameter")
@Composable
fun AppIcons.Outlined.VisibilityToggle(isVisible: Boolean): AnimatedImageVector =
    AnimatedImageVector.animatedVectorResource(
        if (isVisible) R.drawable.app_avd_show_password
        else R.drawable.app_avd_hide_password
    )
