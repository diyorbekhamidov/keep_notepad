package com.bounce.keep.domain.usecase

import com.bounce.keep.domain.repository.NotepadRepository

class DeleteNoteUseCase(private val repository: NotepadRepository) {
    suspend operator fun invoke(id: Int) = repository.deleteNote(id)
}
