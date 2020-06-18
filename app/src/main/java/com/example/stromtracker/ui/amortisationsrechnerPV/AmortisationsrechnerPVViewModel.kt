package com.example.stromtracker.ui.amortisationsrechnerPV

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AmortisationsrechnerPVViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is AmortisationsPV Fragment"
    }
    val text: LiveData<String> = _text
}