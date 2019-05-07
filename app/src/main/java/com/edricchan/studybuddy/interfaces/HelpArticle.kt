package com.edricchan.studybuddy.interfaces

import android.content.Context
import android.net.Uri
import androidx.annotation.BoolRes
import androidx.annotation.StringDef
import androidx.annotation.StringRes

/**
 * An interface for [com.edricchan.studybuddy.HelpActivity]
 * @property articleDesc The description of this help article
 * @property articleIcon The icon of this help article
 * @property articleTitle The title of this help article
 * @property articleUri The URI of this help article as a [String]
 * @property isDisabled Whether this help article is clickable (Set to [true] to be unclickable, [false] otherwise)
 * @property isHidden Whether this help article should be hidden (Set to [true] to be hidden, [false] otherwise)
 */
data class HelpArticle(
		var articleDesc: String? = null,
		@ArticleIcon var articleIcon: String? = null,
		var articleTitle: String? = null,
		// Note: The Gson library is unable to parse Uris. Instead, use a string and then parse it to a Uri in the relevant code.
		var articleUri: String? = null,
		var isDisabled: Boolean? = false,
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

	class Builder {
		private var context: Context? = null
		private var article: HelpArticle? = null

		/**
		 * Creates a builder for a new help article
		 * @param context The context
		 */
		constructor(context: Context) {
			Builder(context, HelpArticle())
		}

		/**
		 * Creates a builder, but allows for a predefined help article to be specified
		 *
		 * @param context The context
		 * @param article The predefined article
		 */
		constructor(context: Context, article: HelpArticle) {
			this.context = context
			this.article = article
		}

		/**
		 * Sets the description of this help article
		 *
		 * @param articleDesc The description
		 * @return The builder object to allow for chaining of methods
		 */
		fun setArticleDesc(articleDesc: String): Builder {
			article?.articleDesc = articleDesc
			return this
		}

		/**
		 * Sets the description of this help article
		 *
		 * @param articleDescRes The description as a string resource reference
		 * @return The builder object to allow for chaining of methods
		 */
		fun setArticleDesc(@StringRes articleDescRes: Int): Builder {
			article?.articleDesc = context?.getString(articleDescRes)
			return this
		}

		/**
		 * Sets the icon for this help article
		 *
		 * @param articleIcon The icon for this help article
		 * @return The builder object to allow for chaining of methods
		 */
		fun setArticleIcon(@ArticleIcon articleIcon: String): Builder {
			article?.articleIcon = articleIcon
			return this
		}

		/**
		 * Sets the title of this help article
		 *
		 * @param articleTitle The title of this help article
		 * @return The builder object to allow for chaining of methods
		 */
		fun setArticleTitle(articleTitle: String): Builder {
			article?.articleTitle = articleTitle
			return this
		}

		/**
		 * Sets the title of this help article
		 *
		 * @param articleTitleRes The title of this help article as a string resource reference
		 * @return The builder object to allow for chaining of methods
		 */
		fun setArticleTitle(@StringRes articleTitleRes: Int): Builder {
			article?.articleTitle = context?.getString(articleTitleRes)
			return this
		}

		/**
		 * Sets the URI of this help article
		 *
		 * @param articleUri The URI of this help article
		 * @return The builder object to allow for chaining of methods
		 */
		fun setArticleUri(articleUri: Uri): Builder {
			article?.articleUri = articleUri.toString()
			return this
		}

		/**
		 * Sets the URL of this help article
		 *
		 * @param articleUrl The URL of this help article as a raw string
		 * @return The builder object to allow for chaining of methods
		 */
		fun setArticleUrl(articleUrl: String): Builder {
			article?.articleUri = articleUrl
			return this
		}

		/**
		 * Sets the URL of this help article
		 *
		 * @param articleUrlRes The URL of this help article as a string resource reference
		 * @return The builder object to allow for chaining of methods
		 */
		fun setArticleUrl(@StringRes articleUrlRes: Int): Builder {
			article?.articleUri = context?.getString(articleUrlRes)
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
			article?.articleUri = articleUri
			return this
		}

		/**
		 * Sets whether this help article is unclickable
		 *
		 * @param isDisabled [true] if this help article should be unclickable, [false] otherwise
		 * @return The builder object to allow for chaining of methods
		 */
		fun setIsDisabled(isDisabled: Boolean): Builder {
			article?.isDisabled = isDisabled
			return this
		}

		/**
		 * Sets whether this help article is unclickable
		 *
		 * @param isDisabledRes [true] if this help article should be unclickable, [false] otherwise
		 * (pass a boolean resource reference for this argument)
		 * @return The builder object to allow for chaining of methods
		 */
		fun setIsDisabled(@BoolRes isDisabledRes: Int): Builder {
			article?.isDisabled = context?.resources?.getBoolean(isDisabledRes)
			return this
		}

		/**
		 * Sets whether this help article is hidden
		 *
		 * @param isHidden [true] if this help article should be hidden, [false] otherwise
		 * @return The builder object to allow for chaining of methods
		 */
		fun setIsHidden(isHidden: Boolean): Builder {
			article?.isHidden = isHidden
			return this
		}

		/**
		 * Sets whether this help article is hidden
		 *
		 * @param isHiddenRes [true] if this help article should be hidden, [false] otherwise
		 * (pass a boolean resource reference for this argument)
		 * @return The builder object to allow for chaining of methods
		 */
		fun setIsHidden(@BoolRes isHiddenRes: Int): Builder {
			article?.isHidden = context?.resources?.getBoolean(isHiddenRes)
			return this
		}

		/**
		 * Checks if all values in this help article and returns this help article
		 *
		 * @return The created help article
		 */
		fun create(): HelpArticle? {
			// Null checks
			if (article?.articleUri.isNullOrEmpty()) {
				throw RuntimeException("Please supply a URL!")
			}
			if (article?.articleTitle.isNullOrEmpty()) {
				throw RuntimeException("Please supply a title!")
			}

			// Finally, return the help article
			return article
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
