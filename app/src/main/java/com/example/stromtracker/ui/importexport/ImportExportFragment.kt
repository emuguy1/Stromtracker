package com.example.stromtracker.ui.importexport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R
import yogesh.firzen.filelister.FileListerDialog
import yogesh.firzen.filelister.OnFileSelectedListener
import java.io.File


class ImportExportFragment : Fragment(){

    private lateinit var importexportViewModel: ImportExportViewModel
    private lateinit var pathselected:String
    private lateinit var fileselected: File

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        importexportViewModel =
            ViewModelProviders.of(this).get(ImportExportViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_importexport, container, false)
        val textView: TextView = root.findViewById(R.id.text_importExport)
        val fileListerDialog: FileListerDialog= FileListerDialog.createFileListerDialog(this.requireContext())

        fileListerDialog.setOnFileSelectedListener(OnFileSelectedListener { file, path ->
            pathselected=path
            fileselected=file
        })
        fileListerDialog.setDefaultDir("/storage/emulated/0/Download")
        fileListerDialog.setFileFilter(FileListerDialog.FILE_FILTER.ALL_FILES)
        fileListerDialog.show()
        return root
    }


}