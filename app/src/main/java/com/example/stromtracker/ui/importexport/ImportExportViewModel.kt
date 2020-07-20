package com.example.stromtracker.ui.importexport

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

import com.example.stromtracker.database.*

class ImportExportViewModel(application: Application) : AndroidViewModel(application) {
    var repo: DataRepository = DataRepository(application)
    var geraetelist:LiveData<List<Geraete>> = repo.getAllGeraete()
    var haushaltlist:LiveData<List<Haushalt>> = repo.getAllHaushalt()
    var kategorieList:LiveData<List<Kategorie>> = repo.getAllKategorie()
    var raumList:LiveData<List<Raum>> = repo.getAllRaeume()


    fun getAllGeraete():LiveData<List<Geraete>> {
        return geraetelist
    }

    fun getAllKategorie():LiveData<List<Kategorie>> {
        return kategorieList
    }

    fun insertKategorie(k: Kategorie) {
        repo.insertKategorie(k)
    }

    fun getAllRaeume():LiveData<List<Raum>> {
        return raumList
    }

    fun insertRaum(r: Raum) {
        repo.insertRaum(r)
    }

    fun insertHaushalt(h: Haushalt) {
        repo.insertHaushalt(h)
    }

    fun getAllHaushalt():LiveData<List<Haushalt>> {
        return haushaltlist
    }

    fun insertGeraet(g: Geraete) {
        repo.insertGeraete(g)
    }

}
