package com.example.stromtracker.ui.importexport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R

import java.io.File


class ImportExportFragment : Fragment(){

    private lateinit var importexportViewModel: ImportExportViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        importexportViewModel =
            ViewModelProviders.of(this).get(ImportExportViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_importexport, container, false)
        val textView: TextView = root.findViewById(R.id.text_importExport)
        val exportbut: Button =root.findViewById(R.id.importexport_exportbutton)
        exportbut.setOnClickListener{view->
            if(view !=null) {

            }

        }
        return root
    }


}
