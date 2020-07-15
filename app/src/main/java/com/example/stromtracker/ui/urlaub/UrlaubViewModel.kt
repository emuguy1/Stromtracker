package com.example.stromtracker.ui.urlaub

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UrlaubViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Urlaub Fragment"
    }
    val text: LiveData<String> = _text
}