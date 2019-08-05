package com.edricchan.studybuddy.interfaces.chat.icon

/**
 * Data class used to represent an icon asset in [ChatIcon].
 * @property fileType The file type of the asset
 * @property size The size of the asset (e.g. `1px`, `3px`, etc.)
 * @property src The src of the asset (ideally a Firebase Storage download URL)
 * @property storageLocation The Firebase storage location of the asset
 */
data class ChatIconAsset(
		val fileType: String? = null,
		val size: String? = "1px",
		val src: String? = null,
		val storageLocation: String? = null
)