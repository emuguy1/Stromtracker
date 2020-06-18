package com.example.stromtracker.ui.verbrauchsrechner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VerbrauchsrechnerViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Verbrauchsrechner Fragment"
    }
    val text: LiveData<String> = _text
}