package com.bounce.keep.domain.usecase

import com.bounce.keep.data.entity.NotepadEntity
import com.bounce.keep.domain.repository.NotepadRepository

class NotepadUseCase(private val notepadRepository: NotepadRepository) {

    suspend fun insertNote(notepadEntity: NotepadEntity) =
        notepadRepository.insertNote(notepadEntity)

    fun getAllNotes() = notepadRepository.getAllNotes()

    suspend fun updateNote(notepadEntity: NotepadEntity) =
        notepadRepository.updateNote(notepadEntity)

    suspend fun deleteNote(id: Int) = notepadRepository.deleteNote(id)

    suspend fun getNoteById(id: Int) = notepadRepository.getNoteById(id)


}