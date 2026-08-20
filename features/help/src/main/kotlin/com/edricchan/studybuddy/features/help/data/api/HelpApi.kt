package com.edricchan.studybuddy.features.help.data.api

import com.edricchan.studybuddy.features.help.data.model.HelpArticle

interface HelpApi {
    /** Fetches a list of help articles. */
    suspend fun fetchHelpArticles(): List<HelpArticle>

    /** Searches for a list of help articles that match the given [query]. */
    suspend fun searchHelpArticles(query: String): List<HelpArticle>
}
