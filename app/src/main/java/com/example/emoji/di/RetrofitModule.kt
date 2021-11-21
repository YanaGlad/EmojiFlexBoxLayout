package com.example.emoji.di

import com.example.emoji.api.*
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class RetrofitModule {
    private val json = Json { ignoreUnknownKeys = true }

    @Provides
    @Reusable
    fun provideHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addNetworkInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(HeaderInterceptor(Companion.AUTH_EMAIL, API_KEY))
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .build()

    @Provides
    @Reusable
    @Named("EMOJI")
    fun getEmojiRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Reusable
    @Named("MESSENGER")
    fun getMessengerRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(DOMAIN)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun getMessengerApi(@Named("MESSENGER") retrofit: Retrofit): Api =
        retrofit.create(Api::class.java)

    companion object {
        const val AUTH_EMAIL = "olesya_leader@mail.ru"
        const val API_KEY = "ps3FBF5c7Zr11BXXgHAkTImypWqv2y6H"
        private const val BASE_URL = "https://tinkoff-android-fall21.zulipchat.com"
        private const val DOMAIN = "$BASE_URL/api/v1/"
        private const val CONNECT_TIMEOUT = 10L
        private const val WRITE_TIMEOUT = 30L
        private const val READ_TIMEOUT = 10L
    }
}
