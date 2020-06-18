package com.example.stromtracker.ui.raeume

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RaeumeViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Räume Fragment"
    }
    val text: LiveData<String> = _text
}