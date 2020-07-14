package com.example.stromtracker.ui.importexport

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R
import com.example.stromtracker.database.Haushalt
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter

import java.io.File
import java.io.FileWriter
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets.UTF_8
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.text.Charsets.UTF_8


class ImportExportFragment : Fragment(){

    private lateinit var importexportViewModel: ImportExportViewModel
    private lateinit var haushaltlist: ArrayList<Haushalt>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        importexportViewModel =
            ViewModelProviders.of(this).get(ImportExportViewModel::class.java)
        haushaltlist=ArrayList()
        importexportViewModel.getAllHaushalt().observe(
            viewLifecycleOwner,
            Observer { haushalte ->
                if (haushalte != null) {
                    haushaltlist.clear()
                    haushaltlist.addAll(haushalte)
                }
            }
        )


        val root = inflater.inflate(R.layout.fragment_importexport, container, false)
        val textView: TextView = root.findViewById(R.id.text_importExport)
        val exportbut: Button =root.findViewById(R.id.importexport_exportbutton)
        exportbut.setOnClickListener{view->
            if(view !=null) {
                val csvFile = generateFile(requireContext(),"HaushaltRoomExport.csv" )
                if (csvFile != null) {
                    //exportDatabaseToCSVFile(csvFile)
                    CsvWriter().open(csvFile,append=false){
                        writeRow(listOf("[HaushaltIdid]","[Name]","[Stromkosten]","[bewohnerAnzahl]","[zaehlerstand]","[datum]","[oekostrom]"))
                        haushaltlist.forEachIndexed{index,haushalt->
                            if(haushalt.getDatum()!=null) {
                                writeRow(
                                    listOf(
                                        haushalt.getHaushaltID(),
                                        haushalt.getName(),
                                        haushalt.getStromkosten(),
                                        haushalt.getBewohnerAnzahl(),
                                        haushalt.getZaehlerstand(),
                                        SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).format(
                                            haushalt.getDatum()
                                        ),
                                        haushalt.getOekostrom()
                                    )
                                )
                            }
                            else{
                                writeRow(
                                    listOf(
                                        haushalt.getHaushaltID(),
                                        haushalt.getName(),
                                        haushalt.getStromkosten(),
                                        haushalt.getBewohnerAnzahl(),
                                        null,
                                        null,
                                        haushalt.getOekostrom()
                                    )
                                )
                            }
                        }
                    }
                    Toast.makeText(this.context, "CSV File wurde Erstellet"+csvFile.readText(), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this.context, "CSV File konnte nicht erstellt werden", Toast.LENGTH_LONG).show()
                }
            }

        }

        return root
    }
    fun generateFile(context: Context, fileName: String): File? {
        val csvFile = File(context.filesDir, fileName)
        csvFile.createNewFile()

        return if (csvFile.exists()) {
            csvFile
        } else {
            null
        }
    }

}
