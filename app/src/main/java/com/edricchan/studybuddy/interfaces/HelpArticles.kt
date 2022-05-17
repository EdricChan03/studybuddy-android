package com.edricchan.studybuddy.interfaces

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * A Kotlin class representation of the JSON schema
 * @property version The version of the schema to use
 * @property articles The list of help articles
 * @property jsonSchema The schema's URL
 */
@JsonClass(generateAdapter = true)
data class HelpArticles(
    val articles: List<HelpArticle>? = null,
    val version: Int? = 0,
    @Json(name = "\$schema") val jsonSchema: String? = null
)
