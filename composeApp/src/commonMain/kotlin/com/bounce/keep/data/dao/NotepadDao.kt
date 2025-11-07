package com.bounce.keep.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.bounce.keep.data.entity.NotepadEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface NotepadDao {

    @Insert
    suspend fun insertNote(notepadEntity: NotepadEntity)

    @Query("SELECT * FROM notepad_table")
    fun getAllNotes(): Flow<List<NotepadEntity>>

    @Update
    suspend fun updateNote(notepadEntity: NotepadEntity)

    @Query("DELETE FROM notepad_table WHERE id = :id")
    suspend fun deleteNote(id: Int)

    @Query("SELECT * FROM notepad_table WHERE id = :id")
    suspend fun getNoteById(id: Int): NotepadEntity?

    @Query("SELECT * FROM notepad_table WHERE title LIKE '%' || :str || '%' OR notes LIKE '%' || :str || '%'")
    fun getNoteByStr(str: String): Flow<List<NotepadEntity>>

}