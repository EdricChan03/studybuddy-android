package com.edricchan.studybuddy.plugin.app.signing

import org.gradle.api.Action
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.Nested

abstract class StudyBuddyAppSigning : ExtensionAware {
    /** Default signing config to use if not explicitly specified for an AGP variant. */
    @get:Nested
    abstract val defaultConfig: AppSigningConfig

    /**
     * Configures the default signing config to use if not explicitly
     * specified for an AGP variant.
     */
    fun defaultConfig(action: Action<in AppSigningConfig>) {
        action.execute(defaultConfig)
    }
}
