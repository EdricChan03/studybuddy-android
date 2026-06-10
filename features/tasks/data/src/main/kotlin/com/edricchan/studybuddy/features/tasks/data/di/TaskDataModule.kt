package com.edricchan.studybuddy.features.tasks.data.di

import com.edricchan.studybuddy.features.tasks.data.repo.TaskRepository
import com.edricchan.studybuddy.features.tasks.data.repo.project.FirebaseTaskProjectRepositoryImpl
import com.edricchan.studybuddy.features.tasks.data.repo.source.FirestoreTaskDataSource
import com.edricchan.studybuddy.features.tasks.data.repo.source.FirestoreTaskProjectDataSource
import com.edricchan.studybuddy.features.tasks.data.repo.source.TaskDataSource
import com.edricchan.studybuddy.features.tasks.data.repo.source.TaskProjectDataSource
import com.edricchan.studybuddy.features.tasks.domain.repo.ITaskRepository
import com.edricchan.studybuddy.features.tasks.domain.repo.project.TaskProjectRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
@JvmSuppressWildcards
abstract class TaskDataModule {
    @Binds
    abstract fun bindTaskDataSource(
        source: FirestoreTaskDataSource
    ): TaskDataSource

    @Binds
    abstract fun bindTaskProjectDataSource(
        source: FirestoreTaskProjectDataSource
    ): TaskProjectDataSource

    @Binds
    abstract fun bindTaskRepository(
        repo: TaskRepository
    ): ITaskRepository

    @Binds
    abstract fun bindTaskProjectRepository(
        repo: FirebaseTaskProjectRepositoryImpl
    ): TaskProjectRepository
}
