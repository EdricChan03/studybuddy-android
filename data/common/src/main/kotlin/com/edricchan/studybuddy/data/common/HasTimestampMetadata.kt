package com.edricchan.studybuddy.data.common

import com.google.firebase.Timestamp

/**
 * Indicates whether a data class should have timestamp metadata included
 * @property createdAt The timestamp the data class' Firestore document was created on
 * @property lastModified The timestamp the data class' Firestore document was last modified at
 */
interface HasTimestampMetadata {
    val createdAt: Timestamp?
    val lastModified: Timestamp?
}
