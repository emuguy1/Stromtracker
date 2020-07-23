package com.example.stromtracker.ui.haushalt

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.stromtracker.database.*

class HaushaltViewModel(application: Application) : AndroidViewModel(application) {
    private var repo: DataRepository = DataRepository(application)
    private var haushaltList: LiveData<List<Haushalt>> = repo.getAllHaushalt()

    fun getAllHaushalt(): LiveData<List<Haushalt>> {
        return haushaltList
    }

    fun getAllVerbraucherByHaushaltID(id: Int): LiveData<List<Geraete>> {
        return repo.getAllVerbraucherByHaushaltID(id)
    }

    fun getAllProduzentenByHaushaltID(id: Int): LiveData<List<Geraete>> {
        return repo.getAllProduzentenByHaushaltID(id)
    }

    fun getAllUrlaubByHaushaltID(id: Int): LiveData<List<Urlaub>> {
        return repo.getAllUrlaubByHaushaltID(id)
    }

    fun insertHaushalt(a: Haushalt) {
        return repo.insertHaushalt(a)
    }

    fun updateHaushalt(a: Haushalt) {
        repo.updateHaushalt(a)
    }

    fun deleteHaushalt(a: Haushalt) {
        repo.deleteHaushalt(a)
    }

    fun insertRaum(r: Raum) {
        repo.insertRaum(r)
    }
}
