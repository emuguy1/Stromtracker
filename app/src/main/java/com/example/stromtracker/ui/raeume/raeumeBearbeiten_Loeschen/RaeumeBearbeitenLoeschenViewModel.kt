package com.example.stromtracker.ui.raeume.raeumeBearbeiten_Loeschen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RaeumeBearbeitenLoeschenViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Räume Bearbeiten Löschen Fragment"
    }
    val text: LiveData<String> = _text
}