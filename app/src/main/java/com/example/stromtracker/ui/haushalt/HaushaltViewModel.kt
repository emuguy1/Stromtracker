package com.example.stromtracker.ui.haushalt

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stromtracker.database.DataRepository
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.database.Kategorie

class HaushaltViewModel(application:Application) : AndroidViewModel(application) {
    var repo: DataRepository = DataRepository(application)
    var haushaltList: LiveData<List<Haushalt>> = repo.getAllHaushalt()

    fun getAllHaushalt():LiveData<List<Haushalt>> {
        return haushaltList
    }
    fun insertHaushalt(a: Haushalt) {
        repo.insertHaushalt(a)
    }
    fun updateHaushalt(a: Haushalt) {
        repo.updateHaushalt(a)
    }
    fun deleteHaushalt(a: Haushalt) {
        repo.deleteHaushalt(a)
    }
}