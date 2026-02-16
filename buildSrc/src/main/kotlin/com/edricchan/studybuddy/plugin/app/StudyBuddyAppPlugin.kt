package com.edricchan.studybuddy.plugin.app

import com.akuleshov7.ktoml.Toml
import com.akuleshov7.ktoml.source.decodeFromStream
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.ApplicationVariant
import com.android.build.api.variant.BuildConfigField
import com.edricchan.studybuddy.plugin.app.data.SecretsConfig
import com.edricchan.studybuddy.plugin.app.data.SigningConfigData
import com.edricchan.studybuddy.plugin.app.signing.AppSigningConfig
import com.edricchan.studybuddy.plugin.app.signing.configure
import com.edricchan.studybuddy.plugin.properties.metadata.StudyBuddyAppMetadata
import org.gradle.api.Action
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.logging.Logging
import org.gradle.api.provider.ProviderFactory
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import java.time.Instant

/** Gradle Plugin which should be applied to the `:app` module. */
class StudyBuddyAppPlugin : Plugin<Project> {
    companion object {
        private val logger = Logging.getLogger(StudyBuddyAppPlugin::class.java)
    }

    private lateinit var agpAppExtension: ApplicationExtension

    private val nowInstant = Instant.now()

    private val toml = Toml

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
        buildTypesSigning.convention(StudyBuddyAppExtension.DEFAULT_BUILD_TYPES)
        metadata.setDefaults(project)

        with(signingConfigs) {
            configureEach {
                storeFile.convention(
                    project.rootProject.layout.projectDirectory.file(
                        AppSigningConfig.DEFAULT_KEYSTORE_FILE
                    )
                )
                secretsFile.convention(
                    project.rootProject.layout.projectDirectory.file(
                        AppSigningConfig.DEFAULT_SECRETS_CONFIG_FILE
                    )
                )
                val secretsConfig =
                    secretsFile.map { it.parseSecretsConfigOrNull() }
                val credentialsProperties = secretsConfig.map { it.signing }
                storePassword.convention(
                    credentialsProperties.map(SigningConfigData::storePassword)
                        .orElse(project.providers.environmentVariable(StringVars.ciEnvKeystorePassword))
                )
                keyAlias.convention(
                    credentialsProperties.map(SigningConfigData::keyAlias)
                        .orElse(project.providers.environmentVariable(StringVars.ciEnvKeystoreAlias))
                )
                keyPassword.convention(
                    credentialsProperties.map(SigningConfigData::storeAliasPassword)
                        .orElse(project.providers.environmentVariable(StringVars.ciEnvKeystoreAliasPassword))
                )
                storeType.convention(AppSigningConfig.DEFAULT_STORE_TYPE)
            }

            try {
                register(StudyBuddyAppExtension.DEFAULT_SIGNING_CONFIG_NAME)
            } catch (e: InvalidUserDataException) {
                logger.info(
                    "Default signing config " +
                        "${StudyBuddyAppExtension.DEFAULT_SIGNING_CONFIG_NAME} found, skipping registration"
                )
            }
        }
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
                    textReport = isCi
                    sarifReport = isCi
                    abortOnError = false
                    baseline = project.file("lint-baseline.xml")
                }

                signingConfigs {
                    extension.signingConfigs.forEach {
                        register(it.name) {
                            configure(it)
                        }
                    }
                }
            }
        }

        val SigningConfigVariantAction = Action<ApplicationVariant> {
            val appSignConfig = agpAppExtension.signingConfigs.findByName(name)
                ?: agpAppExtension.signingConfigs[StudyBuddyAppExtension.DEFAULT_SIGNING_CONFIG_NAME]
            signingConfig.setConfig(appSignConfig)
        }

        // This Action will be called regardless of whether the build type is
        // specified in `extension.buildTypesSigning`
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
        }

        extension.buildTypesSigning.getOrElse(emptySet()).forEach {
            onVariants(
                selector = selector().withBuildType(it),
                callback = SigningConfigVariantAction
            )
        }

        onVariants(callback = AllAction)
    }

    private fun RegularFile.parseSecretsConfigOrNull(): SecretsConfig? =
        asFile.let { file ->
            runCatching { toml.decodeFromStream<SecretsConfig>(file.inputStream()) }.onFailure {
                logger.warn(
                    "Could not read configuration file at $file, defaulting to environment variables",
                    it
                )
            }.getOrNull()
        }
}
