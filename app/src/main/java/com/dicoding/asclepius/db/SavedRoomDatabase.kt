package com.dicoding.asclepius.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SavedAnalyze::class], version = 1)
abstract class SavedRoomDatabase: RoomDatabase() {
    abstract fun savedAnalyzeDao(): SavedAnalyzeDao

    companion object {
        @Volatile
        private var INSTANCE: SavedRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): SavedRoomDatabase {
            if (INSTANCE == null) {
                synchronized(SavedRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        SavedRoomDatabase::class.java, "saved_database")
                        .build()
                }
            }
            return INSTANCE as SavedRoomDatabase
        }
    }

}