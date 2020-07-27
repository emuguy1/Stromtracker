package com.example.stromtracker.ui.co2bilanz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.stromtracker.database.*


class CO2BilanzViewModel(application: Application) : AndroidViewModel(application) {

    var repo: DataRepository = DataRepository(application)


    fun getAllVerbraucherByHaushaltID(id: Int): LiveData<List<Geraete>> {
        return repo.getAllVerbraucherByHaushaltID(id)
    }

    fun getAllProduzentenByHaushaltID(id: Int): LiveData<List<Geraete>> {
        return repo.getAllProduzentenByHaushaltID(id)
    }

    fun getAllRaumByHaushaltID(id: Int): LiveData<List<Raum>> {
        return repo.getAllRaumByHaushaltID(id)
    }

    fun getAllUrlaubByHaushaltID(id: Int): LiveData<List<Urlaub>> {
        return repo.getAllUrlaubByHaushaltID(id)
    }

    fun insertGeraet(g: Geraete) {
        repo.insertGeraete(g)
    }

    fun deleteGeraet(g: Geraete) {
        repo.deleteGeraete(g)
    }

    fun updateGeraet(g: Geraete) {
        repo.updateGeraete(g)
    }

}
