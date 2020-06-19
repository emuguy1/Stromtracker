package com.example.stromtracker.ui.geraete

import android.app.Application
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stromtracker.database.*;


abstract class GeraeteViewModel: AndroidViewModel {
    //lateinit var test:AppDatabase


        constructor(application: Application) : super(application) {

            //test = AppDatabase.getInstance(application.applicationContext)!!

        }






    private val _text = MutableLiveData<String>().apply {
        value = "This is geraete Fragment"
    }
    val text: LiveData<String> = _text

    /*fun setGereateList(geraet:Geraete) {
        test.geraeteDao().insertAll(geraet)

    }

    fun getGereateList():List<Geraete> {
        return test.geraeteDao().getAll()

    }
    */








}