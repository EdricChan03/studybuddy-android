package com.edricchan.studybuddy.plugin.properties

import com.edricchan.studybuddy.plugin.properties.metadata.StudyBuddyAppMetadata
import org.gradle.api.Action
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Nested

abstract class StudyBuddyPropertiesExtension {
    /** Configures the metadata for this project. */
    @get:Nested
    abstract val metadata: StudyBuddyAppMetadata

    /** Configures the metadata for this project. */
    fun metadata(action: Action<in StudyBuddyAppMetadata>) {
        action.execute(metadata)
    }

    /** The file which stores the metadata for the project. */
    abstract val metadataFile: RegularFileProperty

    companion object {
        const val EXTENSION_NAME = "studybuddyProperties"

        const val DEFAULT_METADATA_FILE_NAME = "studybuddy-metadata.json"
    }
}
