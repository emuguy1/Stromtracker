package com.example.stromtracker.ui.amortisationsrechner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AmortisationsrechnerViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Amortisations Fragment"
    }
    val text: LiveData<String> = _text
}