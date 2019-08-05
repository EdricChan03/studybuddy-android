package com.edricchan.studybuddy.interfaces.chat.icon

/**
 * Data class used to represent a chat icon.
 * @property assets The list of icon assets (See [ChatIconAsset] for more info)
 * @property id The ID of the chat icon
 * @property name The name of the chat icon (if the chat icon is a default)
 * @property subjectType The type of subject that the chat icon represents
 * @property author The author (Can be a string or a [com.google.firebase.firestore.DocumentReference])
 * @property userSubmitted Whether the chat icon was submitted by a user
 */
data class ChatIcon(
		val assets: List<ChatIconAsset>? = null,
		val id: String? = null,
		val name: String? = null,
		@ChatIconSubjectTypeAnnotation val subjectType: String? = ChatIconSubjectType.OTHER,
		val author: Any? = null,
		val userSubmitted: Boolean? = true
)