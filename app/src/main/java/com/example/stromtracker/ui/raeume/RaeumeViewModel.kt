package com.example.stromtracker.ui.raeume

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stromtracker.database.DataRepository

import com.example.stromtracker.database.Raum

class RaeumeViewModel (application: Application) : AndroidViewModel(application) {
    var repo: DataRepository = DataRepository(application)

    fun getAllRaeumeById(a:Int):LiveData<List<Raum>> {
        return repo.getAllRaumByHaushaltID(a)
    }
    fun insertRaeume(a: Raum) {
        repo.insertRaum(a)
    }
    fun updateRaeume(a: Raum) {
        repo.updateRaum(a)
    }
    fun deleteRaeume(a: Raum) {
        repo.deleteRaum(a)
    }

}