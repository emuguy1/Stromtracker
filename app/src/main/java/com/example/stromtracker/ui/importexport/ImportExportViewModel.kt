package com.example.stromtracker.ui.importexport

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImportExportViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Import Export Fragment"
    }
    val text: LiveData<String> = _text
}