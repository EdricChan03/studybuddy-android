package com.edricchan.studybuddy.plugin.app.signing

import com.android.build.api.dsl.SigningConfig
import org.gradle.api.Named
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import java.io.File
import java.security.KeyStore
import kotlin.reflect.KProperty

// Gradle's Kotlin DSL does have existing getValue/setValue extension functions, but:
// * getValue calls get() internally which throws an error if the provider
//   doesn't have a value set
// * setValue expects a non-null value to be set
private operator fun <T : Any> Property<T>.getValue(thisRef: Any, kProperty: KProperty<*>) =
    orNull

private operator fun <T : Any> Property<T>.setValue(
    thisRef: Any,
    kProperty: KProperty<*>,
    value: T?
) {
    set(value)
}

private operator fun RegularFileProperty.getValue(thisRef: Any, kProperty: KProperty<*>): File? =
    orNull?.asFile

private operator fun RegularFileProperty.setValue(
    thisRef: Any,
    kProperty: KProperty<*>,
    value: File?
) {
    set(value)
}

abstract class AppSigningConfig : Named {
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

    /**
     * Converts this [AppSigningConfig] DSL to its Android Gradle Plugin [SigningConfig]
     * equivalent, with its values backed by Gradle's managed properties.
     */
    val asAgpConfig: SigningConfig by lazy {
        object : SigningConfig {
            override var keyAlias: String? by this@AppSigningConfig.keyAlias
            override var keyPassword: String? by this@AppSigningConfig.keyPassword
            override var storeFile: File? by this@AppSigningConfig.storeFile
            override var storePassword: String? by this@AppSigningConfig.storePassword
            override var storeType: String? by this@AppSigningConfig.storeType

            override fun initWith(that: SigningConfig) {
                keyAlias = that.keyAlias
                keyPassword = that.keyPassword
                storeFile = that.storeFile
                storePassword = that.storePassword
                storeType = that.storeType
            }

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

fun SigningConfig.configure(appSigningConfig: AppSigningConfig) {
    keyAlias = appSigningConfig.keyAlias.orNull
    keyPassword = appSigningConfig.keyPassword.orNull
    storeFile = appSigningConfig.storeFile.orNull?.asFile
    storePassword = appSigningConfig.storePassword.orNull
    storeType = appSigningConfig.storeType.orNull
}
