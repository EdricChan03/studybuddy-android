package com.edricchan.studybuddy.core.di

import com.edricchan.studybuddy.exts.firebase.auth.currentUserFlow
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.messaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Provides
    fun provideFirebaseUserFlow(): Flow<@JvmSuppressWildcards FirebaseUser?> =
        provideFirebaseAuth().currentUserFlow

    @Provides
    fun provideFirebaseFirestore() = Firebase.firestore

    @Provides
    fun provideFirebaseMessaging() = Firebase.messaging
}
