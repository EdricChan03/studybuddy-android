package com.edricchan.studybuddy.plugin.app.variant

import com.android.build.api.variant.VariantExtension
import com.edricchan.studybuddy.plugin.app.signing.AppSigningConfig
import org.gradle.api.tasks.Nested

/**
 * StudyBuddy-specific options for a specific AGP variant.
 *
 * Currently, it supports specifying a signing configuration via build-types -
 * see [StudyBuddyBuildTypeExtension].
 */
abstract class StudyBuddyVariantExtension : VariantExtension {
    /** The [AppSigningConfig] to use for this variant. */
    @get:Nested
    abstract val signingConfig: AppSigningConfig
}
