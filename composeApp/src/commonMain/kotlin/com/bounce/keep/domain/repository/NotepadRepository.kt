package com.bounce.keep.domain.repository

import com.bounce.keep.data.entity.NotepadEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface NotepadRepository {

    suspend fun insertNote(notepadEntity: NotepadEntity)

    fun getAllNotes(): Flow<List<NotepadEntity>>

    suspend fun updateNote(notepadEntity: NotepadEntity)

    suspend fun deleteNote(id: Int)

    suspend fun getNoteById(id: Int): NotepadEntity?

}