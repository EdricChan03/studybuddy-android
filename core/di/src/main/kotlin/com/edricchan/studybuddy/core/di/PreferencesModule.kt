package com.edricchan.studybuddy.core.di

import android.content.Context
import com.edricchan.studybuddy.core.di.qualifiers.prefs.DefaultSharedPreferences
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {
    @DefaultSharedPreferences
    @Singleton
    @Provides
    fun provideDefaultSharedPrefs(@ApplicationContext context: Context) =
        context.defaultSharedPreferences
}
