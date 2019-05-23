package com.edricchan.studybuddy.interfaces

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.IgnoreExtraProperties
import java.util.*

/**
 * Specifies a task
 * @property content The contents of this task
 * @property dueDate The due date of this task
 * @property isDone Whether the task is initially marked as done
 * @property isArchived Whether the task has been archived
 * @property id The document's ID of this task
 * @property project The project assigned to this task as a document reference
 * @property tags A list of tags assigned to this task
 * @property title The title of this task
 */
@IgnoreExtraProperties
data class TaskItem(
		var content: String? = null,
		var dueDate: Timestamp? = null,
		var isDone: Boolean? = false,
		var isArchived: Boolean? = false,
		@get:Exclude override var id: String = "",
		var project: DocumentReference? = null,
		var tags: List<String>? = null,
		var title: String? = null
) : HasId {
	constructor() : this(null, null, false, false, "", null, null, null)

	class Builder {
		private var task: TaskItem? = null
		private var taskTags: MutableList<String>? = null

		/**
		 * Retrieves the current list of tags
		 *
		 * @return The current list of tags
		 */
		val tags: List<String>?
			get() = taskTags

		/**
		 * Creates a builder for a new task
		 */
		constructor() {
			this.task = TaskItem()
		}

		/**
		 * Creates a builder, but allows for a predefined task to be specified
		 *
		 * @param task The predefined task
		 */
		@Deprecated("Use {@link Builder#Builder()}")
		constructor(task: TaskItem) {
			this.task = task
		}

		/**
		 * Adds a tag to the current list of tags
		 *
		 * @param tag The tag to add
		 * @return The builder object to allow for chaining of methods
		 */
		fun addTag(tag: String): Builder {
			taskTags?.add(tag)
			return this
		}

		/**
		 * Clears the current list of tags
		 *
		 * @return The builder object to allow for chaining of methods
		 */
		fun clearTags(): Builder {
			taskTags?.clear()
			return this
		}

		/**
		 * Sets the content of this task
		 *
		 * @param content The contents of this task
		 * @return The builder object to allow for chaining of methods
		 */
		fun setContent(content: String): Builder {
			task?.content = content
			return this
		}

		/**
		 * Sets the due date of this task
		 *
		 * @param dueDate The due date of this task as a Java [Date], which is automatically converted to a [Timestamp]
		 * @return The builder object to allow for chaining of methods
		 */
		fun setDueDate(dueDate: Date): Builder {
			task?.dueDate = Timestamp(dueDate)
			return this
		}

		/**
		 * Sets the due date of this task
		 *
		 * @param seconds     The seconds of UTC time since the Unix epoch
		 * @param nanoseconds The non-negative fractions of a second at nanosecond resolution
		 * @return The builder object to allow for chaining of methods
		 * @see [Timestamp]
		 */
		fun setDueDate(seconds: Long, nanoseconds: Int): Builder {
			task?.dueDate = Timestamp(seconds, nanoseconds)
			return this
		}

		/**
		 * Sets the due date of this task
		 *
		 * @param timestamp The due date of this task as a [Timestamp]
		 * @return The builder object to allow for chaining of methods
		 */
		fun setDueDate(timestamp: Timestamp): Builder {
			task?.dueDate = timestamp
			return this
		}

		/**
		 * Sets whether this task is marked as done
		 *
		 * @param isDone Whether the task is initially marked as done
		 * @return The builder object to allow for chaining of methods
		 */
		fun setIsDone(isDone: Boolean): Builder {
			task?.isDone = isDone
			return this
		}
		
		fun setIsArchived(isArchived: Boolean): Builder {
			task?.isArchived = isArchived
			return this
		}

		/**
		 * Sets the document ID of this task
		 *
		 * @param id The document ID of this task
		 * @return The builder object to allow for chaining of methods
		 */
		@Deprecated("The document ID is automatically appended to the task once it is added to the database")
		fun setId(id: String): Builder {
			task?.id = id
			return this
		}

		/**
		 * Sets the project assigned to this task
		 *
		 * @param project The project assigned to this task as a document reference to the project's document
		 * @return The builder object to allow for chaining of methods
		 */
		fun setProject(project: DocumentReference): Builder {
			task?.project = project
			return this
		}

		/**
		 * Sets the project assigned to this task
		 *
		 * @param fs      An instance of [FirebaseFirestore]. Use [FirebaseFirestore.getInstance] to retrieve an instance
		 * @param docPath The document path of the project. Must contain an even number of paths
		 * @return The builder object to allow for chaining of methods
		 */
		@Deprecated("Use {@link Builder#setProject(DocumentReference)}")
		fun setProject(fs: FirebaseFirestore, docPath: String): Builder {
			task?.project = fs.document(docPath)
			return this
		}

		/**
		 * Sets the project assigned to this task
		 *
		 * @param fs        An instance of [FirebaseFirestore]. Use [FirebaseFirestore.getInstance] to retrieve an instance
		 * @param userId    The user's ID
		 * @param projectId The project document ID
		 * @return The builder object to allow for chaining of methods
		 */
		@Deprecated("Use {@link Builder#setProject(DocumentReference)}")
		fun setProject(fs: FirebaseFirestore, userId: String, projectId: String): Builder {
			task?.project = fs.document("users/$userId/todoProjects/$projectId")
			return this
		}

		/**
		 * Sets the list of tags assigned to this task
		 *
		 * @param tags The list of tags to assign to this task
		 * @return The builder object to allow for chaining of methods
		 */
		fun setTags(tags: ArrayList<String>): Builder {
			taskTags = tags.toMutableList()
			return this
		}

		/**
		 * Sets the list of tags assigned to this task
		 *
		 * @param tags The list of tags to assign to this task
		 * @return The builder object to allow for chaining of methods
		 */
		fun setTags(tags: MutableList<String>): Builder {
			taskTags = tags
			return this
		}

		/**
		 * Sets the list of tags assigned to this task
		 *
		 * @param tags The list of tags to assign to this task
		 * @return The builder object to allow for chaining of methods
		 */
		@Deprecated("Use {@link Builder#setTags(List)}")
		fun setTags(tags: Array<String>): Builder {
			taskTags = Arrays.asList(*tags)
			return this
		}

		/**
		 * Sets the title of this task
		 *
		 * @param title The title of this task
		 * @return The builder object to allow for chaining of methods
		 */
		fun setTitle(title: String): Builder {
			task?.title = title
			return this
		}

		/**
		 * Checks if all values in this task are valid and returns this task
		 *
		 * @return The created task
		 * @throws RuntimeException If the task's title is empty/not instantiated
		 */
		@Throws(RuntimeException::class)
		fun create(): TaskItem {
			// Set the tags
			if (taskTags != null && taskTags.isNullOrEmpty()) {
				task?.tags = taskTags
			}
			// Null checks to prevent values from being null on the document
			if (task != null && task?.title.isNullOrEmpty()) {
				throw RuntimeException("Please supply a title!")
			}

			// Finally, return the task
			return task as TaskItem
		}
	}
}
