package com.edricchan.studybuddy.features.help.data.api

import com.edricchan.studybuddy.features.help.data.model.HelpArticle

interface HelpApi {
    /** Fetches a list of help articles. */
    suspend fun fetchHelpArticles(): List<HelpArticle>
}
