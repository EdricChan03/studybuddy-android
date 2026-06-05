package com.edricchan.studybuddy.features.help.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.Article
import com.edricchan.studybuddy.exts.androidx.compose.runtime.letComposable
import com.edricchan.studybuddy.features.help.R
import com.edricchan.studybuddy.features.help.data.model.HelpArticle
import com.edricchan.studybuddy.features.help.data.sample.sampleHelpArticles
import com.edricchan.studybuddy.ui.theming.compose.theme.preview.StudyBuddyThemeWrapperProvider
import com.edricchan.studybuddy.ui.widgets.compose.list.m3.ExpListItemDefaults

/**
 * Composable which displays a given [HelpArticle]'s metadata.
 * @param title The [HelpArticle]'s [title][HelpArticle.title].
 * @param description The [HelpArticle]'s [description][HelpArticle.description].
 * @param onClick Lambda that is invoked when the item is clicked on.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HelpArticleItem(
    modifier: Modifier = Modifier,
    shapes: ListItemShapes,
    colors: ListItemColors = ListItemDefaults.segmentedColors(),
    title: String,
    description: String? = null,
    iconShapeStartAngle: Int = 0,
    onClick: () -> Unit
) = SegmentedListItem(
    modifier = modifier,
    onClick = onClick,
    shapes = shapes,
    colors = colors,
    content = { Text(text = title) },
    supportingContent = description?.letComposable { Text(text = it) },
    leadingContent = {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = MaterialShapes.Clover8Leaf.toShape(startAngle = iconShapeStartAngle)
        ) {
            Icon(
                modifier = Modifier.padding(8.dp),
                imageVector = AppIcons.Outlined.Article,
                contentDescription = stringResource(R.string.help_article_icon_content_desc)
            )
        }
    }
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HelpArticleItem(
    modifier: Modifier = Modifier,
    shapes: ListItemShapes,
    colors: ListItemColors = ListItemDefaults.colors(),
    article: HelpArticle,
    onClick: () -> Unit
) = HelpArticleItem(
    modifier = modifier,
    shapes = shapes,
    colors = colors,
    title = article.title,
    description = article.description,
    onClick = onClick
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun HelpArticleItemPreview() {
    HelpArticleItem(
        shapes = ListItemDefaults.shapes(),
        title = "Title goes here",
        description = "Description goes here",
        onClick = {}
    )
}

@Preview
@PreviewLightDark
@PreviewDynamicColors
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun HelpArticleItemsPreview() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(ExpListItemDefaults.groupedItemsSpacing)
    ) {
        helpArticlesList(articles = sampleHelpArticles) {}
    }
}

/**
 * Displays the given list of [articles] in the receiver [LazyListScope].
 * @param itemModifier [Modifier] to be passed to [HelpArticleItem].
 * @param articles The list of articles to be displayed.
 * @param onItemClick Lambda that is invoked when an item is clicked on.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
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
        shapes = ListItemDefaults.segmentedShapes(index = i, count = articles.size),
        title = item.title,
        description = item.description,
        onClick = { onItemClick(item) }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HelpArticlesListLoadingState(
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
) {
    ContainedLoadingIndicator()
    Text(
        text = stringResource(R.string.help_articles_empty_state_loading),
        style = MaterialTheme.typography.titleLarge
    )
}

@Preview(showBackground = true)
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun HelpArticlesListLoadingStatePreview() {
    HelpArticlesListLoadingState()
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HelpArticlesListEmptyState(
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit
) = Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
) {
    Text(
        text = stringResource(R.string.help_articles_empty_state),
        style = MaterialTheme.typography.titleLarge
    )
    ElevatedButton(
        onClick = onRetryClick,
        shapes = ButtonDefaults.shapes()
    ) {
        Text(text = stringResource(R.string.help_articles_empty_state_cta_action_refresh))
    }
}

@Preview(showBackground = true)
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun HelpListEmptyStatePreview() {
    HelpArticlesListEmptyState(onRetryClick = {})
}
