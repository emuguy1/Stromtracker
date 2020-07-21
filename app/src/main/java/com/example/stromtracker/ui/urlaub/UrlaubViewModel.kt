package com.example.stromtracker.ui.urlaub

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.stromtracker.database.*

class UrlaubViewModel(application: Application) : AndroidViewModel(application) {
    var repo: DataRepository = DataRepository(application)
    var urlaubList: LiveData<List<Urlaub>> = repo.getAllUrlaub()

    fun getAllUrlaub(): LiveData<List<Urlaub>> {
        return urlaubList
    }

    fun getAllUrlaubByHaushaltID(id: Int): LiveData<List<Urlaub>> {
        return repo.getAllUrlaubByHaushaltID(id)
    }

    fun getAllVerbraucherByHaushaltID(id: Int): LiveData<List<Geraete>> {
        return repo.getAllVerbraucherByHaushaltID(id)
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

}
