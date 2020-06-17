package com.example.stromtracker.ui.geraete

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GeraeteViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is geraete Fragment"
    }
    val text: LiveData<String> = _text
}