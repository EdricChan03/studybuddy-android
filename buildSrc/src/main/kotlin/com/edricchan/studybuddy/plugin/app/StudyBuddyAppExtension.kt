package com.edricchan.studybuddy.plugin.app

import com.edricchan.studybuddy.plugin.app.signing.StudyBuddyAppSigning
import com.edricchan.studybuddy.plugin.properties.metadata.StudyBuddyAppMetadata
import org.gradle.api.Action
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested

abstract class StudyBuddyAppExtension {
    /**
     * Whether the build is running in a Continuous Integration environment (e.g.
     * GitHub Actions).
     *
     * If this value is not set, it defaults to the presence of the `CI` environment variable
     * set to the value `true`.
     */
    // We can't have getters named "is[Name]"...
    // TODO: Rename to isCi
    abstract val ci: Property<Boolean>

    /** Signing configuration for the application. */
    @get:Nested
    abstract val signing: StudyBuddyAppSigning

    /** Configures the signing configuration for the application. */
    fun signing(action: Action<in StudyBuddyAppSigning>) {
        action.execute(signing)
    }

    /** Metadata to be applied to the build artifacts. */
    @get:Nested
    abstract val metadata: StudyBuddyAppMetadata

    /** Configures the [metadata] to be applied to the build artifacts. */
    fun metadata(action: Action<in StudyBuddyAppMetadata>) = action.execute(metadata)

    companion object {
        const val EXTENSION_NAME = "studybuddyApp"
    }
}
