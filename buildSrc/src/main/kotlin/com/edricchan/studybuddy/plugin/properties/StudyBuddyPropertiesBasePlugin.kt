package com.edricchan.studybuddy.plugin.properties

import com.edricchan.studybuddy.plugin.properties.metadata.StudyBuddyAppMetadata
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.provider.ProviderFactory
import org.gradle.kotlin.dsl.create
import java.time.Instant
import kotlin.io.path.inputStream

sealed class StudyBuddyPropertiesBasePlugin(
    private val ignoreMetadataFile: Boolean = true
) : Plugin<Project> {
    internal companion object {
        inline fun <reified T> logger(): Logger = Logging.getLogger(T::class.java)

        val nowInstant: Instant = Instant.now()

        private val logger = logger<StudyBuddyPropertiesBasePlugin>()
    }

    protected lateinit var extension: StudyBuddyPropertiesExtension

    final override fun apply(target: Project) {
        with(target) {
            extension = extensions.create<StudyBuddyPropertiesExtension>(
                StudyBuddyPropertiesExtension.EXTENSION_NAME
            )

            extension.setConventions(this)

            if (!ignoreMetadataFile) extension.setDefaultsFromFile()

            target.onSubPluginApply(extension)
        }
    }

    abstract fun Project.onSubPluginApply(extension: StudyBuddyPropertiesExtension)

    private fun StudyBuddyPropertiesExtension.setConventions(
        project: Project
    ) {
        metadataFile.convention(
            project.rootProject.layout.buildDirectory.file(
                "studybuddy-metadata/${StudyBuddyPropertiesExtension.DEFAULT_METADATA_FILE_NAME}"
            )
        )
        metadata {
            val gitCommitShaExec = project.rootProject.providers.execGitCommitSha()
            val sha = gitCommitShaExec.standardOutput.asText.map { it.replace("\n", "") }
            logger.info(
                """
                Metadata info:
                * User-specified build time: ${buildTimeMillis.orNull}, now: $nowInstant
                * User-specified commit SHA: ${gitCommitSha.orNull}, current HEAD commit SHA: ${sha.orNull}
                """.trimIndent()
            )
            buildTimeMillis.convention(nowInstant.toEpochMilli())
            gitCommitSha.convention(sha)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun StudyBuddyPropertiesExtension.setDefaultsFromFile() {
        metadataFile.map { it.asFile.toPath() }.orNull.let { path ->
            metadata {
                runCatching {
                    if (path != null) {
                        val json = Json.decodeFromStream<StudyBuddyAppMetadata.AppMetadataJson>(
                            path.inputStream()
                        )
                        buildTimeMillis.convention(json.buildTime.toEpochMilli())
                        gitCommitSha.convention(json.gitCommitSha)

                        return@metadata
                    }
                }.onFailure {
                    logger.info("Could not read from metadata file, assuming conventions", it)
                }
            }
        }
    }

    private fun ProviderFactory.execGitCommitSha() = exec {
        executable = "git"
        args = listOf("rev-parse", "HEAD")
    }
}
