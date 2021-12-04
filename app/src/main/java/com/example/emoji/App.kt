package com.example.emoji

import android.app.Application
import com.example.emoji.di.AppComponent
import com.example.emoji.di.ApplicationModule
import com.example.emoji.di.DaggerAppComponent
import kotlinx.serialization.ExperimentalSerializationApi

/**
 * @author y.gladkikh
 */
@ExperimentalSerializationApi
class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = buildComponent()
    }

    private fun buildComponent(): AppComponent {
        return DaggerAppComponent.builder()
            .applicationModule(ApplicationModule(applicationContext))
            .build()
    }
}
