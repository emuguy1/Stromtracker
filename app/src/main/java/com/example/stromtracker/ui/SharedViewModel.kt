package com.example.stromtracker.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stromtracker.database.*

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    val repo: DataRepository = DataRepository(application)
    val selectedHaushalt = MutableLiveData<Haushalt>()
    var geraetelist: LiveData<List<Geraete>> = repo.getAllGeraete()
    var verbraucherList: LiveData<List<Geraete>> = repo.getAllVerbraucher()
    var produzentenList: LiveData<List<Geraete>> = repo.getAllProduzenten()
    var haushaltlist: LiveData<List<Haushalt>> = repo.getAllHaushalt()
    var kategorieList: LiveData<List<Kategorie>> = repo.getAllKategorie()
    var raumList: LiveData<List<Raum>> = repo.getAllRaeume()
    var urlaubList: LiveData<List<Urlaub>> = repo.getAllUrlaub()

    fun getAllUrlaub(): LiveData<List<Urlaub>>{
        return urlaubList
    }

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

    fun getAllGeraete(): LiveData<List<Geraete>> {
        return geraetelist
    }

    fun getAllVerbraucher(): LiveData<List<Geraete>> {
        return verbraucherList
    }

    fun getAllProduzenten(): LiveData<List<Geraete>> {
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

    fun getHaushaltByID(id: Int): LiveData<Haushalt> {
        return repo.getHaushaltByID(id)
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


    fun getHaushalt(): LiveData<Haushalt> {
        return selectedHaushalt
    }

    fun setHaushalt(haushalt: Haushalt) {
        selectedHaushalt.value = haushalt
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

    fun updateGeraeteByRaumId(alterRaum: Int, neuerRaum: Int) {
        repo.updateGeraetByRaumID(alterRaum, neuerRaum)
    }

    fun insertUrlaub(u: Urlaub) {
        repo.insertUrlaub(u)
    }

    fun updateUrlaub(u: Urlaub) {
        repo.updateUrlaub(u)
    }

    fun deleteUrlaub(u: Urlaub) {
        repo.deleteUrlaub(u)
    }
    fun updateKategorie(k: Kategorie) {
        repo.updateKategorie(k)
    }

    fun updateGeraeteByKategorieID(alteKategorie: Int, neueKategorie: Int) {
        repo.updateGeraetByKategorieID(alteKategorie, neueKategorie)
    }

    fun deleteKategorie(k: Kategorie) {
        repo.deleteKategorie(k)
    }

    fun updateHaushalt(a: Haushalt) {
        repo.updateHaushalt(a)
    }

    fun deleteHaushalt(a: Haushalt) {
        repo.deleteHaushalt(a)
    }
}
