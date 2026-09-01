package com.edricchan.studybuddy.features.about.ui.sections

import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.Article
import com.edricchan.studybuddy.core.resources.icons.outlined.BugReport
import com.edricchan.studybuddy.core.resources.icons.outlined.Code
import com.edricchan.studybuddy.core.resources.icons.outlined.Info
import com.edricchan.studybuddy.features.about.R
import com.edricchan.studybuddy.ui.theming.compose.theme.preview.StudyBuddyThemeWrapperProvider
import com.edricchan.studybuddy.ui.widgets.compose.list.segmented.SegmentedListColumn
import com.edricchan.studybuddy.ui.widgets.compose.list.segmented.SegmentedListItemScope

context(scope: SegmentedListItemScope)
@Composable
internal fun OpenAppInfoListItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) = SegmentedListItem(
    modifier = modifier,
    onClick = onClick,
    shapes = scope.shapes,
    colors = scope.colors,
    leadingContent = {
        Icon(AppIcons.Outlined.Info, contentDescription = null)
    },
    content = {
        Text(text = stringResource(R.string.about_open_app_info_title))
    },
    supportingContent = {
        Text(text = stringResource(R.string.about_open_app_info_subtitle))
    }
)

context(scope: SegmentedListItemScope)
@Composable
internal fun ViewSourceListItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) = SegmentedListItem(
    modifier = modifier,
    onClick = onClick,
    shapes = scope.shapes,
    colors = scope.colors,
    leadingContent = {
        Icon(AppIcons.Outlined.Code, contentDescription = null)
    },
    content = {
        Text(text = stringResource(R.string.about_view_source_code_title))
    },
    supportingContent = {
        Text(text = stringResource(R.string.about_view_source_code_subtitle))
    }
)

context(scope: SegmentedListItemScope)
@Composable
internal fun ViewBugTrackerListItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) = SegmentedListItem(
    modifier = modifier,
    onClick = onClick,
    shapes = scope.shapes,
    colors = scope.colors,
    leadingContent = {
        Icon(AppIcons.Outlined.BugReport, contentDescription = null)
    },
    content = {
        Text(text = stringResource(R.string.about_view_bug_tracker_title))
    },
    supportingContent = {
        Text(text = stringResource(R.string.about_view_bug_tracker_subtitle))
    }
)

context(scope: SegmentedListItemScope)
@Composable
internal fun ViewLicensesListItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) = SegmentedListItem(
    modifier = modifier,
    onClick = onClick,
    shapes = scope.shapes,
    colors = scope.colors,
    leadingContent = {
        Icon(AppIcons.Outlined.Article, contentDescription = null)
    },
    content = {
        Text(text = stringResource(R.string.about_source_licenses_title))
    }
)

@Composable
internal fun LinksSection(
    modifier: Modifier = Modifier,
    onAppInfoClick: () -> Unit,
    onViewSourceClick: () -> Unit,
    onViewBugTrackerClick: () -> Unit,
    onOpenSourceLicensesClick: () -> Unit
) = SegmentedListColumn(
    modifier = modifier
) {
    customItem {
        OpenAppInfoListItem(
            onClick = onAppInfoClick
        )
    }
    customItem {
        ViewSourceListItem(
            onClick = onViewSourceClick
        )
    }
    customItem {
        ViewBugTrackerListItem(
            onClick = onViewBugTrackerClick
        )
    }
    customItem {
        ViewLicensesListItem(
            onClick = onOpenSourceLicensesClick
        )
    }
}

@Preview
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun LinksSectionPreview() {
    LinksSection(
        onAppInfoClick = {},
        onViewSourceClick = {},
        onViewBugTrackerClick = {},
        onOpenSourceLicensesClick = {}
    )
}
