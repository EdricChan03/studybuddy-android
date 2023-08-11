package com.edricchan.studybuddy.extensions

import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationChannelGroupCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Creates a [NotificationChannelCompat] with the given parameters.
 * @param id The notification channel ID.
 * @param importance The notification channel's importance.
 * @param init Additional configuration to be passed to [NotificationChannelCompat.Builder].
 * @return The created [NotificationChannelCompat].
 */
inline fun notificationChannelCompat(
    id: String,
    importance: Int,
    init: NotificationChannelCompat.Builder.() -> Unit
) = NotificationChannelCompat.Builder(id, importance).apply(init).build()

/**
 * Creates a [NotificationChannelCompat] with the given parameters.
 * This overload allows for a [name] and [description] to be specified.
 * @param id The notification channel ID.
 * @param name The notification channel name.
 * @param description The notification channel description.
 * @param importance The notification channel's importance.
 * @param init Additional configuration to be passed to [NotificationChannelCompat.Builder].
 * @return The created [NotificationChannelCompat].
 */
inline fun notificationChannelCompat(
    id: String,
    name: String,
    description: String? = null,
    importance: Int,
    init: NotificationChannelCompat.Builder.() -> Unit
) = notificationChannelCompat(id, importance) {
    init()
    setName(name)
    setDescription(description)
}

/**
 * Creates a notification channel with the given parameters. This method is shorthand
 * for calling [NotificationManagerCompat.createNotificationChannel] with the resulting call
 * of [notificationChannelCompat].
 * @param id The notification channel ID.
 * @param importance The notification channel's importance.
 * @param init Additional configuration to be passed to [NotificationChannelCompat.Builder].
 */
inline fun NotificationManagerCompat.createNotificationChannel(
    id: String,
    importance: Int,
    init: NotificationChannelCompat.Builder.() -> Unit
) = createNotificationChannel(notificationChannelCompat(id, importance, init))

/**
 * Creates a [NotificationChannelGroupCompat] with the given parameters.
 * @param id The notification channel group ID.
 * @param init Additional configuration to be passed to [NotificationChannelGroupCompat.Builder].
 * @return The created [NotificationChannelGroupCompat].
 */
inline fun notificationChannelGroupCompat(
    id: String,
    init: NotificationChannelGroupCompat.Builder.() -> Unit
) = NotificationChannelGroupCompat.Builder(id).apply(init).build()

/**
 * Creates a [NotificationChannelGroupCompat] with the given parameters. This overload
 * allows for a [name] and [description] to be specified.
 * @param id The notification channel group ID.
 * @param name The notification channel group's name.
 * @param description The notification channel group's description.
 * @param init Additional configuration to be passed to [NotificationChannelGroupCompat.Builder].
 * @return The created [NotificationChannelGroupCompat].
 */
inline fun notificationChannelGroupCompat(
    id: String,
    name: String,
    description: String? = null,
    // Lambda is optional as NotificationChannelGroup only has two properties
    init: NotificationChannelGroupCompat.Builder.() -> Unit = {}
) = notificationChannelGroupCompat(id) {
    init()
    setName(name)
    setDescription(description)
}

/**
 * Creates a notification channel group with the given parameters. This method is shorthand
 * for calling [NotificationManagerCompat.createNotificationChannelGroup] with the resulting call
 * of [notificationChannelGroupCompat].
 * @param id The notification channel group ID.
 * @param init Additional configuration to be passed to [NotificationChannelCompat.Builder].
 */
inline fun NotificationManagerCompat.createNotificationChannelGroup(
    id: String,
    init: NotificationChannelGroupCompat.Builder.() -> Unit
) = createNotificationChannelGroup(notificationChannelGroupCompat(id, init))
