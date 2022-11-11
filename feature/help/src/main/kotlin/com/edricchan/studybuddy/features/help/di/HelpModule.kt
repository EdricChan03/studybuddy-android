package com.edricchan.studybuddy.features.help.di

import com.edricchan.studybuddy.features.help.data.api.GitHubHelpApiImpl
import com.edricchan.studybuddy.features.help.data.api.HelpApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class HelpModule {
    @Binds
    abstract fun bindHelpApi(
        helpApiImpl: GitHubHelpApiImpl
    ): HelpApi
}
