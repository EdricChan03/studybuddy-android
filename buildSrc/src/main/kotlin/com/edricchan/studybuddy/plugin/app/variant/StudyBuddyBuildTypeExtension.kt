package com.edricchan.studybuddy.plugin.app.variant

import com.edricchan.studybuddy.plugin.app.signing.AppSigningConfig
import org.gradle.api.Action
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.Nested

/**
 * Configuration for a build-type.
 *
 * Example usage:
 * ```kotlin
 * android {
 *   buildTypes {
 *     register("nightly") {
 *       // Make the nightly build use the debug build's signing config
 *       extensions.configure<StudyBuddyBuildTypeExtension>("studybuddyConfig") {
 *         signingConfig.initWith(named("debug").map { it.extensions.getByType<StudyBuddyBuildTypeExtension>().signingConfig })
 *       }
 *     }
 * ```
 */
abstract class StudyBuddyBuildTypeExtension : ExtensionAware {
    /** Signing configuration for this build-type. */
    @get:Nested
    abstract val signingConfig: AppSigningConfig

    /** Configures the signing configuration for this build-type. */
    fun signingConfig(action: Action<in AppSigningConfig>) {
        action.execute(signingConfig)
    }

    companion object {
        /** Name that this extension class will be registered to and accessible by. */
        val EXTENSION_NAME: String = "studybuddyConfig"
    }
}
