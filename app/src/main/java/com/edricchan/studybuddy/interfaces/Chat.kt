package com.edricchan.studybuddy.interfaces

import android.text.TextUtils
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

/**
 * An interface used for a chat group
 * @property name The name of this chat
 * @property description The description of this chat
 * @property members A list of document references of members in this chat
 * @property admins A list of document references of admins in this chat
 * @property lastModified A timestamp of when the chat was last modified
 * @property createdAt A timestamp of when the chat was created
 * @property owner The person as a document reference who originally created the chat
 * @property pinnedMessage A document reference to the pinned message of the chat
 */
@IgnoreExtraProperties
data class Chat(
		@get:Exclude override var id: String = "",
		var name: String? = null,
		var description: String? = null,
		var members: List<DocumentReference>? = null,
		var admins: List<DocumentReference>? = null,
		var lastModified: Timestamp? = null,
		var createdAt: Timestamp? = null,
		var owner: DocumentReference? = null,
		var pinnedMessage: DocumentReference? = null
): HasId {
	/**
	 * Builder object for simplifying the creation of a [Chat] class
	 */
	class Builder {
		private var chat: Chat? = null

		init {
			this.chat = Chat()
		}

		/**
		 * Sets the name of the chat
		 * @param name The name to set
		 * @return The builder object to allow for chaining of methods
		 */
		fun setName(name: String): Builder {
			chat?.name = name
			return this
		}

		/**
		 * Sets the description of the chat
		 * @param description The description to set
		 * @return The builder object to allow for chaining of methods
		 */
		fun setDescription(description: String): Builder {
			chat?.description = description
			return this
		}

		/**
		 * Sets the list of administrators of the chat
		 * @param admins A list of document references to user documents
		 * @return The builder object to allow for chaining of methods
		 */
		fun setAdmins(admins: List<DocumentReference>): Builder {
			chat?.admins = admins
			return this
		}

		/**
		 * Sets the last modified timestamp of the chat
		 * @param lastModified A valid [Timestamp] object
		 * @return The builder object to allow for chaining of methods
		 */
		@Deprecated("The last modified timestamp is automatically set when added to the database")
		fun setLastModified(lastModified: Timestamp): Builder {
			chat?.lastModified = lastModified
			return this
		}

		/**
		 * Sets the created at timestamp of the chat
		 * @param createdAt A valid [Timestamp] object
		 * @return The builder object to allow for chaining of methods
		 */
		@Deprecated("The created at timestamp is automatically set when added to the database")
		fun setCreatedAt(createdAt: Timestamp): Builder {
			chat?.createdAt = createdAt
			return this
		}

		/**
		 * Sets the owner of the chat
		 * @param owner A document reference to a user document
		 * @return The builder object to allow for chaining of methods
		 */
		fun setOwner(owner: DocumentReference): Builder {
			chat?.owner = owner
			return this
		}

		/**
		 * Sets the pinned message of the chat
		 * @param pinnedMessage A document reference to a message document
		 * @return The builder object to allow for chaining of methods
		 */
		fun setPinnedMessage(pinnedMessage: DocumentReference): Builder {
			chat?.pinnedMessage = pinnedMessage
			return this
		}

		/**
		 * Returns the created chat
		 * @return The created chat
		 */
		fun create(): Chat? {
			if (TextUtils.isEmpty(chat?.name)) {
				throw RuntimeException("Please supply a name!")
			}
			return chat
		}
	}
}