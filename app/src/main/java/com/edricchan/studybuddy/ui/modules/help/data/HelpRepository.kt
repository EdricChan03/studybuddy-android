package com.edricchan.studybuddy.ui.modules.help.data

import com.edricchan.studybuddy.ui.modules.help.data.api.HelpApi
import javax.inject.Inject

class HelpRepository @Inject constructor(
    private val helpApi: HelpApi
) {
    /** Retrieves the current list of help articles. */
    suspend fun fetchHelpArticles() = helpApi.fetchHelpArticles()
}
