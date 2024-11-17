package com.edricchan.studybuddy.features.help.data.sample

import android.net.Uri
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.core.net.toUri
import com.edricchan.studybuddy.features.help.data.model.HelpArticle
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/** Generates a unique [Uri] for a [HelpArticle]. */
@OptIn(ExperimentalUuidApi::class)
internal fun generateHelpArticleUri(): Uri = "https://example.com/${Uuid.random()}".toUri()

private val helpArticleMeta = listOf(
    "Getting started with StudyBuddy" to "An in-depth walkthrough on getting started with the application",
    "About Task Lists" to "Learn about the new Tasks experience, where tasks are now grouped in lists",
    "Sorting & Filtering Tasks" to "Learn how to use the sorting and filtering capabilities when displaying tasks",
    "About the Calendar" to "Learn what the Calendar is, and how to schedule classes",
    "Contributing to StudyBuddy" to "Learn how to contribute back to the project"
)

/**
 * Sample [HelpArticle]s for use in Compose [Preview]s.
 * @see HelpArticlesPreviewParameter
 */
internal val sampleHelpArticles: List<HelpArticle> by lazy {
    helpArticleMeta.map { (title, desc) ->
        HelpArticle(
            title = title,
            description = desc,
            uri = generateHelpArticleUri()
        )
    }
}

/**
 * [PreviewParameterProvider] for getting a list of [HelpArticle]s.
 * @see sampleHelpArticles
 */
internal class HelpArticlesPreviewParameter :
    CollectionPreviewParameterProvider<HelpArticle>(sampleHelpArticles)
