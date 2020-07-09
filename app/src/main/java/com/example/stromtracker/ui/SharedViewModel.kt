package com.example.stromtracker.ui

import android.app.Application
import android.content.ClipData.Item
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stromtracker.database.DataRepository
import com.example.stromtracker.database.Haushalt


class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private var selectedHaushalt = MutableLiveData<Haushalt>()
    var repo: DataRepository = DataRepository(application)
    var haushaltlist: LiveData<List<Haushalt>> = repo.getAllHaushalt()



    fun getHaushalt():MutableLiveData<Haushalt> {
        Log.d("TAGShared", "returning selected Haushalt")
        return selectedHaushalt
    }

    fun setHaushalt(haushalt:Haushalt) {

        selectedHaushalt.value = haushalt


    }

    fun getAllHaushalt():LiveData<List<Haushalt>> {
        return haushaltlist
    }


}