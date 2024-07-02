package com.edricchan.studybuddy.core.resources.notification

import androidx.core.app.NotificationManagerCompat

// TODO: Move to library
enum class NotificationImportance(val value: Int) {
    Unspecified(NotificationManagerCompat.IMPORTANCE_UNSPECIFIED),
    None(NotificationManagerCompat.IMPORTANCE_NONE),
    Min(NotificationManagerCompat.IMPORTANCE_MIN),
    Low(NotificationManagerCompat.IMPORTANCE_LOW),
    Default(NotificationManagerCompat.IMPORTANCE_DEFAULT),
    High(NotificationManagerCompat.IMPORTANCE_HIGH),
    Max(NotificationManagerCompat.IMPORTANCE_MAX)
}
