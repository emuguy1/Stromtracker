package com.example.stromtracker.ui.geraete

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.stromtracker.database.*;


class GeraeteViewModel(application: Application) : AndroidViewModel(application) {


    var repo: DataRepository = DataRepository(application)
    var geraetelist: LiveData<List<Geraete>> = repo.getAllGeraete()
    var verbraucherList: LiveData<List<Geraete>> = repo.getAllVerbraucher()
     var produzentenList:LiveData<List<Geraete>> = repo.getAllProduzenten()


    var haushaltlist: LiveData<List<Haushalt>> = repo.getAllHaushalt()
    var kategorieList: LiveData<List<Kategorie>> = repo.getAllKategorie()
    var raumList: LiveData<List<Raum>> = repo.getAllRaeume()





    fun getAllVerbraucherByHaushaltID(id: Int): LiveData<List<Geraete>> {
        return repo.getAllVerbraucherByHaushaltID(id)
    }


    fun getAllRaumByHaushaltID(id: Int): LiveData<List<Raum>> {
        return repo.getAllRaumByHaushaltID(id)
    }

    fun getAllGeraete(): LiveData<List<Geraete>> {
        return geraetelist
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
