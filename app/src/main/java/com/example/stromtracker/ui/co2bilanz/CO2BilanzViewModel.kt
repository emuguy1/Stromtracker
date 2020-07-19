package com.example.stromtracker.ui.co2bilanz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CO2BilanzViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is CO2Bilanz Fragment"
    }
    val text: LiveData<String> = _text
}
