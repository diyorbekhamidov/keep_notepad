package com.bounce.keep.domain.usecase

import com.bounce.keep.data.entity.NotepadEntity
import com.bounce.keep.domain.repository.NotepadRepository

class UpsertNoteUseCase(private val repository: NotepadRepository) {
    suspend operator fun invoke(note: NotepadEntity) {
        if (note.id == 0) {
            repository.insertNote(note)
        } else {
            repository.updateNote(note)
        }
    }
}
