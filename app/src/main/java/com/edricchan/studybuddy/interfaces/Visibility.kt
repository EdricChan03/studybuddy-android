package com.edricchan.studybuddy.interfaces

/**
 * Enum class used to represent the visibility of an object
 *
 * Note: The security backend should be handled in Cloud Firestore with Security Rules
 */
class Visibility {

    companion object {
        /**
         * Enum to indicate that an object should not be visible to the public
         */
        const val PRIVATE = "private"
        /**
         * Enum to indicate that an object should be visible to the public (and should easily be searchable)
         */
        const val PUBLIC = "public"
        /**
         * Enum to indicate that an object should not be visible to the public unless explicitly accessed
         */
        const val UNLISTED = "unlisted"
    }
}