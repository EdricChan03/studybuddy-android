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
) {
    private constructor(builder: Builder) : this(
        builder.articles,
        builder.version,
        builder.`$schema`
    )

    companion object {
        /**
         * Creates a [HelpArticles] using a [Builder] (with support for inlined setting of variables)
         * @return The created [HelpArticles]
         */
        @Deprecated("This method is kept for compatibility reasons. Please do not use.")
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    @Deprecated("This class is kept for compatibility reasons. Please do not use.")
    class Builder {
        /**
         * The help articles' articles
         */
        var articles: MutableList<HelpArticle>? = mutableListOf()
        /**
         * The help articles' JSON schema
         */
        var `$schema`: String? = null
        /**
         * The help articles' version
         */
        var version: Int? = null

        /**
         * Returns the created [HelpArticles]
         * @return The created [HelpArticles]
         */
        fun build() = HelpArticles(this)
    }
}
