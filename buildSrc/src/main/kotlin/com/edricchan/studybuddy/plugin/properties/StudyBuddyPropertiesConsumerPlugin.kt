package com.edricchan.studybuddy.plugin.properties

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.BuildConfigField
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.api.variant.Variant
import com.android.build.api.variant.VariantBuilder
import com.edricchan.studybuddy.plugin.app.StringVars
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType

class StudyBuddyPropertiesConsumerPlugin : StudyBuddyPropertiesBasePlugin(
    ignoreMetadataFile = false
) {
    override fun Project.onSubPluginApply(extension: StudyBuddyPropertiesExtension) {
        with(extensions) {
            pluginManager.withPlugin("com.android.application") {
                findByType<ApplicationAndroidComponentsExtension>()?.apply {
                    enableBuildConfig()
                    applyExtension(extension)
                }
            }
            pluginManager.withPlugin("com.android.library") {
                findByType<LibraryAndroidComponentsExtension>()?.apply {
                    enableBuildConfig()
                    applyExtension(extension)
                }
            }
        }
    }

    private fun <T : AndroidComponentsExtension<*, VBuilderT, VTypeT>,
        VBuilderT : VariantBuilder, VTypeT : Variant>
        T.applyExtension(
        extension: StudyBuddyPropertiesExtension
    ) {
        val buildTime =
            extension.metadata.buildInstant.orElse(nowInstant)
        val gitCommitSha = extension.metadata.gitCommitSha.orElse("")

        val AllAction = Action<VTypeT> {
            buildConfigFields?.apply {
                put(
                    StringVars.buildConfigBuildTime,
                    buildTime.map {
                        BuildConfigField(
                            type = "long",
                            value = it.toEpochMilli(),
                            comment = "The build time in epoch milli-seconds."
                        )
                    }
                )

                put(
                    StringVars.buildConfigGitCommitSha,
                    gitCommitSha.orElse("").map {
                        BuildConfigField(
                            type = "String",
                            // Quotes must be included
                            value = "\"$it\"",
                            comment = "The current Git commit SHA."
                        )
                    }
                )
            }
        }
        onVariants(callback = AllAction)
    }

    private fun LibraryAndroidComponentsExtension.enableBuildConfig() {
        finalizeDsl {
            it.buildFeatures.buildConfig = true
        }
    }

    private fun ApplicationAndroidComponentsExtension.enableBuildConfig() {
        finalizeDsl {
            it.buildFeatures.buildConfig = true
        }
    }
}
