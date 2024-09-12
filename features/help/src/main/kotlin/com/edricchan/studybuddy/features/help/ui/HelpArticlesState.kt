package com.edricchan.studybuddy.features.help.ui

import com.edricchan.studybuddy.features.help.data.model.HelpArticle

sealed interface HelpArticlesState {
    data object Loading : HelpArticlesState

    data class Success(val articles: List<HelpArticle>) : HelpArticlesState

    data class Error(val exception: Exception) : HelpArticlesState
}
