package com.edricchan.studybuddy.interfaces.chat.icon

/**
 * Data class used to represent a default chat icon.
 * @param assets The list of icon assets (See [DefaultChatIconAssets] for more info)
 * @param id The ID of the chat icon
 * @param name The name of the chat icon (if the chat icon is a default)
 * @param subjectType The type of subject that the chat icon represents
 * @param author The author (Can be a string or a [com.google.firebase.firestore.DocumentReference])
 */
data class DefaultChatIcon(
		val assets: List<DefaultChatIconAssets>? = null,
		val id: String? = null,
		val name: String? = null,
		@DefaultChatIconSubjectTypeAnnotation val subjectType: String? = DefaultChatIconSubjectType.OTHER,
		val author: Any? = null
)