package com.edricchan.studybuddy.core.metadata.updates.di

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.edricchan.studybuddy.core.metadata.updates.source.LocalUpdateMetadataDataSource
import com.edricchan.studybuddy.core.metadata.updates.source.UpdateMetadataDataSource
import com.edricchan.studybuddy.core.metadata.updates.source.store.SeedMetadataMigration
import com.edricchan.studybuddy.core.metadata.updates.source.store.UpdateMetadataSerializer
import com.edricchan.studybuddy.core.metadata.updates.source.store.UpdatesInfoDataStoreFileName
import com.edricchan.studybuddy.core.metadata.updates.source.store.updateMetadataMigration
import com.edricchan.studybuddy.core.settings.updates.proto.UpdateMetadata
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UpdateMetadataModule {
    @Singleton
    @Binds
    abstract fun bindUpdateMetadataDataSource(
        impl: LocalUpdateMetadataDataSource
    ): UpdateMetadataDataSource

    internal companion object {
        @Singleton
        @Provides
        fun provideUpdateMetadataDataStore(
            @ApplicationContext context: Context
        ): DataStore<@JvmSuppressWildcards UpdateMetadata> = DataStoreFactory.create(
            serializer = UpdateMetadataSerializer,
            corruptionHandler = ReplaceFileCorruptionHandler {
                Log.w(
                    "UpdateMetadata",
                    "provideUpdateMetadataDataStore: Update metadata is corrupted. " +
                        "Using default instance",
                    it
                )
                UpdateMetadata()
            },
            produceFile = { context.dataStoreFile(UpdatesInfoDataStoreFileName) },
            migrations = listOf(context.updateMetadataMigration, SeedMetadataMigration(context))
        )
    }
}
