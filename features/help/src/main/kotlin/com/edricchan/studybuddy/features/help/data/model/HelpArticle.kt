package com.edricchan.studybuddy.features.help.data.model

import android.net.Uri
import com.edricchan.studybuddy.data.serialization.UriSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Article metadata for a help entry.
 * @property description The description of this help article
 * @property icon The icon of this help article
 * @property title The title of this help article
 * @property uri The URI of this help article
 * @property isDisabled Whether this help article is clickable
 * @property isHidden Whether this help article should be hidden
 */
@Serializable
data class HelpArticle(
    @SerialName("articleDesc") val description: String? = null,
    @SerialName("articleIcon") val icon: ArticleIcon? = null,
    @SerialName("articleTitle") val title: String,
    @Serializable(with = UriSerializer::class) @SerialName("articleUri") val uri: Uri,
    val isDisabled: Boolean = false,
    val isHidden: Boolean = false
) {
    @Serializable
    @JvmInline
    value class ArticleIcon(val value: String) {
        companion object {
            /** Use a contact icon for this article item when displayed in a list. */
            val Contact: ArticleIcon = ArticleIcon(ICON_CONTACT)

            /** Use a document icon for this article item when displayed in a list. */
            val Document: ArticleIcon = ArticleIcon(ICON_DOCUMENT)

            /** Use the GitHub logo for this article item when displayed in a list. */
            @Deprecated("Consider using a document icon instead")
            val GitHub: ArticleIcon = ArticleIcon(ICON_GITHUB)

            /** Use a help icon for this article item when displayed in a list. */
            val Help: ArticleIcon = ArticleIcon(ICON_HELP)

            /** Use an open link icon for this article item when displayed in a list. */
            val OpenInNew: ArticleIcon = ArticleIcon(ICON_OPEN_IN_NEW)

            /** Use a feedback icon for this article item when displayed in a list. */
            val SendFeedback: ArticleIcon = ArticleIcon(ICON_SEND_FEEDBACK)
        }
    }

    companion object {
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
}
