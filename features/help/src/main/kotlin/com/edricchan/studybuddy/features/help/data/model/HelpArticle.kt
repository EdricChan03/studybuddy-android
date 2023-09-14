package com.edricchan.studybuddy.features.help.data.model

import android.net.Uri
import androidx.annotation.StringDef
import com.edricchan.studybuddy.data.serialization.UriSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An interface for [com.edricchan.studybuddy.ui.modules.help.HelpActivity]
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
    @SerialName("articleIcon") @ArticleIcon val icon: String? = null,
    @SerialName("articleTitle") val title: String,
    @Serializable(with = UriSerializer::class) @SerialName("articleUri") val uri: Uri,
    val isDisabled: Boolean = false,
    val isHidden: Boolean = false
) {
    @Suppress("DEPRECATION")
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
