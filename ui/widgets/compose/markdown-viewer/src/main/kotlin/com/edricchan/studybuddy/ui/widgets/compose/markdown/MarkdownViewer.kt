package com.edricchan.studybuddy.ui.widgets.compose.markdown

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkInteractionListener
import com.boswelja.markdown.material3.MarkdownDocument
import com.boswelja.markdown.material3.m3TextStyleModifiers

@Composable
fun MarkdownViewer(
    modifier: Modifier = Modifier,
    markdownText: String,
    linkContentColor: Color = MaterialTheme.colorScheme.primary,
    linkInteractionListener: LinkInteractionListener? = null
) = MarkdownDocument(
    modifier = modifier,
    markdown = markdownText,
    linkInteractionListener = linkInteractionListener,
    textStyleModifiers = m3TextStyleModifiers(
        linkColor = linkContentColor
    )
)
