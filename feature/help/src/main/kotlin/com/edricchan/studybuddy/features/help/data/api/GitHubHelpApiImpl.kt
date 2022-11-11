package com.edricchan.studybuddy.features.help.data.api

import com.edricchan.studybuddy.core.di.ContentNegotiationJsonHttpClient
import com.edricchan.studybuddy.features.help.constants.urlHelpFeatured
import com.edricchan.studybuddy.features.help.data.model.HelpArticle
import com.edricchan.studybuddy.features.help.data.model.HelpArticles
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class GitHubHelpApiImpl @Inject constructor(
    @ContentNegotiationJsonHttpClient
    private val http: HttpClient
) : HelpApi {
    override suspend fun fetchHelpArticles(): List<HelpArticle> {
        return http.use { it.get(urlHelpFeatured).body<HelpArticles>().articles }
            ?: listOf()
    }
}
