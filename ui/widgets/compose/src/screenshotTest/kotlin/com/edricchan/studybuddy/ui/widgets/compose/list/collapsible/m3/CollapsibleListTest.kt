package com.edricchan.studybuddy.ui.widgets.compose.list.collapsible.m3

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.android.tools.screenshot.PreviewTest
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme

private class CheckedParameterProvider :
    CollectionPreviewParameterProvider<Boolean>(listOf(false, true)) {
    override fun getDisplayName(index: Int): String? = when {
        index > 1 -> null
        else -> "is expanded = ${index == 1}"
    }
}

@Preview
@PreviewTest
@Composable
fun CollapsibleListPreview(@PreviewParameter(CheckedParameterProvider::class) initialExpanded: Boolean) {
    StudyBuddyTheme {
        CollapsibleListSample(
            isExpanded = initialExpanded,
            onExpansionChange = { /* no-op */ }
        )
    }
}
