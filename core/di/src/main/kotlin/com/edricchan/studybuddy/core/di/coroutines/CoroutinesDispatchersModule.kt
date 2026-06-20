package com.edricchan.studybuddy.core.di.coroutines

import com.edricchan.studybuddy.core.di.coroutines.qualifier.DefaultDispatcher
import com.edricchan.studybuddy.core.di.coroutines.qualifier.IoDispatcher
import com.edricchan.studybuddy.core.di.coroutines.qualifier.MainDispatcher
import com.edricchan.studybuddy.core.di.coroutines.qualifier.MainImmediateDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@InstallIn(SingletonComponent::class)
@Module
object CoroutinesDispatchersModule {
    @DefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @MainImmediateDispatcher
    @Provides
    fun providesMainImmediateDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate
}
