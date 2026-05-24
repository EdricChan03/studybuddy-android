package com.edricchan.studybuddy.core.settings.tasks.di

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.edricchan.studybuddy.core.settings.tasks.proto.TasksSettings
import com.edricchan.studybuddy.core.settings.tasks.source.store.TaskDataStoreFileName
import com.edricchan.studybuddy.core.settings.tasks.source.store.TasksSettingsSerializer
import com.edricchan.studybuddy.core.settings.tasks.source.store.tasksPrefsMigration
import com.edricchan.studybuddy.core.settings.tasks.source.store.tasksPrefsOldMigration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class TasksSettingsModule {
    internal companion object {
        @Singleton
        @Provides
        fun provideTasksDataStore(
            @ApplicationContext context: Context
        ): DataStore<@JvmSuppressWildcards TasksSettings> = DataStoreFactory.create(
            serializer = TasksSettingsSerializer,
            corruptionHandler = ReplaceFileCorruptionHandler {
                Log.w(
                    "TasksSettings",
                    "provideTasksDataStore: Tasks settings data is corrupted. " +
                        "Using default instance",
                    it
                )
                TasksSettings()
            },
            produceFile = { context.dataStoreFile(TaskDataStoreFileName) },
            migrations = listOf(context.tasksPrefsOldMigration, context.tasksPrefsMigration)
        )
    }
}
