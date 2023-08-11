package com.edricchan.studybuddy.interfaces

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

/**
 * Specifies a task (in the old format)
 * @property content The contents of this task
 * @property dueDate The due date of this task
 * @property done Whether the task is initially marked as done
 * @property archived Whether the task has been archived
 * @property id The document's ID of this task
 * @property project The project assigned to this task as a document reference
 * @property tags A list of tags assigned to this task
 * @property title The title of this task
 */
@IgnoreExtraProperties
data class TodoItem(
    @DocumentId override val id: String = "",
    val content: String? = null,
    val dueDate: Timestamp? = null,
    // Note: Firestore converts isDone to done when added to the database
    val done: Boolean? = false,
    // Note: Firestore converts isArchived to archived when added to the database
    val archived: Boolean? = false,
    val project: DocumentReference? = null,
    val tags: List<String>? = null,
    val title: String? = null,
    @ServerTimestamp override val createdAt: Timestamp? = null,
    @ServerTimestamp override val lastModified: Timestamp? = null
) : HasId, HasTimestampMetadata {

    private constructor(builder: Builder) : this(
        content = builder.content,
        dueDate = builder.dueDate,
        done = builder.done,
        archived = builder.archived,
        project = builder.project,
        tags = builder.tags,
        title = builder.title,
        createdAt = builder.createdAt,
        lastModified = builder.lastModified
    )

    companion object {
        /**
         * Creates a [TodoItem] using a [Builder] (with support for inlined setting of variables)
         * @return The created [TodoItem]
         */
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    /**
     * Builder object for simplifying the creation of a [TodoItem] class
     */
    class Builder {
        /**
         * The content of the task
         */
        var content: String? = null

        /**
         * The task's title
         */
        var title: String? = null

        /**
         * The task's due date
         */
        var dueDate: Timestamp? = null

        /**
         * The task's project
         */
        var project: DocumentReference? = null

        /**
         * The task's tags
         */
        var tags: List<String>? = null

        /**
         * Whether the task has been initially marked as done
         */
        var done: Boolean = false

        /**
         * Whether the task has been archived
         */
        var archived: Boolean = false

        /**
         * The timestamp the task was created at
         *
         * If left null, this field will be replaced with a server-generated timestamp
         * @see ServerTimestamp
         */
        var createdAt: Timestamp? = null

        /**
         * The timestamp the task was last modified
         *
         * If left null, this field will be replaced with a server-generated timestamp
         * @see ServerTimestamp
         */
        var lastModified: Timestamp? = null

        /**
         * Returns the created [TodoItem]
         * @return The created [TodoItem]
         */
        fun build() = TodoItem(this)
    }
}
