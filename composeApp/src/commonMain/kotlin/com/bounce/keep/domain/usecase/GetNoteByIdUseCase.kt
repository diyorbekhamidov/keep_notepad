package com.bounce.keep.domain.usecase

import com.bounce.keep.data.entity.NotepadEntity
import com.bounce.keep.domain.repository.NotepadRepository

class GetNoteByIdUseCase(private val repository: NotepadRepository) {
    suspend operator fun invoke(id: Int): NotepadEntity? = repository.getNoteById(id)
}
