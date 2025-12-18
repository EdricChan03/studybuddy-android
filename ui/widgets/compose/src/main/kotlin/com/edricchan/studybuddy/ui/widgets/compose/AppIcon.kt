package com.edricchan.studybuddy.ui.widgets.compose

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material3.Icon
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
    iconDrawable: Drawable
) {
    Image(
        modifier = modifier,
        painter = rememberDrawablePainter(iconDrawable),
        contentDescription = stringResource(CoreResR.string.app_logo_content_desc)
    )
}

/**
 * Composable that shows the current app's icon. A fallback icon is shown
 * in preview mode.
 */
@Composable
fun AppIcon(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val icon = runCatching { context.appIcon }.getOrNull()
    if (icon == null) {
        Icon(
            modifier = modifier,
            imageVector = Icons.Outlined.Build,
            contentDescription = "[INSPECTION] StudyBuddy icon"
        )
    } else {
        AppIcon(
            modifier = modifier,
            iconDrawable = icon
        )
    }
}

@Preview
@Composable
private fun AppIconPreview() {
    StudyBuddyTheme {
        AppIcon()
    }
}
