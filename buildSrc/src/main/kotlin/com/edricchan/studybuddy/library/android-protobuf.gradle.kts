/** Convention plugin for Android libraries using Google's Protocol Buffers. */
package com.edricchan.studybuddy.library

import com.google.devtools.ksp.gradle.KspAATask
import com.squareup.wire.gradle.WireTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.edricchan.studybuddy.library.android")
    com.squareup.wire
}

private val libs = versionCatalogs.named("libs")

dependencies {
    api(libs.findLibrary("wire-runtime").get())
}

wire {
    kotlin {}
}

private val kspPluginId = libs.findPlugin("ksp").get()
    .map { it.pluginId }
    .get()

private fun <T : Any> Task.addKspDependsOn(
    other: TaskProvider<out Task>,
    provider: Provider<T>
) {
    dependsOn(other)

    when (this) {
        // KSP2
        is KspAATask -> {
            kspConfig.javaSourceRoots.from(provider)
        }

        // KSP1
        is KotlinCompile -> {
            setSource(provider)
        }
    }
}

// Wire up ProtoBuf sources with KSP to get Dagger injection working
pluginManager.withPlugin(kspPluginId) {
    androidComponents {
        onVariants(selector().all()) { variant ->
            afterEvaluate {
                tasks.named(variant.computeTaskName("ksp", "kotlin")) {
                    val wireTask = tasks.named<WireTask>(
                        variant.computeTaskName(
                            "generate",
                            "protos"
                        )
                    )
                    addKspDependsOn(
                        wireTask,
                        wireTask.map { it.outputDirectories }
                    )
                }
            }
        }
    }
}
