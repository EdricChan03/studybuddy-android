package com.edricchan.studybuddy.plugin.properties.metadata.tasks

import com.edricchan.studybuddy.plugin.properties.metadata.StudyBuddyAppMetadata
import com.edricchan.studybuddy.plugin.properties.metadata.tasks.work.WriteMetadataAction
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.submit
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.gradle.work.DisableCachingByDefault
import org.gradle.workers.WorkerExecutor
import javax.inject.Inject

@DisableCachingByDefault
abstract class WriteMetadataTask : DefaultTask() {
    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @get:Nested
    abstract val metadata: StudyBuddyAppMetadata

    @get:Inject
    abstract val workerExecutor: WorkerExecutor

    init {
        description = "Writes the app's metadata in JSON form to the specified output file."
        group = LifecycleBasePlugin.BUILD_GROUP
    }

    @TaskAction
    fun writeMetadata() {
        workerExecutor.noIsolation().submit(WriteMetadataAction::class) {
            outputFile.set(this@WriteMetadataTask.outputFile)
            gitCommitSha.set(this@WriteMetadataTask.metadata.gitCommitSha)
            buildTimeMillis.set(this@WriteMetadataTask.metadata.buildTimeMillis)
        }
    }
}
