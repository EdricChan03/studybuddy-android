package com.edricchan.studybuddy.interfaces

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Specifies a notification request's action
 * @property icon The action's icon
 * @property title The action's title
 * @property type The action's type
 */
@Serializable
data class NotificationAction(
    @SerialName("actionTitle") val title: String? = null,
    @SerialName("actionIcon") val icon: String? = null,
    @SerialName("actionType") val type: String? = null
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
