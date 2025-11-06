package com.bounce.keep.data.repository

import com.bounce.keep.data.dao.NotepadDao
import com.bounce.keep.data.entity.NotepadEntity
import com.bounce.keep.domain.repository.NotepadRepository
import kotlinx.coroutines.flow.Flow

class NotepadRepositoryImpl(private val notepadDao: NotepadDao) : NotepadRepository {
    override suspend fun insertNote(notepadEntity: NotepadEntity) {
        notepadDao.insertNote(notepadEntity)
    }

    override fun getAllNotes(): Flow<List<NotepadEntity>> {
        return notepadDao.getAllNotes()
    }

    override suspend fun updateNote(notepadEntity: NotepadEntity) {
        notepadDao.updateNote(notepadEntity)
    }

    override suspend fun deleteNote(id: Int) {
        notepadDao.deleteNote(id)
    }

    override suspend fun getNoteById(id: Int): NotepadEntity? {
        return notepadDao.getNoteById(id)
    }
}