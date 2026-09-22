package com.edricchan.studybuddy.plugin.app

import com.akuleshov7.ktoml.Toml
import com.akuleshov7.ktoml.source.decodeFromStream
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.ApplicationVariant
import com.android.build.api.variant.BuildConfigField
import com.android.build.api.variant.DslExtension
import com.edricchan.studybuddy.plugin.app.data.SecretsConfig
import com.edricchan.studybuddy.plugin.app.signing.AppSigningConfig
import com.edricchan.studybuddy.plugin.app.signing.setDefaults
import com.edricchan.studybuddy.plugin.app.variant.StudyBuddyBuildTypeExtension
import com.edricchan.studybuddy.plugin.app.variant.StudyBuddyVariantExtension
import com.edricchan.studybuddy.plugin.properties.metadata.StudyBuddyAppMetadata
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.logging.Logging
import org.gradle.api.problems.ProblemReporter
import org.gradle.api.problems.Problems
import org.gradle.api.provider.ProviderFactory
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.newInstance
import org.slf4j.MarkerFactory
import java.time.Instant
import javax.inject.Inject

/** Gradle Plugin which should be applied to the `:app` module. */
abstract class StudyBuddyAppPlugin : Plugin<Project> {
    companion object {
        private val logger = Logging.getLogger(StudyBuddyAppPlugin::class.java)
        private val signingLogMarker = MarkerFactory.getMarker("SIGNING")
    }

    private lateinit var agpAppExtension: ApplicationExtension

    private val nowInstant = Instant.now()

    private val toml = Toml

    @get:Inject
    protected abstract val problems: Problems

    private val reporter: ProblemReporter get() = problems.reporter

    override fun apply(target: Project) {
        with(target) {
            val appExtension =
                extensions.create<StudyBuddyAppExtension>(StudyBuddyAppExtension.EXTENSION_NAME)
                    .also { it.setDefaults(this) }

            pluginManager.withPlugin("com.android.application") {
                agpAppExtension = extensions.getByType()

                extensions.findByType<ApplicationAndroidComponentsExtension>()
                    ?.applyExtension(this@with, appExtension)
            }
        }
    }

    private fun StudyBuddyAppExtension.setDefaults(
        project: Project
    ) {
        ci.convention(project.providers.environmentVariable("CI").map { it.toBoolean() })
        metadata.setDefaults(project)

        signing.defaultConfig.setDefaults(project)
    }

    private fun AppSigningConfig.setDefaults(
        project: Project
    ) {
        setDefaults(
            projectDirectory = project.rootProject.layout.projectDirectory,
            secretsConfig = secretsFile.map { it.parseSecretsConfigOrNull() },
            provideEnvVar = project.providers::environmentVariable,
        )
    }

    private fun StudyBuddyAppMetadata.setDefaults(
        project: Project
    ) {
        val gitCommitShaExec = project.providers.execGitCommitSha()
        buildTimeMillis.convention(nowInstant.toEpochMilli())
        gitCommitSha.convention(
            gitCommitShaExec.standardOutput.asText.map { it.replace("\n", "") }
        )
    }

    private fun ProviderFactory.execGitCommitSha() = exec {
        executable = "git"
        args = listOf("rev-parse", "HEAD")
    }

    private fun ApplicationAndroidComponentsExtension.applyExtension(
        project: Project,
        extension: StudyBuddyAppExtension
    ) {
        val buildTime = extension.metadata.buildTimeMillis.orElse(nowInstant.toEpochMilli())
        val gitCommitSha = extension.metadata.gitCommitSha
        val isCi = extension.ci.getOrElse(false)

        finalizeDsl { androidAppExt ->
            with(androidAppExt) {
                lint {
                    abortOnError = false
                    baseline = project.file("lint-baseline.xml")
                }
            }
        }

        registerExtension(
            DslExtension.Builder(StudyBuddyBuildTypeExtension.EXTENSION_NAME)
                .extendBuildTypeWith(StudyBuddyBuildTypeExtension::class.java)
                .build()
        ) {
            project.objects.newInstance(StudyBuddyVariantExtension::class).apply {
                val buildTypeExt =
                    it.buildTypeExtension(StudyBuddyBuildTypeExtension::class.java)
                buildTypeExt.signingConfig.setDefaults(project)

                if (it.variant.buildType == "debug") {
                    logger.info(
                        signingLogMarker,
                        "Using default debug keystore for debug build-type"
                    )
                    buildTypeExt.signingConfig.useDebugKeystore()
                }

                signingConfig.initWith(buildTypeExt.signingConfig)
            }
        }

        val SigningConfigAllAction = Action<ApplicationVariant> {
            val config = getExtension(StudyBuddyVariantExtension::class.java)?.signingConfig
                ?: run {
                    logger.info(
                        signingLogMarker,
                        "Signing config $name does not exist, falling back to the default config"
                    )
                    extension.signing.defaultConfig
                }
            signingConfig.from(config.asSigningInfo())
        }

        val AllAction = Action<ApplicationVariant> {
            buildConfigFields?.apply {
                put(
                    StringVars.buildConfigBuildTime,
                    buildTime.map {
                        BuildConfigField(
                            type = "long",
                            value = it,
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

            lifecycleTasks.registerPreBuild(project.rootProject.tasks.named("writeStudyBuddyMetadata"))
        }

        onVariants(callback = AllAction)
        onVariants(callback = SigningConfigAllAction)
    }

    private fun RegularFile.parseSecretsConfigOrNull(): SecretsConfig? =
        asFile.let { file ->
            runCatching { toml.decodeFromStream<SecretsConfig>(file.inputStream()) }.onFailure {
                reporter.report(StudyBuddyAppProblemIds.MissingSecretsConfig) {
                    details("The requested secrets configuration file at $file is missing or could not be read")
                    solution("Create a secrets-config.toml file at the specified directory, or ensure that you have the relevant read permissions")
                    fileLocation(file.path)
                    withException(it)
                }
            }.getOrNull()
        }
}
