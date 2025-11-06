package com.bounce.keep.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bounce.keep.data.database.AppDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("notes.db")

    return Room.databaseBuilder(
        context = appContext,
        name = dbFile.absolutePath
    )
}
