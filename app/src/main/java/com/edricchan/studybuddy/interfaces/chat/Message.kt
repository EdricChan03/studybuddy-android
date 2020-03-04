package com.edricchan.studybuddy.interfaces.chat

import com.edricchan.studybuddy.interfaces.HasId
import com.edricchan.studybuddy.interfaces.HasTimestampMetadata
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

/**
 * An interface used for a message in a chat group
 * @property text The text of this message (**required**)
 * @property author The author of this message
 */
@IgnoreExtraProperties
data class Message(
    @DocumentId override var id: String? = null,
    val text: String? = "",
    val author: DocumentReference? = null,
    @ServerTimestamp override val createdAt: Timestamp? = null,
    @ServerTimestamp override val lastModified: Timestamp? = null
) : HasId, HasTimestampMetadata {

    private constructor(builder: Builder) : this(
        text = builder.text,
        author = builder.author,
        createdAt = builder.createdAt,
        lastModified = builder.lastModified
    )

    companion object {
        /**
         * Creates a [Message] using a [Builder] (with support for inlined setting of variables)
         * @return The created [Message]
         */
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        /**
         * The message's text
         */
        var text: String? = null
        /**
         * The message's author
         */
        var author: DocumentReference? = null
        /**
         * The timestamp that the message was created at
         *
         * If left null, this field will be replaced with a server-generated timestamp
         * @see ServerTimestamp
         */
        var createdAt: Timestamp? = null
        /**
         * The timestamp that the message was last modified at
         *
         * If left null, this field will be replaced with a server-generated timestamp
         * @see ServerTimestamp
         */
        var lastModified: Timestamp? = null

        /**
         * Returns the created instance of [Message]
         * @return The created [Message]
         */
        fun build() = Message(this)
    }
}