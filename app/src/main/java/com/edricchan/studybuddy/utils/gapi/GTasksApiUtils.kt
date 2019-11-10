package com.edricchan.studybuddy.utils.gapi

import com.edricchan.studybuddy.BuildConfig
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.tasks.Tasks
import com.google.api.services.tasks.model.TaskList
import com.google.api.services.tasks.model.TaskLists

/**
 * Utility class that auto-initialises the Google Tasks API.
 *
 * Note: This class does not automatically handle authorising the request for you.
 * @param credential The currently-signed in user's credential
 */
class GTasksApiUtils(
		val credential: GoogleAccountCredential
) {
	/**
	 * The current instance of the [Tasks] API.
	 */
	var service: Tasks
		private set

	/**
	 * The application name to be passed to the [Tasks.Builder].
	 */
	var applicationName: String = "StudyBuddy"

	init {
		service = Tasks.Builder(NetHttpTransport(), JacksonFactory(), credential)
				.apply {
					applicationName = "${applicationName}/${BuildConfig.VERSION_NAME}"
				}
				.build()
	}

	/**
	 * Gets the specified task list from the currently signed-in user as an instance of
	 * [Tasks.Tasklists.Get].
	 * @param taskList The ID of the task list.
	 */
	fun getTaskListHttp(taskList: String): Tasks.Tasklists.Get {
		return service.tasklists().get(taskList)
	}

	/**
	 * Gets the specified task list from the currently signed-in user.
	 * @param taskList The ID of the task list.
	 */
	fun getTaskList(taskList: String): TaskList {
		return getTaskListHttp(taskList).execute()
	}


	/**
	 * Lists the currently signed-in user's task lists as an instance of [Tasks.Tasklists.List].
	 */
	fun listTaskListsHttp(): Tasks.Tasklists.List {
		return service.tasklists().list()
	}

	/**
	 * Lists the currently signed-in user's task lists.
	 */
	fun listTaskLists(): TaskLists {
		return listTaskListsHttp().execute()
	}
}