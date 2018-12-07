package com.edricchan.studybuddy.interfaces;

import android.net.Uri;

/**
 * An interface for {@link com.edricchan.studybuddy.HelpActivity}
 */
public class HelpArticle {
	private String articleDesc;
	private String articleTitle;
	private Uri articleUri;

	/**
	 * Retrieves the help article's description
	 *
	 * @return The help article's description
	 */
	public String getArticleDesc() {
		return articleDesc;
	}

	/**
	 * Retrieves the help article's title
	 *
	 * @return The help article's title
	 */
	public String getArticleTitle() {
		return articleTitle;
	}

	/**
	 * Retrieves the help article's URI
	 *
	 * @return The help article's URI
	 */
	public Uri getArticleUri() {
		return articleUri;
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
		this.articleUri = Uri.parse(articleUrl);
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
		 * @param articleDesc The description of this help article
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setArticleDesc(String articleDesc) {
			article.articleDesc = articleDesc;
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
			article.articleUri = articleUri;
			return this;
		}

		/**
		 * Sets the URI of this help article
		 *
		 * @param articleUri The URL of this help article, which will be parsed as an URI
		 * @return The builder object to allow for chaining of methods
		 * @deprecated Use {@link Builder#setArticleUri(Uri)}
		 */
		@Deprecated
		public Builder setArticleUri(String articleUri) {
			article.articleUri = Uri.parse(articleUri);
			return this;
		}

		/**
		 * Checks if all values in this help article and returns this help article
		 *
		 * @return The created help article
		 */
		public HelpArticle create() {
			// Null checks
			if (article.articleUri == null) {
				throw new RuntimeException("Please supply a URI!");
			}

			// Finally, return the help article
			return article;
		}
	}
}
