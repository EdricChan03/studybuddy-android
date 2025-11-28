package com.edricchan.studybuddy.features.tasks.data.di

import com.edricchan.studybuddy.data.source.firestore.IDefaultFirestoreDataSource
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.data.model.TodoProject
import com.edricchan.studybuddy.features.tasks.data.repo.TaskProjectDataSource
import com.edricchan.studybuddy.features.tasks.data.repo.TaskRepository
import com.edricchan.studybuddy.features.tasks.data.source.TaskDataSource
import com.edricchan.studybuddy.features.tasks.domain.repo.ITaskRepository
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
        source: TaskDataSource
    ): IDefaultFirestoreDataSource<TodoItem>

    @Binds
    abstract fun bindTaskProjectDataSource(
        source: TaskProjectDataSource
    ): IDefaultFirestoreDataSource<TodoProject>

    @Binds
    abstract fun bindTaskRepository(
        repo: TaskRepository
    ): ITaskRepository
}
