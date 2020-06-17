package com.example.stromtracker.ui.kategorien


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class KategorienViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Kategorien Fragment"
    }
    val text: LiveData<String> = _text
}