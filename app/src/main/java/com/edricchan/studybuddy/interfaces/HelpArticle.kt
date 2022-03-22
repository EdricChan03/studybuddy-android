package com.edricchan.studybuddy.interfaces

import android.net.Uri
import androidx.annotation.StringDef
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * An interface for [com.edricchan.studybuddy.ui.modules.help.HelpActivity]
 * @property description The description of this help article
 * @property icon The icon of this help article
 * @property title The title of this help article
 * @property uri The URI of this help article
 * @property isDisabled Whether this help article is clickable
 * @property isHidden Whether this help article should be hidden
 */
@JsonClass(generateAdapter = true)
data class HelpArticle(
    @Json(name = "articleDesc") val description: String? = null,
    @Json(name = "articleIcon") @ArticleIcon val icon: String? = null,
    @Json(name = "articleTitle") val title: String? = null,
    @Json(name = "articleUri") val uri: Uri? = null,
    val isDisabled: Boolean = false,
    val isHidden: Boolean = false
) {
    private constructor(builder: Builder) : this(
        builder.articleDesc,
        builder.articleIcon,
        builder.articleTitle,
        builder.articleUri,
        builder.isDisabled,
        builder.isHidden
    )

    @StringDef(
        ICON_CONTACT,
        ICON_DOCUMENT,
        ICON_GITHUB,
        ICON_HELP,
        ICON_OPEN_IN_NEW,
        ICON_SEND_FEEDBACK
    )
    annotation class ArticleIcon

    companion object {
        /**
         * Creates a [HelpArticle] using a [Builder] (with support for inlined setting of variables)
         * @return The created [HelpArticle]
         */
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()

        /**
         * Specifies that this help article's icon should use a contact icon
         */
        const val ICON_CONTACT = "com.edricchan.studybuddy.article.icon.CONTACT"

        /**
         * Specifies that this help article's icon should use a article/document icon
         */
        const val ICON_DOCUMENT = "com.edricchan.studybuddy.article.icon.DOCUMENT"

        /**
         * Specifies that this help article's icon should use a GitHub icon
         */
        @Deprecated("Consider using a document icon ({@link HelpArticle#ICON_DOCUMENT} for instance)")
        const val ICON_GITHUB = "com.edricchan.studybuddy.article.icon.GITHUB"

        /**
         * Specifies that this help article's icon should use a help icon
         */
        const val ICON_HELP = "com.edricchan.studybuddy.article.icon.HELP"

        /**
         * Specifies that this help article's icon should use a open in new icon
         */
        const val ICON_OPEN_IN_NEW = "com.edricchan.studybuddy.article.icon.OPEN_IN_NEW"

        /**
         * Specifies that this help article's icon should use a send feedback icon
         */
        const val ICON_SEND_FEEDBACK = "com.edricchan.studybuddy.article.icon.SEND_FEEDBACK"
    }

    class Builder {
        /**
         * The help article's title
         */
        var articleTitle: String? = null
        /**
         * The help article's description
         */
        var articleDesc: String? = null
        /**
         * The help article's URI
         */
        var articleUri: Uri? = null
        /**
         * The help article's icon
         */
        @ArticleIcon
        var articleIcon: String? = null
        /**
         * Whether the help article is marked as disabled
         */
        var isDisabled: Boolean = false
        /**
         * Whether the help article is marked as hidden
         */
        var isHidden: Boolean = false

        /**
         * Returns the created instance of [HelpArticle]
         * @return The created [HelpArticle]
         */
        fun build() = HelpArticle(this)
    }
}
