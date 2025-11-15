package com.edricchan.studybuddy.features.tasks.data.di

import com.edricchan.studybuddy.data.source.firestore.IDefaultFirestoreDataSource
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.data.model.TodoProject
import com.edricchan.studybuddy.features.tasks.data.repo.TaskProjectDataSource
import com.edricchan.studybuddy.features.tasks.data.source.TaskDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class TaskDataModule {
    @Binds
    abstract fun bindTaskDataSource(
        source: TaskDataSource
    ): IDefaultFirestoreDataSource<TodoItem>

    @Binds
    abstract fun bindTaskProjectDataSource(
        source: TaskProjectDataSource
    ): IDefaultFirestoreDataSource<TodoProject>
}
