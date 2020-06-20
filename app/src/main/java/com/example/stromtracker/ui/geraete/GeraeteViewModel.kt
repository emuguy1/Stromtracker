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
    var list:LiveData<List<Geraete>> = repo.getAllGeraete()



    private val _text = MutableLiveData<String>().apply {
        value = "This is geraete Fragment"
    }
    val text: LiveData<String> = _text


    fun getAllGeraete():LiveData<List<Geraete>> {
        return list
    }

        /*

    fun setGereateList(geraet:Geraete) {
        test.geraeteDao().insertAll(geraet)

    }

    fun getGereateList():List<Geraete> {
        return test.geraeteDao().getAll()

    }








     */


}