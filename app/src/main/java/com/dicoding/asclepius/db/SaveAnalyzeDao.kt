package com.dicoding.asclepius.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface SavedAnalyzeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(savedAnalyze: SavedAnalyze)

    @Update
    fun update(savedAnalyze: SavedAnalyze)

    @Delete
    fun delete(savedAnalyze: SavedAnalyze)

    @Query("SELECT * FROM saved_analyze")
    fun getAllItems(): LiveData<List<SavedAnalyze>>
}