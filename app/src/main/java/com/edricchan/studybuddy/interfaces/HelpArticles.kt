package com.edricchan.studybuddy.interfaces

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A Kotlin class representation of the JSON schema
 * @property version The version of the schema to use
 * @property articles The list of help articles
 * @property jsonSchema The schema's URL
 */
@Serializable
data class HelpArticles(
    val articles: List<HelpArticle>? = null,
    val version: Int? = 0,
    @SerialName("\$schema") val jsonSchema: String? = null
)
