package com.edricchan.studybuddy.core.settings.appearance.repo.source

import androidx.datastore.core.DataStore
import com.edricchan.studybuddy.core.settings.appearance.AppThemeSetting
import com.edricchan.studybuddy.core.settings.appearance.DarkModeSetting
import com.edricchan.studybuddy.core.settings.appearance.font.TypefaceConfig
import com.edricchan.studybuddy.core.settings.appearance.font.TypefaceSetting
import com.edricchan.studybuddy.core.settings.appearance.proto.AppearanceSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** Implementation of [AppearanceSettingsDataStore] backed by a ProtoBuf file. */
class LocalAppearanceSettingsDataStore @Inject constructor(
    private val dataStore: DataStore<@JvmSuppressWildcards AppearanceSettings>
) : AppearanceSettingsDataStore {
    override val darkMode = dataStore.data.map {
        DarkModeSetting.fromProto(it.dark_mode) ?: DarkModeSetting.FollowSystem
    }

    override suspend fun setDarkMode(value: DarkModeSetting) {
        dataStore.updateData {
            it.copy(dark_mode = value.protoValue)
        }
    }

    override val appTheme: Flow<AppThemeSetting> =
        dataStore.data.map { AppThemeSetting.fromProto(it.theme) ?: AppThemeSetting.StudyBuddy }

    override suspend fun setAppTheme(value: AppThemeSetting) {
        dataStore.updateData {
            it.copy(theme = value.protoValue)
        }
    }

    // Display settings

    override val displayTypeface = dataStore.data.map {
        TypefaceSetting.fromProto(it.display_typeface) ?: TypefaceSetting.SystemDefault
    }

    override suspend fun setDisplayTypeface(style: TypefaceSetting) {
        dataStore.updateData {
            it.copy(display_typeface = style.protoValue)
        }
    }

    override val bodyTypeface = dataStore.data.map {
        TypefaceSetting.fromProto(it.body_typeface) ?: TypefaceSetting.SystemDefault
    }

    override suspend fun setBodyTypeface(style: TypefaceSetting) {
        dataStore.updateData {
            it.copy(body_typeface = style.protoValue)
        }
    }

    override val typefaceConfig = dataStore.data.map {
        TypefaceConfig(
            displayStyle = TypefaceSetting.fromProto(it.display_typeface)
                ?: TypefaceSetting.SystemDefault,
            bodyStyle = TypefaceSetting.fromProto(it.body_typeface) ?: TypefaceSetting.SystemDefault
        )
    }

    override suspend fun setTypefaceConfig(
        displayStyle: TypefaceSetting,
        bodyStyle: TypefaceSetting
    ) {
        dataStore.updateData {
            it.copy(
                display_typeface = displayStyle.protoValue,
                body_typeface = bodyStyle.protoValue
            )
        }
    }

    override val useRelativeTimestamps = dataStore.data.map { it.use_relative_timestamps }

    override suspend fun setUseRelativeTimestamps(shouldUse: Boolean) {
        dataStore.updateData {
            it.copy(use_relative_timestamps = shouldUse)
        }
    }
}
