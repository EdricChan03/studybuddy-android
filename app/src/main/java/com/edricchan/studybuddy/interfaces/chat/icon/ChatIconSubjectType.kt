package com.edricchan.studybuddy.interfaces.chat.icon

/**
 * Enum class used to represent a subject type of a [ChatIcon] object
 */
class ChatIconSubjectType {
    companion object {
        /**
         * Indicates that the icon represents Mathematics
         */
        const val MATHEMATICS = "mathematics"

        /**
         * Indicates that the icon represents Biology
         */
        const val BIOLOGY = "biology"
        /**
         * Indicates that the icon represents Chemistry
         */
        const val CHEMISTRY = "chemistry"
        /**
         * Indicates that the icon represents Physics
         */
        const val PHYSICS = "physics"
        /**
         * Indicates that the icon represents Astronomy
         */
        const val ASTRONOMY = "astronomy"

        /**
         * Indicates that the icon represents English
         */
        const val ENGLISH = "english"
        /**
         * Indicates that the icon represents Literature
         */
        const val LITERATURE = "literature"
        /**
         * Indicates that the icon represents Language Studies
         */
        const val LANGUAGE_STUDIES = "language_studies"

        /**
         * Indicates that the icon represents Computer Science
         */
        const val COMPUTER_SCIENCE = "computer_science"
        /**
         * Indicates that the icon represents Electronics
         */
        const val ELECTRONICS = "electronics"
        /**
         * Indicates that the icon represents Music
         */
        const val MUSIC = "music"
        /**
         * Indicates that the icon represents Art
         */
        const val ART = "art"

        /**
         * Indicates that the icon represents Physical Education
         */
        const val PHYSICAL_EDUCATION = "physical_education"

        /**
         * Indicates that the icon represents Geography
         */
        const val GEOGRAPHY = "geography"
        /**
         * Indicates that the icon represents Social Studies
         */
        const val SOCIAL_STUDIES = "social_studies"

        /**
         * Indicates that the icon represents something else not in the list of available subject types
         */
        const val OTHER = "other"
    }
}