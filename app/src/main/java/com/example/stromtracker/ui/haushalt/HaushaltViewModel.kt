package com.example.stromtracker.ui.haushalt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HaushaltViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Haushaltlol Fragment"
    }
    val text: LiveData<String> = _text
}