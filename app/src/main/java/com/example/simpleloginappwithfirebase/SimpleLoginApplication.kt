package com.example.simpleloginappwithfirebase

import android.app.Application
import com.example.simpleloginappwithfirebase.presentation.common.di.commonModule
import com.example.simpleloginappwithfirebase.presentation.common.di.repositoriesModule
import com.example.simpleloginappwithfirebase.presentation.common.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SimpleLoginApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initialiseDependencyInjection()
    }

    private fun initialiseDependencyInjection() {
        startKoin {
            androidContext(this@SimpleLoginApplication)
            modules(
                viewModelsModule,
                commonModule,
                repositoriesModule
            )
        }
    }
}