package com.example.stromtracker.ui.kategorien


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.stromtracker.database.DataRepository
import com.example.stromtracker.database.Kategorie

class KategorienViewModel(application: Application) : AndroidViewModel(application) {
    var repo: DataRepository = DataRepository(application)
    var kategorieList: LiveData<List<Kategorie>> = repo.getAllKategorie()

    fun getAllKategorie(): LiveData<List<Kategorie>> {
        return kategorieList
    }

    fun insertKategorie(k: Kategorie) {
        repo.insertKategorie(k)
    }

    fun updateKategorie(k: Kategorie) {
        repo.updateKategorie(k)
    }

    fun updateGeraeteByKategorieID(alteKategorie: Int, neueKategorie : Int) {
        repo.updateGeraetByKategorieID(alteKategorie, neueKategorie)
    }

    fun deleteKategorie(k: Kategorie) {
        repo.deleteKategorie(k)
    }

}
