package com.edricchan.studybuddy.plugin.properties.metadata.tasks.work

import com.edricchan.studybuddy.plugin.properties.metadata.StudyBuddyAppMetadata
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.logging.Logging
import org.gradle.api.provider.Property
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters
import java.time.Instant

abstract class WriteMetadataAction : WorkAction<WriteMetadataAction.Parameters> {
    private companion object {
        val logger = Logging.getLogger(WriteMetadataAction::class.java)
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun execute() {
        val json = parameters.asMetadataJson()
        val outputFile = parameters.outputFile.asFile.get()

        Json.encodeToStream(json, outputFile.outputStream())

        logger.lifecycle("Written metadata to $outputFile")
    }

    interface Parameters : WorkParameters {
        val gitCommitSha: Property<String>
        val buildTimeMillis: Property<Long>

        val outputFile: RegularFileProperty
    }
}

private fun WriteMetadataAction.Parameters.asMetadataJson() = StudyBuddyAppMetadata.AppMetadataJson(
    gitCommitSha = gitCommitSha.get(),
    buildTime = Instant.ofEpochMilli(buildTimeMillis.get())
)
