package com.dicoding.asclepius.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.db.SavedAnalyze
import com.dicoding.asclepius.db.SavedAnalyzeDao
import com.dicoding.asclepius.db.SavedRoomDatabase
import com.dicoding.asclepius.utils.AppExecutors

class SavedRepository private constructor(application: Application, private val appExecutors: AppExecutors){
    private val savedAnalyzeDao: SavedAnalyzeDao

    companion object {
        @Volatile
        private var INSTANCE: SavedRepository? = null

        fun getInstance(application: Application, appExecutors: AppExecutors): SavedRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = SavedRepository(application, appExecutors)
                INSTANCE = instance
                instance
            }
        }
    }

    init {
        val db = SavedRoomDatabase.getDatabase(application)
        savedAnalyzeDao = db.savedAnalyzeDao()
    }

    fun getAllData(): LiveData<List<SavedAnalyze>> = savedAnalyzeDao.getAllItems()

    fun setSavedData(data: SavedAnalyze) {
        appExecutors.diskIO.execute {
            savedAnalyzeDao.update(data)
        }
    }

    fun insert(data: SavedAnalyze) {
        appExecutors.diskIO.execute { savedAnalyzeDao.insert(data)}
    }

}