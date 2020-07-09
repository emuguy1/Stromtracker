package com.example.stromtracker.ui

import android.app.Application
import android.content.ClipData.Item
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stromtracker.MainActivity
import com.example.stromtracker.database.DataRepository
import com.example.stromtracker.database.Haushalt


class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: DataRepository = DataRepository(application)

    private  val haushaltlist: LiveData<List<Haushalt>> = repo.getAllHaushalt()
    val selectedHaushalt = MutableLiveData<Haushalt>()

    fun getAllHaushalt(): LiveData<List<Haushalt>> {
        return haushaltlist
    }

        fun getHaushalt(): LiveData<Haushalt> {
            return selectedHaushalt
        }

        fun setHaushalt(haushalt: Haushalt) {
            selectedHaushalt.value = haushalt
    }
    }


