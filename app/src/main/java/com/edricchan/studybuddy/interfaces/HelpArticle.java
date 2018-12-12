package com.edricchan.studybuddy.interfaces;

import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.StringDef;

/**
 * An interface for {@link com.edricchan.studybuddy.HelpActivity}
 */
public class HelpArticle {
	@StringDef({
			ICON_CONTACT,
			ICON_DOCUMENT,
			ICON_GITHUB,
			ICON_HELP,
			ICON_OPEN_IN_NEW,
			ICON_SEND_FEEDBACK
	})
	public @interface ArticleIcon {
	}

	/**
	 * The description of this help article
	 */
	private String articleDesc;
	/**
	 * The icon of this help article
	 */
	@ArticleIcon
	private String articleIcon;
	/**
	 * The title of this help article
	 */
	private String articleTitle;
	// Note: The Gson library is unable to parse Uris. Instead, use a string and then parse it to a Uri in the relevant code.
	/**
	 * The URI of this help article
	 */
	private String articleUri;
	/**
	 * Specifies whether this help article is unclickable
	 */
	private boolean isDisabled;
	/**
	 * Specifies whether this help article is hidden from the list of help articles
	 */
	private boolean isHidden;
	/**
	 * Specifies that this help article's icon should use a contact icon
	 */
	public static final String ICON_CONTACT = "com.edricchan.studybuddy.article.icon.CONTACT";
	/**
	 * Specifies that this help article's icon should use a article/document icon
	 */
	public static final String ICON_DOCUMENT = "com.edricchan.studybuddy.article.icon.DOCUMENT";
	/**
	 * Specifies that this help article's icon should use a GitHub icon
	 *
	 * @deprecated Consider using a document icon ({@link HelpArticle#ICON_DOCUMENT} for instance)
	 */
	@Deprecated
	public static final String ICON_GITHUB = "com.edricchan.studybuddy.article.icon.GITHUB";
	/**
	 * Specifies that this help article's icon should use a help icon
	 */
	public static final String ICON_HELP = "com.edricchan.studybuddy.article.icon.HELP";
	/**
	 * Specifies that this help article's icon should use a open in new icon
	 */
	public static final String ICON_OPEN_IN_NEW = "com.edricchan.studybuddy.article.icon.OPEN_IN_NEW";
	/**
	 * Specifies that this help article's icon should use a send feedback icon
	 */
	public static final String ICON_SEND_FEEDBACK = "com.edricchan.studybuddy.article.icon.SEND_FEEDBACK";

	/**
	 * Retrieves this help article's description
	 *
	 * @return THis help article's description
	 */
	public String getArticleDesc() {
		return articleDesc;
	}

	/**
	 * Retrieves this help article's icon
	 *
	 * @return This help article's icon
	 */
	public String getArticleIcon() {
		return articleIcon;
	}

	/**
	 * Retrieves this help article's title
	 *
	 * @return This help article's title
	 */
	public String getArticleTitle() {
		return articleTitle;
	}

	/**
	 * Retrieves this help article's URI
	 *
	 * @return This help article's URI
	 */
	public Uri getArticleUri() {
		return Uri.parse(articleUri);
	}

	/**
	 * Retrieves whether this help article is marked as disabled/unclickable
	 *
	 * @return True if it is marked as disabled/unclickable, false otherwise
	 */
	public boolean isDisabled() {
		return isDisabled;
	}

	/**
	 * Retrieves whether this help article should be hidden
	 *
	 * @return True if it is marked as hidden, false otherwise
	 */
	public boolean isHidden() {
		return isHidden;
	}

	/**
	 * Creates a new instance of {@link HelpArticle}
	 */
	public HelpArticle() {
	}

	/**
	 * Creates a new instance of {@link HelpArticle}
	 *
	 * @param articleTitle The title of the help article
	 * @param articleUrl   The URL of the help article
	 * @deprecated Use {@link Builder}
	 */
	@Deprecated
	public HelpArticle(String articleTitle, String articleUrl) {
		this.articleTitle = articleTitle;
		this.articleUri = articleUrl;
	}

	public static class Builder {
		private HelpArticle article;

		/**
		 * Creates a builder for a new help article
		 */
		public Builder() {
			this.article = new HelpArticle();
		}

		/**
		 * Creates a builder, but allows for a predefined help article to be specified
		 *
		 * @param article The predefined article
		 * @deprecated Use {@link Builder#Builder()}
		 */
		@Deprecated
		public Builder(HelpArticle article) {
			this.article = article;
		}

		/**
		 * Sets the description of this help article
		 *
		 * @param articleDesc The description
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setArticleDesc(String articleDesc) {
			article.articleDesc = articleDesc;
			return this;
		}

		/**
		 * Sets the icon for this help article
		 *
		 * @param articleIcon The icon for this help article
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setArticleIcon(@ArticleIcon String articleIcon) {
			article.articleIcon = articleIcon;
			return this;
		}

		/**
		 * Sets the title of this help article
		 *
		 * @param articleTitle The title of this help article
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setArticleTitle(String articleTitle) {
			article.articleTitle = articleTitle;
			return this;
		}

		/**
		 * Sets the URI of this help article
		 *
		 * @param articleUri The URI of this help article
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setArticleUri(Uri articleUri) {
			article.articleUri = articleUri.toString();
			return this;
		}

		/**
		 * Sets the URL of this help article
		 *
		 * @param articleUrl The URL of this help article as a raw string
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setArticleUrl(String articleUrl) {
			article.articleUri = articleUrl;
			return this;
		}

		/**
		 * Sets the URI of this help article
		 *
		 * @param articleUri The URL of this help article, which will be parsed as an URI
		 * @return The builder object to allow for chaining of methods
		 * @deprecated Use {@link Builder#setArticleUrl(String)}
		 */
		@Deprecated
		public Builder setArticleUri(String articleUri) {
			article.articleUri = articleUri;
			return this;
		}

		/**
		 * Sets whether this help article is unclickable.
		 *
		 * @param isDisabled True if this help article should be unclickable, false otherwise
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setIsDisabled(boolean isDisabled) {
			article.isDisabled = isDisabled;
			return this;
		}

		/**
		 * Sets whether this help article is hidden.
		 *
		 * @param isHidden True if this help article should be hidden, false otherwise
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setIsHidden(boolean isHidden) {
			article.isHidden = isHidden;
			return this;
		}

		/**
		 * Checks if all values in this help article and returns this help article
		 *
		 * @return The created help article
		 */
		public HelpArticle create() {
			// Null checks
			if (TextUtils.isEmpty(article.articleUri)) {
				throw new RuntimeException("Please supply a URL!");
			}
			if (TextUtils.isEmpty(article.articleTitle)) {
				throw new RuntimeException("Please supply a title!");
			}

			// Finally, return the help article
			return article;
		}
	}
}
