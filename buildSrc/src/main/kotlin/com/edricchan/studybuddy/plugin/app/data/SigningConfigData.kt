package com.edricchan.studybuddy.plugin.app.data

import com.edricchan.studybuddy.plugin.app.StringVars
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SigningConfigData(
    @SerialName(StringVars.propsKeystoreAlias)
    val keyAlias: String,
    @SerialName(StringVars.propsKeystorePassword)
    val storePassword: String,
    @SerialName(StringVars.propsKeystoreAliasPassword)
    val storeAliasPassword: String
)
