package com.edricchan.studybuddy.core.di

import com.edricchan.studybuddy.core.auth.service.AuthService
import com.edricchan.studybuddy.core.auth.service.firebase.FirebaseAuthServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    abstract fun bindAuthService(firebase: FirebaseAuthServiceImpl): AuthService
}
