package com.edricchan.studybuddy.interfaces;

import android.text.TextUtils;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TaskItem {
	/**
	 * Specifies the contents of this task
	 */
	private String content;
	/**
	 * Specifies the due date of this task
	 */
	private Timestamp dueDate;
	/**
	 * Specifies is the task is marked as done
	 */
	private boolean isDone;
	/**
	 * Specifies the document's ID of this task in the database
	 */
	private String id;
	/**
	 * Specifies the project assigned to this task as a document reference to the project's document
	 */
	private DocumentReference project;
	/**
	 * Specifies the list of tags assigned to this task
	 */
	private List<String> tags;
	/**
	 * Secifies the title of this task
	 */
	private String title;

	// Default constructor for Cloud Firestore
	public TaskItem() {
	}

	/**
	 * Retrieves the content of this task
	 *
	 * @return The content of this task
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * Retrieves the due date of this task
	 *
	 * @return The due date of this task as a {@link Timestamp}
	 */
	public Timestamp getDueDate() {
		return this.dueDate;
	}

	/**
	 * Retrieves whether this task is marked as done
	 *
	 * @return Whether this task is marked as done. True if it is marked as done, false otherwise
	 */
	public boolean isDone() {
		return this.isDone;
	}

	/**
	 * Retrieves the document's ID of this task
	 *
	 * @return The document's ID of this task
	 * @implNote It's preferred to retrieve the document's ID via {@link DocumentReference#getId()} instead.
	 * <p>This method is only provided for convenience and may be deprecated in the future.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Retrieves the project assigned to this task
	 *
	 * @return The project assigned to this task as a document reference to the project's document
	 */
	public DocumentReference getProject() {
		return this.project;
	}

	/**
	 * Retrieves the list of tags assigned to this task
	 *
	 * @return The list of tags assigned to this task
	 */
	public List<String> getTags() {
		return tags;
	}

	/**
	 * Retrieves the title of this task
	 *
	 * @return The title of this task
	 */
	public String getTitle() {
		return title;
	}

	public static class Builder {
		private TaskItem task;
		private List<String> taskTags;

		/**
		 * Creates a builder for a new task
		 */
		public Builder() {
			this.task = new TaskItem();
		}

		/**
		 * Creates a builder, but allows for a predefined task to be specified
		 *
		 * @param task The predefined task
		 * @deprecated Use {@link Builder#Builder()}
		 */
		@Deprecated
		public Builder(TaskItem task) {
			this.task = task;
		}

		/**
		 * Adds a tag to the current list of tags
		 *
		 * @param tag The tag to add
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder addTag(String tag) {
			taskTags.add(tag);
			return this;
		}

		/**
		 * Clears the current list of tags
		 *
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder clearTags() {
			taskTags.clear();
			return this;
		}

		/**
		 * Retrieves the current list of tags
		 *
		 * @return The current list of tags
		 */
		public List<String> getTags() {
			return taskTags;
		}

		/**
		 * Sets the content of this task
		 *
		 * @param content The contents of this task
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setContent(String content) {
			task.content = content;
			return this;
		}

		/**
		 * Sets the due date of this task
		 *
		 * @param dueDate The due date of this task as a Java {@link Date}, which is automatically converted to a {@link Timestamp}
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setDueDate(Date dueDate) {
			task.dueDate = new Timestamp(dueDate);
			return this;
		}

		/**
		 * Sets the due date of this task
		 *
		 * @param seconds     The seconds of UTC time since the Unix epoch
		 * @param nanoseconds The non-negative fractions of a second at nanosecond resolution
		 * @return The builder object to allow for chaining of methods
		 * @see <a href="https://firebase.google.com/docs/reference/android/com/google/firebase/Timestamp#Timestamp(long,%20int)"><code>Timestamp(long seconds, int nanoseconds)</code></a>
		 */
		public Builder setDueDate(long seconds, int nanoseconds) {
			task.dueDate = new Timestamp(seconds, nanoseconds);
			return this;
		}

		/**
		 * Sets the due date of this task
		 *
		 * @param timestamp The due date of this task as a {@link Timestamp}
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setDueDate(Timestamp timestamp) {
			task.dueDate = timestamp;
			return this;
		}

		/**
		 * Sets whether this task is marked as done
		 *
		 * @param isDone Whether the task is initially marked as done
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setIsDone(boolean isDone) {
			task.isDone = isDone;
			return this;
		}

		/**
		 * Sets the document ID of this task
		 *
		 * @param id The document ID of this task
		 * @return The builder object to allow for chaining of methods
		 * @deprecated The document ID is automatically appended to the task once it is added to the database
		 */
		@Deprecated
		public Builder setId(String id) {
			task.id = id;
			return this;
		}

		/**
		 * Sets the project assigned to this task
		 *
		 * @param project The project assigned to this task as a document reference to the project's document
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setProject(DocumentReference project) {
			task.project = project;
			return this;
		}

		/**
		 * Sets the project assigned to this task
		 *
		 * @param fs      An instance of {@link FirebaseFirestore}. Use {@link FirebaseFirestore#getInstance()} to retrieve an instance
		 * @param docPath The document path of the project. Must contain an even number of paths
		 * @return The builder object to allow for chaining of methods
		 * @deprecated Use {@link Builder#setProject(DocumentReference)}
		 */
		@Deprecated
		public Builder setProject(FirebaseFirestore fs, String docPath) {
			task.project = fs.document(docPath);
			return this;
		}

		/**
		 * Sets the project assigned to this task
		 *
		 * @param fs        An instance of {@link FirebaseFirestore}. Use {@link FirebaseFirestore#getInstance()} to retrieve an instance
		 * @param userId    The user's ID
		 * @param projectId The project document ID
		 * @return The builder object to allow for chaining of methods
		 * @deprecated Use {@link Builder#setProject(DocumentReference)}
		 */
		@Deprecated
		public Builder setProject(FirebaseFirestore fs, String userId, String projectId) {
			task.project = fs.document("users/" + userId + "/todoProjects/" + projectId);
			return this;
		}

		/**
		 * Sets the list of tags assigned to this task
		 *
		 * @param tags The list of tags to assign to this task
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setTags(List<String> tags) {
			taskTags = tags;
			return this;
		}

		/**
		 * Sets the list of tags assigned to this task
		 *
		 * @param tags The list of tags to assign to this task
		 * @return The builder object to allow for chaining of methods
		 * @deprecated Use {@link Builder#setTags(List)}
		 */
		@Deprecated
		public Builder setTags(String[] tags) {
			taskTags = Arrays.asList(tags);
			return this;
		}

		/**
		 * Sets the title of this task
		 *
		 * @param title The title of this task
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setTitle(String title) {
			task.title = title;
			return this;
		}

		/**
		 * Checks if all values in this task are valid and returns this task
		 *
		 * @return The created task
		 * @throws RuntimeException If the task's title is empty/not instantiated
		 */
		public TaskItem create() throws RuntimeException {
			// Set the tags
			if (taskTags != null && !taskTags.isEmpty()) {
				task.tags = taskTags;
			}
			// Null checks to prevent values from being null on the document
			if (TextUtils.isEmpty(task.title)) {
				throw new RuntimeException("Please supply a title!");
			}

			// Finally, return the task
			return task;
		}
	}
}
