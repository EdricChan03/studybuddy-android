package com.edricchan.studybuddy.interfaces;

import android.net.Uri;
import android.text.TextUtils;

import java.util.List;

/**
 * A Java class representation of the JSON schema
 */
public class HelpArticles {
	/**
	 * The version of the schema to use
	 */
	private int version;
	/**
	 * The schema's URL
	 */
	private String $schema;
	/**
	 * The list of help articles
	 */
	private HelpArticle[] articles;

	public HelpArticles() {
	}

	/**
	 * Retrieves the list of help articles
	 *
	 * @return The list of help articles
	 */
	public HelpArticle[] getArticles() {
		return articles;
	}

	/**
	 * Retrieves the schema
	 *
	 * @return The schema
	 */
	public String get$Schema() {
		return $schema;
	}

	/**
	 * Retrieves the version of the schema
	 *
	 * @return The version of the schema
	 */
	public int getVersion() {
		return version;
	}

	// I don't think people would actually use this class.
	// Why do I always implement useless code? Sigh.
	@Deprecated
	public static class Builder {
		private HelpArticles articlesJSON;

		/**
		 * Constructs a builder with a new help article JSON
		 */
		public Builder() {
			articlesJSON = new HelpArticles();
		}

		/**
		 * Sets the articles for this help article JSON
		 *
		 * @param articles The articles to set as a {@link HelpArticle[]}
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setArticles(HelpArticle[] articles) {
			articlesJSON.articles = articles;
			return this;
		}

		/**
		 * Sets the articles for this help article JSON
		 *
		 * @param articles The articles to set as a {@link List<HelpArticle>}
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setArticles(List<HelpArticle> articles) {
			articlesJSON.articles = articles.toArray(new HelpArticle[0]);
			return this;
		}

		/**
		 * Sets the JSON schema fro this help article JSON to use
		 *
		 * @param schema The schema to use
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setSchema(String schema) {
			articlesJSON.$schema = schema;
			return this;
		}

		/**
		 * Sets the JSON schema fro this help article JSON to use
		 *
		 * @param schema The schema to use
		 * @return The builder object to allow for chaining of methods
		 * @deprecated Use {@link Builder#setSchema(String)}
		 */
		@Deprecated
		public Builder setSchema(Uri schema) {
			articlesJSON.$schema = schema.toString();
			return this;
		}

		/**
		 * Sets the version for the schema for this help article JSON to use
		 *
		 * @param version The version to use
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setVersion(int version) {
			articlesJSON.version = version;
			return this;
		}

		/**
		 * Returns the instance of the JSON
		 *
		 * @return The help article JSON
		 * @throws RuntimeException If the JSON is invalid
		 */
		public HelpArticles create() throws RuntimeException {
			if (TextUtils.isEmpty(articlesJSON.$schema)) {
				throw new RuntimeException("Please supply a schema to use!");
			}
			if (articlesJSON.version < 1) {
				throw new RuntimeException("Please supply a version that is greater than 0!");
			}
			return articlesJSON;
		}
	}
}
