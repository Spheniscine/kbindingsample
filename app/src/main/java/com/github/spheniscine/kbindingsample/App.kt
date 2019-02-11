package com.github.spheniscine.kbindingsample

import android.app.Application
import android.content.Context
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(koinModule)
        }
    }
}

val App.koinModule: Module get() {
    val app = this
    return module {
        single { app }
        single<Application> { app }
        single<Context> { app }

        viewModel { TwoWayBindingExampleViewModel() }
    }
}