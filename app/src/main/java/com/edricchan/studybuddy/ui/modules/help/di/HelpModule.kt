package com.edricchan.studybuddy.ui.modules.help.di

import com.edricchan.studybuddy.ui.modules.help.data.api.GitHubHelpApiImpl
import com.edricchan.studybuddy.ui.modules.help.data.api.HelpApi
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
