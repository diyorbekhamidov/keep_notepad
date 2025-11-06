package com.bounce.keep.di

import com.bounce.keep.data.getDatabaseBuilder
import org.koin.dsl.module

actual fun platformModule() = module {
    single { getDatabaseBuilder(get()) }
}