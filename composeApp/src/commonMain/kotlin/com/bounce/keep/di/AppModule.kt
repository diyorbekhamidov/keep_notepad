package com.bounce.keep.di

import com.bounce.keep.data.dao.NotepadDao
import com.bounce.keep.data.database.getRoomDatabase
import com.bounce.keep.data.repository.NotepadRepositoryImpl
import com.bounce.keep.domain.repository.NotepadRepository
import com.bounce.keep.domain.usecase.NotepadUseCase
import com.bounce.keep.presentation.detail.DetailViewModel
import com.bounce.keep.presentation.editor.EditorViewModel
import com.bounce.keep.presentation.home.HomeViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect fun platformModule(): Module
val appModule = module {

    single { getRoomDatabase(get()) }
    single<NotepadDao> { getRoomDatabase(get()).notepadDao() }
    single<NotepadRepository> { NotepadRepositoryImpl(get()) }
    single<NotepadUseCase> { NotepadUseCase(get()) }
    viewModelOf(::EditorViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::DetailViewModel)
}