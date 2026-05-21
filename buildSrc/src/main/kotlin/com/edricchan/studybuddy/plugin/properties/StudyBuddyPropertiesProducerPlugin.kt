package com.edricchan.studybuddy.plugin.properties

import com.edricchan.studybuddy.plugin.properties.metadata.tasks.WriteMetadataTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering

class StudyBuddyPropertiesProducerPlugin : StudyBuddyPropertiesBasePlugin() {
    override fun Project.onSubPluginApply(extension: StudyBuddyPropertiesExtension) {
        registerTasks(extension)
    }

    private fun Project.registerTasks(extension: StudyBuddyPropertiesExtension) {
        tasks {
            val writeStudyBuddyMetadata by registering(WriteMetadataTask::class) {
                outputFile.convention(extension.metadataFile)
                metadata.gitCommitSha.convention(extension.metadata.gitCommitSha)
                metadata.buildTimeMillis.convention(extension.metadata.buildTimeMillis)
            }
        }
    }
}
