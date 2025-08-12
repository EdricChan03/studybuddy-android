package com.edricchan.studybuddy.features.help.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.exts.androidx.compose.runtime.letComposable
import com.edricchan.studybuddy.features.help.R
import com.edricchan.studybuddy.features.help.data.model.HelpArticle
import com.edricchan.studybuddy.features.help.data.sample.sampleHelpArticles
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import com.edricchan.studybuddy.ui.widgets.compose.list.m3.ExpListItem
import com.edricchan.studybuddy.ui.widgets.compose.list.m3.ExpListItemDefaults

/**
 * Composable which displays a given [HelpArticle]'s metadata.
 * @param title The [HelpArticle]'s [title][HelpArticle.title].
 * @param description The [HelpArticle]'s [description][HelpArticle.description].
 * @param onClick Lambda that is invoked when the item is clicked on.
 */
@Composable
fun HelpArticleItem(
    modifier: Modifier = Modifier,
    shape: Shape,
    title: String,
    description: String? = null,
    onClick: () -> Unit
) = ExpListItem(
    modifier = modifier,
    onClick = onClick,
    shape = shape,
    headlineContent = { Text(text = title) },
    supportingContent = description?.letComposable { Text(text = it) },
    leadingContent = {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = CircleShape
        ) {
            Icon(
                modifier = Modifier.padding(8.dp),
                painter = painterResource(R.drawable.ic_file_document_box_outline_24dp),
                contentDescription = stringResource(R.string.help_article_icon_content_desc)
            )
        }
    }
)

@Composable
fun HelpArticleItem(
    modifier: Modifier = Modifier,
    shape: Shape,
    article: HelpArticle,
    onClick: () -> Unit
) = HelpArticleItem(
    modifier = modifier,
    shape = shape,
    title = article.title,
    description = article.description,
    onClick = onClick
)

@Preview
@Composable
private fun HelpArticleItemPreview() {
    StudyBuddyTheme {
        HelpArticleItem(
            shape = ExpListItemDefaults.baseShape,
            title = "Title goes here",
            description = "Description goes here",
            onClick = {}
        )
    }
}

@Preview
@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun HelpArticleItemsPreview() {
    StudyBuddyTheme {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(ExpListItemDefaults.groupedItemsSpacing)
        ) {
            helpArticlesList(articles = sampleHelpArticles) {}
        }
    }
}

/**
 * Displays the given list of [articles] in the receiver [LazyListScope].
 * @param itemModifier [Modifier] to be passed to [HelpArticleItem].
 * @param articles The list of articles to be displayed.
 * @param onItemClick Lambda that is invoked when an item is clicked on.
 */
fun LazyListScope.helpArticlesList(
    itemModifier: Modifier = Modifier,
    articles: List<HelpArticle>,
    onItemClick: (HelpArticle) -> Unit
) = itemsIndexed(
    items = articles,
    key = { _, item -> item.uri }
) { i, item ->
    HelpArticleItem(
        modifier = itemModifier.animateItem(),
        shape = ExpListItemDefaults.itemShape(i, articles.size),
        title = item.title,
        description = item.description,
        onClick = { onItemClick(item) }
    )
}
