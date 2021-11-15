package com.example.emoji.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

const val API_KEY = "ps3FBF5c7Zr11BXXgHAkTImypWqv2y6H"
const val AUTH_EMAIL = "olesya_leader@mail.ru"

class HeaderInterceptor(email: String, apiKey: String) : Interceptor {
    private val credential: String = Credentials.basic(email, apiKey)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request: Request = chain.request()
        val authenticatedRequest: Request = request.newBuilder()
            .header("Authorization", credential)
            .build()
        return chain.proceed(authenticatedRequest)
    }
}

@ExperimentalSerializationApi
object Instance {
    private const val BASE_URL = "https://tinkoff-android-fall21.zulipchat.com"
    private const val DOMAIN = "$BASE_URL/api/v1/"
    private var builder: Retrofit.Builder? = null

    private val json = Json { ignoreUnknownKeys = true }

    fun getInstance(): Retrofit {
        if (builder == null) {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val okHttpBuiler: OkHttpClient.Builder = OkHttpClient.Builder()
            okHttpBuiler
                .addInterceptor(HeaderInterceptor(AUTH_EMAIL, API_KEY))
                .connectTimeout(10000, TimeUnit.SECONDS)
                .writeTimeout(10000, TimeUnit.SECONDS)
                .readTimeout(10000, TimeUnit.SECONDS)
                .addInterceptor(logger)
                .build()

            builder = Retrofit.Builder()
                .baseUrl(DOMAIN)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpBuiler.build())
        }
        return builder!!.build()
    }
}