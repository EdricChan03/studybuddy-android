package com.edricchan.studybuddy.core.settings.updates.di

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.edricchan.studybuddy.core.settings.updates.proto.UpdateSettings
import com.edricchan.studybuddy.core.settings.updates.source.LocalUpdateSettingsDataSource
import com.edricchan.studybuddy.core.settings.updates.source.UpdateSettingsDataSource
import com.edricchan.studybuddy.core.settings.updates.source.store.UpdatesDataStoreFileName
import com.edricchan.studybuddy.core.settings.updates.source.store.UpdatesSettingsSerializer
import com.edricchan.studybuddy.core.settings.updates.source.store.updatePrefsMigration
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UpdateSettingsModule {
    @Singleton
    @Binds
    abstract fun bindUpdateSettingsDataSource(
        impl: LocalUpdateSettingsDataSource
    ): UpdateSettingsDataSource

    internal companion object {
        @Singleton
        @Provides
        fun provideUpdatesDataStore(
            @ApplicationContext context: Context
        ): DataStore<@JvmSuppressWildcards UpdateSettings> = DataStoreFactory.create(
            serializer = UpdatesSettingsSerializer,
            corruptionHandler = ReplaceFileCorruptionHandler {
                Log.w(
                    "UpdateSettings",
                    "provideUpdatesDataStore: Update settings data is corrupted. " +
                        "Using default instance",
                    it
                )
                UpdateSettings()
            },
            produceFile = { context.dataStoreFile(UpdatesDataStoreFileName) },
            migrations = listOf(context.updatePrefsMigration)
        )
    }
}
