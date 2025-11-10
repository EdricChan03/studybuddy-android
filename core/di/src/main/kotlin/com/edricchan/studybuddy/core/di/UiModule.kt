package com.edricchan.studybuddy.core.di

import com.edricchan.studybuddy.ui.common.snackbar.SnackBarController
import com.edricchan.studybuddy.ui.common.snackbar.SnackBarHost
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UiModule {
    @Binds
    @Singleton
    abstract fun bindSnackBarController(host: SnackBarHost): SnackBarController

    companion object {
        @Provides
        @Singleton
        fun provideSnackBarHost(): SnackBarHost = SnackBarHost()
    }
}
