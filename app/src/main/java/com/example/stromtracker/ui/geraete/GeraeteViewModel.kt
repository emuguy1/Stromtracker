package com.example.stromtracker.ui.geraete

import android.app.Application
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stromtracker.database.*;


class GeraeteViewModel(application: Application) : AndroidViewModel(application) {

    var repo:DataRepository = DataRepository(application)
    var geraetelist:LiveData<List<Geraete>> = repo.getAllGeraete()
     var verbraucherList:LiveData<List<Geraete>> = repo.getAllVerbraucher()
     var produzentenList:LiveData<List<Geraete>> = repo.getAllProduzenten()

    var haushaltlist: LiveData<List<Haushalt>> = repo.getAllHaushalt()
    var kategorieList: LiveData<List<Kategorie>> = repo.getAllKategorie()
    var raumList: LiveData<List<Raum>> = repo.getAllRaeume()

    fun getAllGeraete(): LiveData<List<Geraete>> {
        return geraetelist
    }

    fun getAllRaumByHaushaltID(id: Int): LiveData<List<Raum>> {
        return repo.getAllRaumByHaushaltID(id)
    }

    fun getAllVerbraucher(): LiveData<List<Geraete>> {
        return verbraucherList
    }
     fun getAllProduzenten():LiveData<List<Geraete>> {
         return produzentenList
     }




    fun getAllKategorie(): LiveData<List<Kategorie>> {
        return kategorieList
    }

    fun insertKategorie(k: Kategorie) {
        repo.insertKategorie(k)
    }

    fun getAllRaeume(): LiveData<List<Raum>> {
        return raumList
    }

    fun insertRaum(r: Raum) {
        repo.insertRaum(r)
    }

    fun insertHaushalt(h: Haushalt) {
        repo.insertHaushalt(h)
    }

    fun getAllHaushalt(): LiveData<List<Haushalt>> {
        return haushaltlist
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
