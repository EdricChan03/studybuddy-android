/** Convention plugin for Android libraries using Google's Protocol Buffers. */
package com.edricchan.studybuddy.library

import com.google.devtools.ksp.gradle.KspAATask
import com.google.protobuf.gradle.GenerateProtoTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.edricchan.studybuddy.library.android")
    com.google.protobuf
}

private val libs = versionCatalogs.named("libs")

val protocArtifact = libs.findLibrary("protobuf-protoc")
    .get()
    .map { it.toString() }
    .get()

protobuf {
    protoc {
        artifact = protocArtifact
    }
    generateProtoTasks {
        all().configureEach {
            builtins {
                val java by registering {
                    option("lite")
                }
                val kotlin by registering {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    // Expose it to consumers of the library, so that we don't get "Can't resolve class
    // EnumLite" errors.
    api(libs.findLibrary("protobuf-kotlin-lite").get())
}

private val kspPluginId = libs.findPlugin("ksp").get()
    .map { it.pluginId }
    .get()

// Wire up ProtoBuf sources with KSP to get Dagger injection working
pluginManager.withPlugin(kspPluginId) {
    androidComponents {
        onVariants(selector().all()) { variant ->
            afterEvaluate {
                val variantCapName = variant.name.replaceFirstChar { it.uppercaseChar() }
                val protoTask =
                    project.tasks.named<GenerateProtoTask>("generate${variantCapName}Proto")

                project.tasks.named("ksp${variantCapName}Kotlin") {
                    dependsOn(protoTask)

                    when (this) {
                        // KSP2
                        is KspAATask -> {
                            kspConfig.javaSourceRoots.from(protoTask.map { it.outputBaseDir })
                        }

                        // KSP1
                        is KotlinCompile -> {
                            setSource(protoTask.map { it.outputBaseDir })
                        }
                    }
                }
            }
        }
    }
}
