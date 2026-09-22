package com.edricchan.studybuddy.plugin.app.signing

import com.android.build.api.variant.SigningConfigInfo
import org.gradle.api.Describable
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters

abstract class SigningConfigValueSource :
    ValueSource<SigningConfigInfo, SigningConfigValueSource.Parameters>, Describable {
    override fun obtain(): SigningConfigInfo? {
        return SigningConfigInfo(
            storeFile = parameters.storeFile.get().asFile,
            storePassword = parameters.storePassword.get(),
            keyAlias = parameters.keyAlias.get(),
            keyPassword = parameters.keyPassword.get(),
            storeType = parameters.storeType.get(),
        )
    }

    override fun getDisplayName(): String {
        return "signing configuration via app extension DSL"
    }

    interface Parameters : ValueSourceParameters {
        val keyAlias: Property<String>
        val keyPassword: Property<String>
        val storeFile: RegularFileProperty
        val storePassword: Property<String>
        val storeType: Property<String>
    }
}
