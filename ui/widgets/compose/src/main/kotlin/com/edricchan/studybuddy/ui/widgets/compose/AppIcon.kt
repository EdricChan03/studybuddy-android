package com.edricchan.studybuddy.ui.widgets.compose

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.edricchan.studybuddy.core.resources.appIcon
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.edricchan.studybuddy.core.resources.R as CoreResR

/**
 * Composable that shows the current app's icon.
 * @param iconDrawable The app icon to display.
 */
@Composable
fun AppIcon(
    modifier: Modifier = Modifier,
    iconDrawable: Drawable = LocalContext.current.appIcon
) {
    Image(
        modifier = modifier,
        painter = rememberDrawablePainter(drawable = iconDrawable),
        contentDescription = stringResource(CoreResR.string.app_logo_content_desc)
    )
}

@Preview
@Composable
private fun AppIconPreview() {
    StudyBuddyTheme {
        AppIcon()
    }
}
