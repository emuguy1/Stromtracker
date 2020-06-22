package com.example.stromtracker.ui.haushalteBearbeiten_Loeschen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HaushaltBearbeitenLoeschenViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Haushalt_bearbeitenLöschen Fragment"
    }
    val text: LiveData<String> = _text
}