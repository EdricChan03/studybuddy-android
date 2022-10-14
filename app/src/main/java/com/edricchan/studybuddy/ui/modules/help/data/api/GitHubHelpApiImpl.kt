package com.edricchan.studybuddy.ui.modules.help.data.api

import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.di.ContentNegotiationJsonHttpClient
import com.edricchan.studybuddy.interfaces.HelpArticle
import com.edricchan.studybuddy.interfaces.HelpArticles
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class GitHubHelpApiImpl @Inject constructor(
    @ContentNegotiationJsonHttpClient
    private val http: HttpClient
) : HelpApi {
    override suspend fun fetchHelpArticles(): List<HelpArticle> {
        return http.get(Constants.urlHelpFeatured).body<HelpArticles>().articles ?: listOf()
    }
}
