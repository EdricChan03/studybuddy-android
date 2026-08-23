package com.edricchan.studybuddy.features.settings.appearance.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.Check
import com.edricchan.studybuddy.core.resources.icons.outlined.Palette
import com.edricchan.studybuddy.core.settings.appearance.AppThemeSetting
import com.edricchan.studybuddy.core.settings.appearance.labelResource
import com.edricchan.studybuddy.ui.preference.compose.Preference
import com.edricchan.studybuddy.ui.preference.compose.PreferenceCategory
import com.edricchan.studybuddy.ui.preference.compose.PreferenceCategoryScope
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import com.edricchan.studybuddy.ui.theming.compose.theme.AppThemes
import com.edricchan.studybuddy.ui.theming.compose.theme.preview.StudyBuddyThemeWrapperProvider
import com.edricchan.studybuddy.core.settings.appearance.resources.R as AppearanceR

@Composable
internal fun PreferenceCategoryScope.AppThemePreference(
    modifier: Modifier = Modifier,
    themesListModifier: Modifier = Modifier,
    themes: List<AppThemeSetting> = AppThemes,
    selectedTheme: AppThemeSetting,
    onThemeChange: (AppThemeSetting) -> Unit
) = Preference(
    modifier = modifier,
    icon = { Icon(AppIcons.Outlined.Palette, contentDescription = null) },
    title = { Text(text = stringResource(AppearanceR.string.pref_app_theme_title)) },
    subtitle = {
        LazyRow(
            modifier = themesListModifier.selectableGroup(),
            contentPadding = PaddingValues(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(themes) {
                AppThemePreferenceOption(
                    theme = it,
                    selected = selectedTheme == it,
                    onClick = { onThemeChange(it) }
                )
            }
        }
    }
)

@Composable
fun AppThemePreferenceOption(
    modifier: Modifier = Modifier,
    theme: AppThemeSetting,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.width(114.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StudyBuddyTheme(
            appTheme = theme,
        ) {
            AppThemePreviewItem(
                selected = selected,
                onClick = onClick
            )
        }

        Text(
            text = stringResource(theme.labelResource),
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            maxLines = 2,
            minLines = 2,
            style = MaterialTheme.typography.bodyMedium,
        )
    }

}

// Shamelessly stolen from Tachiyomi
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppThemePreviewItem(
    selected: Boolean,
    onClick: () -> Unit,
) {
    val dividerColor by animateColorAsState(
        if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            DividerDefaults.color
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(9f / 16f)
            .border(
                width = 4.dp,
                color = dividerColor,
                shape = RoundedCornerShape(17.dp),
            )
            .padding(4.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(MaterialTheme.colorScheme.background)
            .selectable(selected = selected, onClick = onClick),
    ) {
        // App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .weight(0.7f)
                    .padding(end = 4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = MaterialTheme.shapes.small,
                    ),
            )


            Box(
                modifier = Modifier.weight(0.3f),
                contentAlignment = Alignment.CenterEnd,
            ) {
                // See https://stackoverflow.com/q/67975569
                androidx.compose.animation.AnimatedVisibility(
                    visible = selected,
                    enter = fadeIn(
                        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
                    ) + scaleIn(
                        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
                    ),
                    exit = fadeOut(
                        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
                    ) + scaleOut(
                        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
                    ),
                    label = "App theme preview selected icon visibility"
                ) {
                    Surface(
                        shape = MaterialShapes.Cookie9Sided.toShape(),
                        color = MaterialTheme.colorScheme.primary,
                    ) {
                        Icon(
                            modifier = Modifier.padding(3.dp),
                            imageVector = AppIcons.Outlined.Check,
                            contentDescription = null
                        )
                    }
                }
            }
        }


        // Items
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = false
        ) {
            items(2) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // First line
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(12.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(MaterialTheme.colorScheme.secondary)
                    )
                    // Second line
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(8.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))
                    )
                    // Third line
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(8.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))
                    )
                }
            }
        }


        // Bottom bar
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Surface(
                tonalElevation = 3.dp,
            ) {
                Row(
                    modifier = Modifier
                        .height(32.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(17.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape,
                            ),
                    )
                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .alpha(0.6f)
                            .height(17.dp)
                            .weight(1f)
                            .background(
                                color = MaterialTheme.colorScheme.onSurface,
                                shape = MaterialTheme.shapes.small,
                            ),
                    )
                }
            }
        }
    }
}

@Composable
fun AppThemePreviewSwatch(
    modifier: Modifier = Modifier,
    size: DpSize = DpSize(48.dp, 48.dp)
) {
    val primary = MaterialTheme.colorScheme.primaryContainer
    val secondary = MaterialTheme.colorScheme.secondaryContainer
    val tertiary = MaterialTheme.colorScheme.tertiaryContainer
    Canvas(modifier = modifier.size(size)) {
//        drawCircle(color = surfaceContainer)
        // Top-half
        drawArc(
            color = primary,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = true
        )
        // Bottom-left quarter
        drawArc(
            color = secondary,
            startAngle = 90f,
            sweepAngle = 90f,
            useCenter = true
        )
        // Bottom-right quarter
        drawArc(
            color = tertiary,
            startAngle = 0f,
            sweepAngle = 90f,
            useCenter = true
        )
    }
}

@Composable
fun AppThemePreviewSwatchSurface(
    modifier: Modifier = Modifier,
    swatchModifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.size(48.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = MaterialTheme.shapes.medium
    ) {
        AppThemePreviewSwatch(modifier = swatchModifier.padding(8.dp))
    }
}

@Preview
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun AppThemePreviewSwatchPreview() {
    AppThemePreviewSwatchSurface()
}

@Preview
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@PreviewLightDark
@PreviewScreenSizes
@Composable
private fun AppThemePreferencePreview() {
    var selectedTheme by rememberSaveable { mutableStateOf(AppThemeSetting.Monet) }
    PreferenceCategory {
        AppThemePreference(selectedTheme = selectedTheme) { selectedTheme = it }
    }
}

@Preview
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun AppThemePreviewItemPreview() {
    var selected by rememberSaveable { mutableStateOf(false) }
    AppThemePreviewItem(selected = selected) { selected = !selected }
}
