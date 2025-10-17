package com.edricchan.studybuddy.ui.widgets.compose.markdown

import androidx.compose.material3.DividerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import com.boswelja.markdown.material3.MarkdownDocument
import com.boswelja.markdown.material3.m3RuleStyle

@Composable
fun MarkdownViewer(
    modifier: Modifier = Modifier,
    markdownText: String,
    onLinkClick: ((LinkAnnotation) -> Unit)? = null
) = MarkdownDocument(
    modifier = modifier,
    markdown = markdownText,
    onLinkClick = onLinkClick,
    // TODO: Remove manual rule style when
    //  https://github.com/boswelja/compose-markdown/pull/100
    //  is merged
    ruleStyle = m3RuleStyle(
        thickness = DividerDefaults.Thickness
    )
)
