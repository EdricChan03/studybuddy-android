package com.edricchan.studybuddy.interfaces.chat

import com.edricchan.studybuddy.annotations.VisibilityAnnotation
import com.edricchan.studybuddy.interfaces.HasId
import com.edricchan.studybuddy.interfaces.HasTimestampMetadata
import com.edricchan.studybuddy.interfaces.Visibility
import com.edricchan.studybuddy.interfaces.chat.icon.ChatIcon
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

// TODO: Add support for custom group image
/**
 * An interface used for a chat group
 * @property name The name of this chat (**required**)
 * @property description The description of this chat
 * @property icon A URL to the icon (ideally stored in Firebase Storage)
 * @property visibility The visibility of the chat
 * @property isArchived Whether the chat is archived, indicating that the chat is marked as read-only
 * @property pinnedMessage A document reference to the pinned message of the chat
 * @property owner The person as a document reference who originally created the chat
 * @property admins A list of document references of admins in this chat
 * @property members A list of document references of members in this chat
 */
@IgnoreExtraProperties
data class Chat(
		@DocumentId override var id: String? = null,
		val name: String? = "",
		val description: String? = null,
		val icon: ChatIcon? = null,
		@VisibilityAnnotation val visibility: String? = Visibility.PUBLIC,
		val isArchived: Boolean? = false,
		val pinnedMessage: DocumentReference? = null,
		val owner: DocumentReference? = null,
		val admins: List<DocumentReference?>? = null,
		val members: List<DocumentReference?>? = null,
		@ServerTimestamp override val createdAt: Timestamp? = null,
		@ServerTimestamp override val lastModified: Timestamp? = null
) : HasId, HasTimestampMetadata {
	private constructor(builder: Builder) : this(
			name = builder.name,
			description = builder.description,
			icon = builder.icon,
			visibility = builder.visibility,
			isArchived = builder.isArchived,
			pinnedMessage = builder.pinnedMessage,
			owner = builder.owner,
			admins = builder.admins,
			members = builder.members,
			createdAt = builder.createdAt,
			lastModified = builder.lastModified
	)

	companion object {
		/**
		 * Creates a [Chat] using a [Builder] (with support for inlined setting of variables)
		 * @return The created [Chat]
		 */
		inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
	}

	/**
	 * Builder object for simplifying the creation of a [Chat] class
	 */
	class Builder {
		/**
		 * The chat's name
		 */
		var name: String? = null
		/**
		 * The chat's description
		 */
		var description: String? = null
		/**
		 * The chat's icon
		 */
		var icon: ChatIcon? = null
		/**
		 * The chat's visibility
		 * @see Visibility
		 * @see VisibilityAnnotation
		 */
		@VisibilityAnnotation
		var visibility: String? = null
		/**
		 * Whether the chat has been marked as archived
		 */
		var isArchived: Boolean? = false
		/**
		 * The chat's pinned message
		 */
		var pinnedMessage: DocumentReference? = null
		/**
		 * The chat's owner
		 */
		var owner: DocumentReference? = null
		/**
		 * The chat's admins
		 */
		var admins: MutableList<DocumentReference?>? = mutableListOf()
		/**
		 * The chat's members
		 */
		var members: MutableList<DocumentReference?>? = mutableListOf()
		/**
		 * The timestamp that the chat was created at
		 *
		 * If left null, this field will be replaced with a server-generated timestamp
		 * @see ServerTimestamp
		 */
		var createdAt: Timestamp? = null
		/**
		 * The timestamp that the chat was last modified at
		 *
		 * If left null, this field will be replaced with a server-generated timestamp
		 * @see ServerTimestamp
		 */
		var lastModified: Timestamp? = null

		/**
		 * Creates the [Chat] with the [Builder] instance
		 * @return The created [Chat]
		 */
		fun build() = Chat(this)
	}

}