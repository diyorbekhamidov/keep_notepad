package com.bounce.keep.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notepad_table")
data class NotepadEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val notes: String,
    val date: String,
    val color: Long
)
