package com.example.stromtracker.ui.raeume.raeumeErstellen
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RaeumeErstellenViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Räume_Erstellen Fragment"
    }
    val text: LiveData<String> = _text
}