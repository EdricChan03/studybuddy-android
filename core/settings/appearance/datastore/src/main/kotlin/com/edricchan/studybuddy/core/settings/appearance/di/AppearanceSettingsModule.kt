package com.edricchan.studybuddy.core.settings.appearance.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.edricchan.studybuddy.core.settings.appearance.proto.AppearanceSettings
import com.edricchan.studybuddy.core.settings.appearance.repo.source.AppearanceSettingsDataStore
import com.edricchan.studybuddy.core.settings.appearance.repo.source.LocalAppearanceSettingsDataStore
import com.edricchan.studybuddy.core.settings.appearance.store.AppearanceSettingsDataStoreFileName
import com.edricchan.studybuddy.core.settings.appearance.store.AppearanceSettingsSerializer
import com.edricchan.studybuddy.core.settings.appearance.store.migration.AppearancePrefsMigration
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AppearanceSettingsModule {
    @Binds
    abstract fun bindAppearanceDataStore(
        datastoreImpl: LocalAppearanceSettingsDataStore
    ): AppearanceSettingsDataStore

    companion object {
        @Singleton
        @Provides
        fun provideAppearanceDataStore(
            @ApplicationContext context: Context
        ): DataStore<@JvmSuppressWildcards AppearanceSettings> = DataStoreFactory.create(
            serializer = AppearanceSettingsSerializer,
            corruptionHandler = ReplaceFileCorruptionHandler {
                AppearanceSettings()
            },
            produceFile = { context.dataStoreFile(AppearanceSettingsDataStoreFileName) },
            migrations = listOf(context.AppearancePrefsMigration)
        )
    }
}
