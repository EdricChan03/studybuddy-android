package com.edricchan.studybuddy.core.metadata.updates.source

import com.edricchan.studybuddy.core.metadata.updates.AppVersion
import com.edricchan.studybuddy.core.settings.updates.proto.UpdateMetadata.AppVersion as AppVersionProto

fun AppVersionProto.toDomain(): AppVersion = AppVersion(
    code = version_code,
    name = version_name,
    source = source.toDomain()
)

fun AppVersion.toProto(): AppVersionProto = AppVersionProto(
    version_code = code,
    version_name = name,
    source = source.toProto()
)

fun AppVersionProto.InstalledSource.toDomain(): AppVersion.Source = when (this) {
    AppVersionProto.InstalledSource.Unknown -> AppVersion.Source.Unknown
    AppVersionProto.InstalledSource.GitHub -> AppVersion.Source.GitHub
    AppVersionProto.InstalledSource.FDroid -> AppVersion.Source.FDroid
    AppVersionProto.InstalledSource.PlayStore -> AppVersion.Source.PlayStore
}

fun AppVersion.Source.toProto(): AppVersionProto.InstalledSource = when (this) {
    AppVersion.Source.Unknown -> AppVersionProto.InstalledSource.Unknown
    AppVersion.Source.GitHub -> AppVersionProto.InstalledSource.GitHub
    AppVersion.Source.FDroid -> AppVersionProto.InstalledSource.FDroid
    AppVersion.Source.PlayStore -> AppVersionProto.InstalledSource.PlayStore
}
