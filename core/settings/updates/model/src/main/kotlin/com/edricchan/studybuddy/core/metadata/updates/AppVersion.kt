package com.edricchan.studybuddy.core.metadata.updates

data class AppVersion(
    val code: Long,
    val name: String,
    val source: Source = Source.Unknown
) {
    enum class Source {
        Unknown,
        GitHub,
        FDroid,
        PlayStore
    }
}
