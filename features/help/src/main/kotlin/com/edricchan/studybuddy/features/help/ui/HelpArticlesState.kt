package com.edricchan.studybuddy.features.help.ui

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.edricchan.studybuddy.features.help.data.model.HelpArticle
import com.edricchan.studybuddy.features.help.data.sample.sampleHelpArticles

sealed interface HelpArticlesState {
    data object Loading : HelpArticlesState

    data class Success(val articles: List<HelpArticle>) : HelpArticlesState

    data class Error(val exception: Exception) : HelpArticlesState
}

private val stateSamples = listOf(
    HelpArticlesState.Loading to "Loading",
    HelpArticlesState.Success(sampleHelpArticles) to "Success",
    HelpArticlesState.Success(listOf()) to "Success (empty)",
    HelpArticlesState.Error(Exception("Sample error")) to "Error"
)

class HelpArticlesStatePreviewParameterProvider :
    CollectionPreviewParameterProvider<HelpArticlesState>(stateSamples.map { it.first }) {
    override fun getDisplayName(index: Int): String = stateSamples[index].second
}
