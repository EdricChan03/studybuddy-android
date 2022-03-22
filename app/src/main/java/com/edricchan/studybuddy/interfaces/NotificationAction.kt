package com.edricchan.studybuddy.interfaces

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Specifies a notification request's action
 * @property icon The action's icon
 * @property title The action's title
 * @property type The action's type
 */
@JsonClass(generateAdapter = true)
data class NotificationAction(
    @Json(name = "actionTitle") val title: String? = null,
    @Json(name = "actionIcon") val icon: String? = null,
    @Json(name = "actionType") val type: String? = null
) {
    private constructor(builder: Builder) : this(
        builder.title,
        builder.icon,
        builder.type
    )

    companion object {
        /**
         * Creates a [NotificationAction] using a [Builder] (with support for inlined setting of variables)
         * @return The created [NotificationAction]
         */
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    // TODO: Add support for creating multiple actions using the builder
    /**
     * Builder object to simplify the creation of a [NotificationAction] object
     */
    class Builder {
        /**
         * The notification action's title
         */
        var title: String? = null
        /**
         * The notification action's icon
         */
        var icon: String? = null
        /**
         * The notification action's intent type
         */
        var type: String? = null

        /**
         * Returns the created [NotificationAction]
         * @return The created [NotificationAction]
         */
        fun build() = NotificationAction(this)
    }
}
