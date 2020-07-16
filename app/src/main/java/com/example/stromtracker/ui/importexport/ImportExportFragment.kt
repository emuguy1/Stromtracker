package com.example.stromtracker.ui.importexport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R
import com.example.stromtracker.ui.importexport.ImportExportViewModel

class ImportExportFragment : Fragment() {

    private lateinit var importexportViewModel: ImportExportViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        importexportViewModel =
            ViewModelProvider(this).get(com.example.stromtracker.ui.importexport.ImportExportViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_importexport, container, false)
        val textView: TextView = root.findViewById(R.id.text_importExport)
        importexportViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
