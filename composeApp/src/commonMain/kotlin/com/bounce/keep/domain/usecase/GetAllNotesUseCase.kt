package com.bounce.keep.domain.usecase

import com.bounce.keep.data.entity.NotepadEntity
import com.bounce.keep.domain.repository.NotepadRepository
import kotlinx.coroutines.flow.Flow

class GetAllNotesUseCase(private val repository: NotepadRepository) {
    operator fun invoke(): Flow<List<NotepadEntity>> = repository.getAllNotes()
}
