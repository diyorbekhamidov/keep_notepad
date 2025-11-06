package com.bounce.keep.di

import com.bounce.keep.data.database.getDatabaseBuilder
import org.koin.dsl.module

actual fun platformModule() = module {
    single { getDatabaseBuilder() }
}