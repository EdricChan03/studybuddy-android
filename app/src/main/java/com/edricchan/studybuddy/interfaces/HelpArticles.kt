package com.edricchan.studybuddy.interfaces

/**
 * A Kotlin class representation of the JSON schema
 * @property version The version of the schema to use
 * @property articles The list of help articles
 * @property `$schema` The schema's URL
 */
data class HelpArticles(
    val articles: List<HelpArticle>? = null,
    val version: Int? = 0,
    val `$schema`: String? = null
)
