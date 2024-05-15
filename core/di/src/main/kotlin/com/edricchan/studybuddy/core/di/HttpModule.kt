package com.edricchan.studybuddy.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ContentNegotiationJsonHttpClient

val Context.defaultCacheStorage get() = FileStorage(cacheDir.resolve("ktor"))

fun HttpClientConfig<*>.installBaseConfig(context: Context) {
    install(HttpCache) {
        privateStorage(context.defaultCacheStorage)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object HttpModule {
    @ContentNegotiationJsonHttpClient
    @Provides
    fun provideContentNegotiationJsonHttpClient(
        @ApplicationContext context: Context
    ) = HttpClient(OkHttp) {
        installBaseConfig(context)
        install(ContentNegotiation) {
            json()
        }
    }
}
