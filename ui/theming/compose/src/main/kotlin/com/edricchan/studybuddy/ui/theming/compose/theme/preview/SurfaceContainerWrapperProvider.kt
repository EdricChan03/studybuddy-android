package com.edricchan.studybuddy.ui.theming.compose.theme.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewWrapperProvider

/**
 * [PreviewWrapperProvider] which wraps the content in [StudyBuddyThemeWrapperProvider]
 * and a [Surface] that is themed to use the
 * [androidx.compose.material3.ColorScheme.surfaceContainer] colour role
 * as the container colour.
 */
class SurfaceContainerWrapperProvider : PreviewWrapperProvider {
    private val themeWrapper = StudyBuddyThemeWrapperProvider()

    @Composable
    override fun Wrap(content: @Composable (() -> Unit)) {
        themeWrapper.Wrap {
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainer,
                content = content
            )
        }
    }
}
