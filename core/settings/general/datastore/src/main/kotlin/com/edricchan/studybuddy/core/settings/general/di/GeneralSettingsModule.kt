package com.edricchan.studybuddy.core.settings.general.di

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.edricchan.studybuddy.core.settings.general.proto.GeneralSettings
import com.edricchan.studybuddy.core.settings.general.repo.source.GeneralSettingsDataSource
import com.edricchan.studybuddy.core.settings.general.repo.source.LocalGeneralSettingsDataSource
import com.edricchan.studybuddy.core.settings.general.store.GeneralSettingsDataStoreFileName
import com.edricchan.studybuddy.core.settings.general.store.GeneralSettingsSerializer
import com.edricchan.studybuddy.core.settings.general.store.migration.GeneralPrefsMigration
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class GeneralSettingsModule {
    @Binds
    abstract fun bindGeneralDataStore(
        datastoreImpl: LocalGeneralSettingsDataSource
    ): GeneralSettingsDataSource

    companion object {
        @Singleton
        @Provides
        fun provideGeneralDataStore(
            @ApplicationContext context: Context
        ): DataStore<@JvmSuppressWildcards GeneralSettings> = DataStoreFactory.create(
            serializer = GeneralSettingsSerializer,
            corruptionHandler = ReplaceFileCorruptionHandler {
                Log.w(
                    "GeneralSettingsModule",
                    "General settings data is corrupted, using default instance",
                    it
                )
                GeneralSettings()
            },
            produceFile = { context.dataStoreFile(GeneralSettingsDataStoreFileName) },
            migrations = listOf(context.GeneralPrefsMigration)
        )
    }
}
