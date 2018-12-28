package com.edricchan.studybuddy.interfaces

import android.net.Uri
import android.text.TextUtils

import androidx.annotation.StringDef

/**
 * An interface for [com.edricchan.studybuddy.HelpActivity]
 */
data class HelpArticle(
		/**
		 * The description of this help article
		 */
		var articleDesc: String? = null,
		/**
		 * The icon of this help article
		 */
		@ArticleIcon var articleIcon: String? = null,
		/**
		 * The title of this help article
		 */
		var articleTitle: String? = null,
		// Note: The Gson library is unable to parse Uris. Instead, use a string and then parse it to a Uri in the relevant code.
		/**
		 * The URI of this help article
		 */
		var articleUri: String? = null,
		/**
		 * Specifies whether this help article is unclickable
		 */
		var isDisabled: Boolean? = false,
		/**
		 * Specifies whether this help article is hidden from the list of help articles
		 */
		var isHidden: Boolean? = false
) {
	@StringDef(ICON_CONTACT, ICON_DOCUMENT, ICON_GITHUB, ICON_HELP, ICON_OPEN_IN_NEW, ICON_SEND_FEEDBACK)
	annotation class ArticleIcon

	/**
	 * Retrieves this help article's URI
	 *
	 * @return This help article's URI
	 */
	fun getArticleUri(): Uri {
		return Uri.parse(articleUri)
	}

	/**
	 * Creates a new instance of [HelpArticle]
	 */
	constructor() : this(null, null, null, null, false, false)

	/**
	 * Creates a new instance of [HelpArticle]
	 *
	 * @param articleTitle The title of the help article
	 * @param articleUrl   The URL of the help article
	 */
	@Deprecated("Use the builder instead")
	constructor(articleTitle: String?, articleUrl: String?) : this(articleTitle = articleTitle, articleUri = articleUrl)

	class Builder {
		private var article: HelpArticle? = null

		/**
		 * Creates a builder for a new help article
		 */
		constructor() {
			this.article = HelpArticle()
		}

		/**
		 * Creates a builder, but allows for a predefined help article to be specified
		 *
		 * @param article The predefined article
		 */
		@Deprecated(
				"Use Builder#Builder()",
				ReplaceWith(
						"HelpArticle.Builder()",
						"com.edricchan.studybuddy.interfaces.HelpArticle.Builder"
				)
		)
		constructor(article: HelpArticle) {
			this.article = article
		}

		/**
		 * Sets the description of this help article
		 *
		 * @param articleDesc The description
		 * @return The builder object to allow for chaining of methods
		 */
		fun setArticleDesc(articleDesc: String): Builder {
			article!!.articleDesc = articleDesc
			return this
		}

		/**
		 * Sets the icon for this help article
		 *
		 * @param articleIcon The icon for this help article
		 * @return The builder object to allow for chaining of methods
		 */
		fun setArticleIcon(@ArticleIcon articleIcon: String): Builder {
			article!!.articleIcon = articleIcon
			return this
		}

		/**
		 * Sets the title of this help article
		 *
		 * @param articleTitle The title of this help article
		 * @return The builder object to allow for chaining of methods
		 */
		fun setArticleTitle(articleTitle: String): Builder {
			article!!.articleTitle = articleTitle
			return this
		}

		/**
		 * Sets the URI of this help article
		 *
		 * @param articleUri The URI of this help article
		 * @return The builder object to allow for chaining of methods
		 */
		fun setArticleUri(articleUri: Uri): Builder {
			article!!.articleUri = articleUri.toString()
			return this
		}

		/**
		 * Sets the URL of this help article
		 *
		 * @param articleUrl The URL of this help article as a raw string
		 * @return The builder object to allow for chaining of methods
		 */
		fun setArticleUrl(articleUrl: String): Builder {
			article!!.articleUri = articleUrl
			return this
		}

		/**
		 * Sets the URI of this help article
		 *
		 * @param articleUri The URL of this help article, which will be parsed as an URI
		 * @return The builder object to allow for chaining of methods
		 */
		@Deprecated("Use {@link Builder#setArticleUrl(String)}")
		fun setArticleUri(articleUri: String): Builder {
			article!!.articleUri = articleUri
			return this
		}

		/**
		 * Sets whether this help article is unclickable.
		 *
		 * @param isDisabled True if this help article should be unclickable, false otherwise
		 * @return The builder object to allow for chaining of methods
		 */
		fun setIsDisabled(isDisabled: Boolean): Builder {
			article!!.isDisabled = isDisabled
			return this
		}

		/**
		 * Sets whether this help article is hidden.
		 *
		 * @param isHidden True if this help article should be hidden, false otherwise
		 * @return The builder object to allow for chaining of methods
		 */
		fun setIsHidden(isHidden: Boolean): Builder {
			article!!.isHidden = isHidden
			return this
		}

		/**
		 * Checks if all values in this help article and returns this help article
		 *
		 * @return The created help article
		 */
		fun create(): HelpArticle {
			// Null checks
			if (TextUtils.isEmpty(article!!.articleUri)) {
				throw RuntimeException("Please supply a URL!")
			}
			if (TextUtils.isEmpty(article!!.articleTitle)) {
				throw RuntimeException("Please supply a title!")
			}

			// Finally, return the help article
			return article as HelpArticle
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
