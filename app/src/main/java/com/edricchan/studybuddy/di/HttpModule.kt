package com.edricchan.studybuddy.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ContentNegotiationJsonHttpClient

@Module
@InstallIn(SingletonComponent::class)
internal object HttpModule {
    @ContentNegotiationJsonHttpClient
    @Provides
    fun provideContentNegotiationJsonHttpClient() = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json()
        }
    }
}
