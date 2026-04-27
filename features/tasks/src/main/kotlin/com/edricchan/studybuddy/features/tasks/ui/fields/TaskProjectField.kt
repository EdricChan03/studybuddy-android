package com.edricchan.studybuddy.features.tasks.ui.fields

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.Assignment
import com.edricchan.studybuddy.ui.theming.compose.theme.preview.StudyBuddyThemeWrapperProvider

@Composable
fun TaskProjectIconSurface(
    color: Color?,
    content: @Composable () -> Unit = { TaskProjectFieldDefaults.DefaultSurfaceIcon() }
) {
    Surface(
        shape = MaterialShapes.Circle.toShape(),
        color = color ?: MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        content()
    }
}

@Stable
object TaskProjectFieldDefaults {
    @Composable
    fun DefaultSurfaceIcon(
        modifier: Modifier = Modifier,
        iconPadding: PaddingValues = PaddingValues(8.dp),
        icon: ImageVector = AppIcons.Outlined.Assignment,
        contentDescription: String? = null
    ) {
        Icon(
            modifier = modifier
                .padding(iconPadding),
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}

@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Preview
@Composable
private fun TaskProjectIconSurfacePreview() {
    TaskProjectIconSurface(color = null)
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Preview
@Composable
private fun TaskProjectIconSurfaceListItemPreview() {
    ListItem(
        onClick = {},
        content = { Text(text = "StudyBuddy Compose rewrite") },
        trailingContent = {
            TaskProjectIconSurface(color = null)
        },
        shapes = ListItemDefaults.shapes()
    )
}
