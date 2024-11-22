package com.dicoding.asclepius.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.db.SavedAnalyze
import com.dicoding.asclepius.repository.SavedRepository
import kotlinx.coroutines.launch

class SavedViewModel(private val savedRepository: SavedRepository): ViewModel() {


    fun getSavedData() = savedRepository.getAllData()

    fun saveData(data: SavedAnalyze) {
        viewModelScope.launch {
            savedRepository.insert(data)
            savedRepository.setSavedData(data)
        }
    }
}