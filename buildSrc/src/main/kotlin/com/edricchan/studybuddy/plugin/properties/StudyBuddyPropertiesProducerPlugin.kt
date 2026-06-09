package com.edricchan.studybuddy.plugin.properties

import com.edricchan.studybuddy.plugin.properties.metadata.tasks.WriteMetadataTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.register

class StudyBuddyPropertiesProducerPlugin : StudyBuddyPropertiesBasePlugin() {
    override fun Project.onSubPluginApply(extension: StudyBuddyPropertiesExtension) {
        registerTasks(extension)
    }

    private fun Project.registerTasks(extension: StudyBuddyPropertiesExtension) {
        tasks {
            register<WriteMetadataTask>("writeStudyBuddyMetadata") {
                outputFile.convention(extension.metadataFile)
                metadata.gitCommitSha.convention(extension.metadata.gitCommitSha)
                metadata.buildTimeMillis.convention(extension.metadata.buildTimeMillis)
            }
        }
    }
}
