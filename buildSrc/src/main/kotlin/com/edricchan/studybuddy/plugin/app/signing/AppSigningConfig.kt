package com.edricchan.studybuddy.plugin.app.signing

import com.android.build.api.dsl.SigningConfig
import com.android.build.api.variant.SigningConfigInfo
import com.edricchan.studybuddy.plugin.app.StringVars
import com.edricchan.studybuddy.plugin.app.data.SecretsConfig
import com.edricchan.studybuddy.plugin.app.data.SigningConfigData
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.kotlin.dsl.of
import java.io.File
import java.security.KeyStore
import javax.inject.Inject

/** Specifies the signing configuration for a specific build variant. */
abstract class AppSigningConfig @Inject constructor(
    private val providerFactory: ProviderFactory
) {
    /** Uses the signing configuration from the automatically generated `debug` keystore. */
    fun useDebugKeystore() {
        // Values from https://cs.android.com/android-studio/platform/tools/adt/idea/+/mirror-goog-studio-main:android-templates/src/com/android/tools/idea/templates/KeystoreUtils.kt
        // Keystore name: "debug.keystore"
        // Keystore password: "android"
        // Keystore alias: "androiddebugkey"
        // Key password: "android"
        storeFile.fileProvider(
            // $HOME/.android/debug.keystore
            providerFactory.systemProperty("user.home")
                .map { File(it, ".android/debug.keystore") }
        )
        storePassword.set("android")
        keyAlias.set("androiddebugkey")
        keyPassword.set("android")
        storeType.set(DEFAULT_STORE_TYPE)
    }

    /**
     * Path to the keystore file.
     * @see SigningConfig.storeFile
     */
    abstract val storeFile: RegularFileProperty

    /**
     * Path to the credentials file. This should be a [`.toml` file](https://toml.io/en/)
     * with the following key-values:
     *
     * ```toml
     * [signing]
     * keystore-password = "<keystore-pwd>"
     * keystore-alias = "<keystore-alias>"
     * keystore-alias-password = "<keystore-alias-pwd>"
     * ```
     */
    abstract val secretsFile: RegularFileProperty

    /**
     * Store password used when signing.
     * @see SigningConfig.storePassword
     */
    abstract val storePassword: Property<String>

    /**
     * Key alias used when signing.
     * @see SigningConfig.keyAlias
     */
    abstract val keyAlias: Property<String>

    /**
     * Key password used when signing.
     * @see SigningConfig.keyPassword
     */
    abstract val keyPassword: Property<String>

    /**
     * Store type used when signing.
     * @see SigningConfig.storeType
     */
    abstract val storeType: Property<String>

    /**
     * Sets the signing config to use based on the [other] parameter.
     *
     * Note: Options passed to the [other] signing config will **override** the values
     * that might be previously present in this receiver's config.
     */
    fun initWith(other: AppSigningConfig) {
        storeFile.set(other.storeFile)
        secretsFile.set(other.secretsFile)
        storePassword.set(other.storePassword)
        keyAlias.set(other.keyAlias)
        keyPassword.set(other.keyPassword)
        storeType.set(other.storeType)
    }

    /**
     * Sets the signing config to use based on the [other] lazy config parameter.
     *
     * Note: Options passed to the [other] signing config will **override** the values
     * that might be previously present in this receiver's config.
     */
    fun initWith(other: Provider<AppSigningConfig>) {
        storeFile.set(other.flatMap { it.storeFile })
        secretsFile.set(other.flatMap { it.secretsFile })
        storePassword.set(other.flatMap { it.storePassword })
        keyAlias.set(other.flatMap { it.keyAlias })
        keyPassword.set(other.flatMap { it.keyPassword })
        storeType.set(other.flatMap { it.storeType })
    }

    override fun toString(): String {
        return "AppSigningConfig(storeFile=${storeFile.orNull}, " +
            "secretsFile=${secretsFile.orNull?.asFile}, " +
            "storePassword=${storePassword.orNull}, " +
            "keyAlias=${keyAlias.orNull}, " +
            "keyPassword=${keyPassword.orNull}, " +
            "storeType=${storeType.orNull})"
    }

    /** Converts this [AppSigningConfig] DSL to its [SigningConfigInfo] equivalent. */
    fun asSigningInfo(): Provider<SigningConfigInfo> =
        providerFactory.of(SigningConfigValueSource::class) {
            parameters {
                this.storeFile.set(this@AppSigningConfig.storeFile)
                this.storePassword.set(this@AppSigningConfig.storePassword)
                this.keyAlias.set(this@AppSigningConfig.keyAlias)
                this.keyPassword.set(this@AppSigningConfig.keyPassword)
                this.storeType.set(this@AppSigningConfig.storeType)
            }
        }

    companion object {
        /** Default name of the [keystore file][storeFile] to be used. */
        const val DEFAULT_KEYSTORE_FILE = "studybuddy.jks"

        /** Default name of the [secrets configuration file][secretsFile] to be used. */
        const val DEFAULT_SECRETS_CONFIG_FILE = "secret-config.toml"

        /** Default [store type][storeType] to be used. */
        val DEFAULT_STORE_TYPE = KeyStore.getDefaultType()
    }
}

/** Sets the convention values for the lazy properties in the receiver [AppSigningConfig]. */
fun AppSigningConfig.setDefaults(
    projectDirectory: Directory,
    secretsConfig: Provider<SecretsConfig>,
    provideEnvVar: (String) -> Provider<String>
) {
    storeFile.convention(
        projectDirectory.file(
            AppSigningConfig.DEFAULT_KEYSTORE_FILE
        )
    )
    secretsFile.convention(
        projectDirectory.file(
            AppSigningConfig.DEFAULT_SECRETS_CONFIG_FILE
        )
    )
    val credentialsProperties = secretsConfig.map { it.signing }
    storePassword.convention(
        credentialsProperties.map(SigningConfigData::storePassword)
            .orElse(provideEnvVar(StringVars.ciEnvKeystorePassword))
    )
    keyAlias.convention(
        credentialsProperties.map(SigningConfigData::keyAlias)
            .orElse(provideEnvVar(StringVars.ciEnvKeystoreAlias))
    )
    keyPassword.convention(
        credentialsProperties.map(SigningConfigData::storeAliasPassword)
            .orElse(provideEnvVar(StringVars.ciEnvKeystoreAliasPassword))
    )
    storeType.convention(AppSigningConfig.DEFAULT_STORE_TYPE)
}
