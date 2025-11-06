package com.bounce.keep

import android.app.Application
import com.bounce.keep.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent

class MyApp : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()
    }
    init {
        initKoin {
            androidLogger()
            androidContext(this@MyApp)
        }
    }
}