package com.example.emoji

import android.app.Application
import com.example.emoji.di.AppComponent
import com.example.emoji.di.DaggerAppComponent
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class App : Application() {

   lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = buildComponent()
    }

    fun buildComponent(): AppComponent {
        return DaggerAppComponent.builder()
            .build()
    }
}