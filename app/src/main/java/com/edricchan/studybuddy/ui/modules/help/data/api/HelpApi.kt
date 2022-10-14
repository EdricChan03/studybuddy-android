package com.edricchan.studybuddy.ui.modules.help.data.api

import com.edricchan.studybuddy.interfaces.HelpArticle

interface HelpApi {
    /** Fetches a list of help articles. */
    suspend fun fetchHelpArticles(): List<HelpArticle>
}
