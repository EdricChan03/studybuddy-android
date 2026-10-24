package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.dagger.di

import com.edricchan.studybuddy.core.settings.appearance.di.AppearanceSettingsModule
import com.edricchan.studybuddy.core.settings.appearance.repo.source.AppearanceSettingsDataStore
import com.edricchan.studybuddy.core.settings.appearance.repo.source.impl.InMemoryAppearanceSettingsDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppearanceSettingsModule::class]
)
object FakeDataStorageModule {
    @Singleton
    @Provides
    fun provideInMemoryAppearanceSettingsDataStore(): AppearanceSettingsDataStore =
        InMemoryAppearanceSettingsDataStore()
}
