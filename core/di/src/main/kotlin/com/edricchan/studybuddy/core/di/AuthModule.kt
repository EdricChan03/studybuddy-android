package com.edricchan.studybuddy.core.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.edricchan.studybuddy.core.auth.service.AuthService
import com.edricchan.studybuddy.core.auth.service.firebase.FirebaseAuthServiceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    abstract fun bindAuthService(firebase: FirebaseAuthServiceImpl): AuthService

    companion object {
        @Provides
        fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager =
            CredentialManager.create(context)
    }
}
