package com.edricchan.studybuddy.features.help.data

import com.edricchan.studybuddy.features.help.data.api.HelpApi
import javax.inject.Inject

class HelpRepository @Inject constructor(
    private val helpApi: HelpApi
) {
    /** Retrieves the current list of help articles. */
    suspend fun fetchHelpArticles() = helpApi.fetchHelpArticles()

    /** Searches for a list of help articles that match the given [query]. */
    suspend fun searchHelpArticles(query: String) = helpApi.searchHelpArticles(query)
}
