package com.edricchan.studybuddy.interfaces

import android.net.Uri
import android.text.TextUtils

/**
 * A Kotlin class representation of the JSON schema
 * @property version The version of the schema to use
 * @property `$schema` The schema's URL
 * @property articles The list of help articles
 */
data class HelpArticles(
		var version: Int? = 0,
		var `$schema`: String? = null,
		var articles: Array<HelpArticle>? = null
) {
	// I don't think people would actually use this class.
	// Why do I always implement useless code? Sigh.
	@Deprecated("")
	class Builder {
		private val articlesJSON: HelpArticles = HelpArticles()

		/**
		 * Sets the articles for this help article JSON
		 *
		 * @param articles The articles to set as a [HelpArticle[]]
		 * @return The builder object to allow for chaining of methods
		 */
		fun setArticles(articles: Array<HelpArticle>): Builder {
			articlesJSON.articles = articles
			return this
		}

		/**
		 * Sets the articles for this help article JSON
		 *
		 * @param articles The articles to set as a [<]
		 * @return The builder object to allow for chaining of methods
		 */
		fun setArticles(articles: List<HelpArticle>): Builder {
			articlesJSON.articles = articles.toTypedArray()
			return this
		}

		/**
		 * Sets the JSON schema fro this help article JSON to use
		 *
		 * @param schema The schema to use
		 * @return The builder object to allow for chaining of methods
		 */
		fun setSchema(schema: String): Builder {
			articlesJSON.`$schema` = schema
			return this
		}

		/**
		 * Sets the JSON schema fro this help article JSON to use
		 *
		 * @param schema The schema to use
		 * @return The builder object to allow for chaining of methods
		 */
		@Deprecated("Use {@link Builder#setSchema(String)}")
		fun setSchema(schema: Uri): Builder {
			articlesJSON.`$schema` = schema.toString()
			return this
		}

		/**
		 * Sets the version for the schema for this help article JSON to use
		 *
		 * @param version The version to use
		 * @return The builder object to allow for chaining of methods
		 */
		fun setVersion(version: Int): Builder {
			articlesJSON.version = version
			return this
		}

		/**
		 * Returns the instance of the JSON
		 *
		 * @return The help article JSON
		 * @throws RuntimeException If the JSON is invalid
		 */
		@Throws(RuntimeException::class)
		fun create(): HelpArticles {
			if (TextUtils.isEmpty(articlesJSON.`$schema`)) {
				throw RuntimeException("Please supply a schema to use!")
			}
			if (articlesJSON.version!! < 1) {
				throw RuntimeException("Please supply a version that is greater than 0!")
			}
			return articlesJSON
		}
	}
}
