package com.edricchan.studybuddy.ui.common.about

import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuGroup
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenuPopup
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.SplitButtonLayout
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.window.core.layout.WindowSizeClass
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.Build
import com.edricchan.studybuddy.core.resources.icons.outlined.ContentCopy
import com.edricchan.studybuddy.core.resources.icons.outlined.KeyboardArrowDown
import com.edricchan.studybuddy.core.resources.icons.outlined.MarkdownCopy
import com.edricchan.studybuddy.core.resources.icons.outlined.OpenInNew
import com.edricchan.studybuddy.core.resources.metadata.StudyBuddyMetadata
import com.edricchan.studybuddy.core.resources.temporal.appFormat
import com.edricchan.studybuddy.exts.android.metadata.appIcon
import com.edricchan.studybuddy.exts.android.metadata.appLabel
import com.edricchan.studybuddy.exts.android.metadata.versionCode
import com.edricchan.studybuddy.exts.android.metadata.versionName
import com.edricchan.studybuddy.exts.datetime.toLocalDateTime
import com.edricchan.studybuddy.ui.common.R
import com.edricchan.studybuddy.ui.theming.compose.theme.preview.StudyBuddyThemeWrapperProvider
import com.edricchan.studybuddy.ui.widgets.compose.AppIcon
import java.time.Instant
import kotlin.time.Clock

@Composable
fun VersionDialog(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    buildTime: Instant? = StudyBuddyMetadata.BuildTime,
    gitCommitSha: String? = StudyBuddyMetadata.GitCommitSha,
    onDismissRequest: () -> Unit,
    onViewCommitClick: () -> Unit,
    onRequestCopyPlainClick: () -> Unit,
    onRequestCopyMarkdownClick: () -> Unit,
) {
    val appIcon = remember(context, context::appIcon)
    val appName = remember(context, context::appLabel)
    val packageName = remember(context, context::getPackageName)
    val versionName = remember(context, context::versionName)
    val versionCode = remember(context, context::versionCode)

    VersionDialog(
        modifier = modifier,
        appName = appName.toString(),
        appIcon = {
            AppIcon(
                iconDrawable = appIcon
            )
        },
        packageName = packageName,
        versionName = versionName.orEmpty(),
        versionCode = versionCode,
        buildTime = buildTime,
        gitCommitSha = gitCommitSha,
        onDismissRequest = onDismissRequest,
        onViewCommitClick = onViewCommitClick,
        onRequestCopyPlainClick = onRequestCopyPlainClick,
        onRequestCopyMarkdownClick = onRequestCopyMarkdownClick
    )
}

@Composable
fun VersionDialog(
    modifier: Modifier = Modifier,
    appName: String,
    appIcon: @Composable BoxScope.() -> Unit,
    packageName: String,
    versionName: String,
    versionCode: Long,
    buildTime: Instant?,
    gitCommitSha: String?,
    onDismissRequest: () -> Unit,
    onViewCommitClick: () -> Unit,
    onRequestCopyPlainClick: () -> Unit,
    onRequestCopyMarkdownClick: () -> Unit
) = BasicAlertDialog(
    modifier = modifier.padding(horizontal = 16.dp),
    onDismissRequest = onDismissRequest,
    properties = DialogProperties(
        usePlatformDefaultWidth = false
    )
) {
    VersionDialogContent(
        modifier = Modifier.animateContentSize(),
        appName = appName,
        appIcon = appIcon,
        packageName = packageName,
        versionName = versionName,
        versionCode = versionCode,
        buildTime = buildTime?.toLocalDateTime()?.appFormat(),
        gitCommitSha = gitCommitSha,
        onViewCommitClick = onViewCommitClick,
        onRequestCopyPlainClick = onRequestCopyPlainClick,
        onRequestCopyMarkdownClick = onRequestCopyMarkdownClick
    )
}

@Composable
private fun VersionText(
    modifier: Modifier = Modifier,
    versionName: String,
    versionCode: Long
) = Text(
    modifier = modifier,
    text = stringResource(
        R.string.version_dialog_version_formatted,
        versionName,
        versionCode
    )
)

@Composable
private fun VersionDialogVersionMetadata(
    modifier: Modifier = Modifier,
    packageName: String,
    versionName: String,
    versionCode: Long,
    buildTime: String?,
    gitCommitSha: String?
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        SelectionContainer {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = packageName)
                VersionText(
                    versionName = versionName,
                    versionCode = versionCode
                )
                buildTime?.let {
                    Text(
                        text = stringResource(R.string.version_dialog_build_time_formatted, it),
                    )
                }
                gitCommitSha?.let {
                    Text(
                        text = stringResource(
                            R.string.version_dialog_commit_formatted,
                            it.take(7)
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun VersionDialogContent(
    modifier: Modifier = Modifier,
    appName: String,
    appIcon: @Composable BoxScope.() -> Unit = {},
    packageName: String,
    versionName: String,
    versionCode: Long,
    buildTime: String?,
    gitCommitSha: String? = null,
    onViewCommitClick: () -> Unit,
    onRequestCopyPlainClick: () -> Unit,
    onRequestCopyMarkdownClick: () -> Unit
) {
    val adaptiveInfo = currentWindowAdaptiveInfoV2()
    val isCompactWidth =
        !adaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)

    val mainContent = remember {
        movableContentOf {
            Box(modifier = Modifier.defaultMinSize(minWidth = 64.dp, minHeight = 64.dp)) {
                appIcon()
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = appName,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center.takeIf { isCompactWidth }
                )
                VersionDialogVersionMetadata(
                    modifier = Modifier.fillMaxWidth(),
                    packageName = packageName,
                    versionName = versionName,
                    versionCode = versionCode,
                    buildTime = buildTime,
                    gitCommitSha = gitCommitSha
                )
            }
        }
    }

    Surface(
        modifier = modifier,
        shape = AlertDialogDefaults.shape
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            if (isCompactWidth) {
                Column(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    mainContent()
                }
            } else {
                Row(
                    modifier = Modifier.weight(1f, fill = false),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    mainContent()
                }
            }
            VersionDialogActions(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                gitCommitSha = gitCommitSha,
                onRequestCopyPlainClick = onRequestCopyPlainClick,
                onRequestCopyMarkdownClick = onRequestCopyMarkdownClick,
                onViewCommitClick = onViewCommitClick,
                useFullWidthButtons = isCompactWidth
            )
        }
    }
}

@Composable
private fun CopySplitButton(
    modifier: Modifier = Modifier,
    useFullWidthLeadingButton: Boolean,
    onRequestCopyPlainClick: () -> Unit,
    onRequestCopyMarkdownClick: () -> Unit,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    val dismissMenu = { isExpanded = false }

    Box {
        SplitButtonLayout(
            modifier = modifier,
            leadingButton = {
                SplitButtonDefaults.OutlinedLeadingButton(
                    modifier = if (useFullWidthLeadingButton) Modifier.fillMaxWidth() else Modifier,
                    onClick = onRequestCopyPlainClick
                ) {
                    Icon(
                        modifier = Modifier.size(SplitButtonDefaults.LeadingIconSize),
                        imageVector = AppIcons.Outlined.ContentCopy,
                        contentDescription = null
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = stringResource(R.string.version_dialog_copy_info_btn_text))
                }
            },
            trailingButton = {
                val stateText = stringResource(
                    if (isExpanded) R.string.version_dialog_copy_info_options_state_shown_desc
                    else R.string.version_dialog_copy_info_options_state_hidden_desc
                )
                val contentDesc = stringResource(
                    if (isExpanded) R.string.version_dialog_hide_copy_info_options_content_desc
                    else R.string.version_dialog_show_copy_info_options_content_desc
                )

                SplitButtonDefaults.OutlinedTrailingButton(
                    modifier = Modifier.semantics {
                        stateDescription = stateText
                    },
                    checked = isExpanded,
                    onCheckedChange = { isExpanded = it }
                ) {
                    val rotation by animateFloatAsState(
                        targetValue = if (isExpanded) 180f else 0f,
                        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
                        label = "Copy split trailing icon rotation",
                    )
                    Icon(
                        AppIcons.Outlined.KeyboardArrowDown,
                        modifier = Modifier
                            .size(SplitButtonDefaults.TrailingIconSize)
                            .rotate(rotation),
                        contentDescription = contentDesc,
                    )
                }
            }
        )

        DropdownMenuPopup(
            expanded = isExpanded,
            onDismissRequest = dismissMenu
        ) {
            DropdownMenuGroup(
                shapes = MenuDefaults.groupShapes()
            ) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.version_dialog_copy_plain_info_btn_text)) },
                    leadingIcon = {
                        Icon(
                            AppIcons.Outlined.ContentCopy,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        onRequestCopyPlainClick()
                        dismissMenu()
                    },
                    shape = MenuDefaults.leadingItemShape
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.version_dialog_copy_plain_markdown_btn_text)) },
                    leadingIcon = {
                        Icon(
                            AppIcons.Outlined.MarkdownCopy,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        onRequestCopyMarkdownClick()
                        dismissMenu()
                    },
                    shape = MenuDefaults.trailingItemShape
                )
            }
        }
    }
}

@Composable
private fun VersionDialogActions(
    modifier: Modifier = Modifier,
    onRequestCopyPlainClick: () -> Unit,
    onRequestCopyMarkdownClick: () -> Unit,
    gitCommitSha: String?,
    onViewCommitClick: () -> Unit,
    useFullWidthButtons: Boolean
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CopySplitButton(
            useFullWidthLeadingButton = useFullWidthButtons,
            onRequestCopyPlainClick = onRequestCopyPlainClick,
            onRequestCopyMarkdownClick = onRequestCopyMarkdownClick
        )
        gitCommitSha?.let {
            FilledTonalButton(
                modifier = if (useFullWidthButtons) Modifier.fillMaxWidth() else Modifier,
                onClick = onViewCommitClick,
                shapes = ButtonDefaults.shapes()
            ) {
                Text(
                    text = stringResource(
                        R.string.version_dialog_view_commit_btn_text
                    )
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Icon(
                    modifier = Modifier.size(ButtonDefaults.IconSize),
                    imageVector = AppIcons.Outlined.OpenInNew,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@PreviewFontScale
@PreviewDynamicColors
@PreviewLightDark
@PreviewScreenSizes
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun VersionDialogContentPreview() {
    VersionDialogContent(
        appName = "StudyBuddy",
        packageName = "com.example.studybuddy",
        appIcon = {
            Icon(
                modifier = Modifier.matchParentSize(),
                imageVector = AppIcons.Outlined.Build,
                contentDescription = null
            )
        },
        versionName = "1.2.3",
        versionCode = 123,
        buildTime = Clock.System.now().toString(),
        onViewCommitClick = {},
        onRequestCopyPlainClick = {},
        onRequestCopyMarkdownClick = {}
    )
}

@Preview
@PreviewFontScale
@PreviewDynamicColors
@PreviewLightDark
@PreviewScreenSizes
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun VersionDialogContentWithShaPreview() {
    VersionDialogContent(
        appName = "StudyBuddy",
        packageName = "com.example.studybuddy",
        appIcon = {
            Icon(
                modifier = Modifier.matchParentSize(),
                imageVector = AppIcons.Outlined.Build,
                contentDescription = null
            )
        },
        versionName = "1.2.3",
        versionCode = 123,
        buildTime = Clock.System.now().toString(),
        gitCommitSha = "22ea3f88a45c29409ce239bc94ff195e6c37df99",
        onViewCommitClick = {},
        onRequestCopyPlainClick = {},
        onRequestCopyMarkdownClick = {}
    )
}
