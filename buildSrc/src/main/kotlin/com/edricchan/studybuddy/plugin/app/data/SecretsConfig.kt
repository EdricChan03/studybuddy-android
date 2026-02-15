package com.edricchan.studybuddy.plugin.app.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SecretsConfig(
    @SerialName("signing")
    val signing: SigningConfigData
)
