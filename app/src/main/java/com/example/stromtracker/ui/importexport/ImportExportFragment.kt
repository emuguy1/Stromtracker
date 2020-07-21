package com.example.stromtracker.ui.importexport

import android.content.Context
import android.content.Intent
import android.media.session.MediaController
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.stromtracker.R
import com.example.stromtracker.database.Geraete
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.database.Kategorie
import com.example.stromtracker.database.Raum
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.FileNotFoundException
import java.io.FileOutputStream


class ImportExportFragment : Fragment() {

    private lateinit var importexportViewModel: ImportExportViewModel
    private lateinit var haushaltlist: ArrayList<Haushalt>
    private lateinit var raumlist: ArrayList<Raum>
    private lateinit var kategorielist: ArrayList<Kategorie>
    private lateinit var geraetlist: ArrayList<Geraete>
    private lateinit var csvfile : File
//    private lateinit var urlaublist: ArrayList<Urlaub>


    private val filepath = "MyFileStorage"
    internal var myExternalFile: File?=null
    private val isExternalStorageReadOnly: Boolean get() {
         val extStorageState = Environment.getExternalStorageState()
         return Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState
    }
    private val isExternalStorageAvailable: Boolean get() {
        val extStorageState = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == extStorageState
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        importexportViewModel =
            ViewModelProvider(this).get(ImportExportViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_importexport, container, false)

        val textView: TextView = root.findViewById(R.id.text_importExport)

        val exportbut: Button = root.findViewById(R.id.importexport_exportbutton)
        exportbut.setOnClickListener { view ->
            if (view != null) {
                writeCSVFile()
            }

        }

        val importbut:Button = root.findViewById(R.id.importexport_importbutton)
        importbut.setOnClickListener{view ->
            if(view!=null){

            }
        }

        createLists()
        return root
    }

//    private fun getDatafromcsv(csv:File){
//        csv.absolutePath
//        Toast.makeText(
//            this.context,
//            "CSV File wurde Erstellet" + csv.absolutePath,
//            Toast.LENGTH_SHORT
//        ).show()
//    }

    private fun writeCSVFile() {
        val csvFile = generateFile()
        if (csvFile != null) {
            //Als erstes werden die Haushalte ins File geschrieben
            csvWriter().open(csvFile, append = false) {
                writeRow(
                    listOf(
                        "[HaushaltIdid]",
                        "[Name]",
                        "[Stromkosten]",
                        "[bewohnerAnzahl]",
                        "[zaehlerstand]",
                        "[datum]",
                        "[oekostrom]"
                    )
                )
                haushaltlist.forEachIndexed { _, haushalt ->
                    if (haushalt.getDatum() != null) {
                        writeRow(
                            listOf(
                                haushalt.getHaushaltID(),
                                haushalt.getName(),
                                haushalt.getStromkosten(),
                                haushalt.getBewohnerAnzahl(),
                                haushalt.getZaehlerstand(),
                                SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).format(
                                    //Kann nicht null sein, da danach geprüft wird.
                                    haushalt.getDatum()
                                ),
                                haushalt.getOekostrom()
                            )
                        )
                    } else {
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
                //Jetzt die Raeume
                writeRow("-----------------------------------")
                writeRow(listOf("[Raumid]", "[Haushaltid]", "[Raumname]"))
                raumlist.forEachIndexed { _, raum ->
                    writeRow(
                        listOf(
                            raum.getRaumID(),
                            raum.getHaushaltID(),
                            raum.getName()
                        )
                    )
                }
                //Als naechstes die Kategorien
                writeRow("-----------------------------------")
                writeRow(listOf("[KategorienId]", "[Kategorienname]", "[Iconindex]"))
                kategorielist.forEachIndexed { _, kategorie ->
                    writeRow(
                        listOf(
                            kategorie.getKategorieID(),
                            kategorie.getName(),
                            kategorie.getIcon()
                        )
                    )
                }
                //Jetzt die Geraete
                writeRow("-----------------------------------")
                writeRow(
                    listOf(
                        "[GeraeteID]",
                        "[Geraetename]",
                        "[KategorieID]",
                        "[RaumID]",
                        "[HaushaltID]",
                        "[StromverbrauchVollast]",
                        "[StromverbrauchStandBy]",
                        "[BetriebszeitVollast]",
                        "[BetriebszeitStandBy]",
                        "[Urlaubsmodus]",
                        "[Jahresverbrauch]",
                        "[Eigenverbrauch]",
                        "[Notiz]"
                    )
                )
                geraetlist.forEachIndexed { _, geraet ->
                    writeRow(
                        listOf(
                            geraet.getGeraeteID(),
                            geraet.getName(),
                            geraet.getKategorieID(),
                            geraet.getRaumID(),
                            geraet.getHaushaltID(),
                            geraet.getStromVollast(),
                            geraet.getStromStandBy(),
                            geraet.getBetriebszeit(),
                            geraet.getBetriebszeitStandBy(),
                            geraet.getUrlaubsmodus(),
                            geraet.getJahresverbrauch(),
                            geraet.getEigenverbrauch(),
                            geraet.getNotiz()
                        )
                    )


                }
                //TODO: wenn Urlaub gemerged ist
                //Als letztes die Urlaube
//                        writeRow("-----------------------------------")
//                        writeRow(listOf("[Urlaubid]", "[HaushaltId]", "[DatumVon]", "[DatumBis]", "[name]", "[gesamtVerbrauch]"))
//                        urlaublist.forEachIndexed { _, urlaub ->
//                            writeRow(
//                                listOf(
//                                    urlaub.getUrlaubID(),
//                                    urlaub.getHaushaltID(),
//                                    urlaub.getDatevon(),
//                                    urlaub.getDatebis(),
//                                    urlaub.getName(),
//                                    urlaub.getGesamtverbrauchh()
//                                )
//                            )
//                        }


            }
            Toast.makeText(
                this.context,
                "CSV File wurde Erstellet" + csvFile.readText(),
                Toast.LENGTH_SHORT
            ).show()
            val intent = goToFileIntent(requireContext(), csvFile)
            startActivity(intent)

        } else {
            Toast.makeText(
                this.context,
                "CSV File konnte nicht erstellt werden",
                Toast.LENGTH_LONG
            ).show()

        }
    }

    private fun createLists(){
        haushaltlist = ArrayList()
        raumlist = ArrayList()
        kategorielist = ArrayList()
        geraetlist = ArrayList()
//        urlaublist = ArrayList()

        importexportViewModel.getAllHaushalt().observe(
            viewLifecycleOwner,
            Observer { haushalte ->
                if (haushalte != null) {
                    haushaltlist.clear()
                    haushaltlist.addAll(haushalte)
                }
            }
        )
        importexportViewModel.getAllRaeume().observe(
            viewLifecycleOwner,
            Observer { raeume ->
                if (raeume != null) {
                    raumlist.clear()
                    raumlist.addAll(raeume)
                }
            }
        )
        importexportViewModel.getAllKategorie().observe(
            viewLifecycleOwner,
            Observer { kategorien ->
                if (kategorien != null) {
                    kategorielist.clear()
                    kategorielist.addAll(kategorien)
                }
            }
        )
        importexportViewModel.getAllGeraete().observe(
            viewLifecycleOwner,
            Observer { geraete ->
                if (geraete != null) {
                    geraetlist.clear()
                    geraetlist.addAll(geraete)
                }
            }
        )
//        importexportViewModel.getAllUrlaube().observe(
//            viewLifecycleOwner,
//            Observer { urlaube ->
//                if (urlaube != null) {
//                    urlaublist.clear()
//                    urlaublist.addAll(urlaube)
//                }
//            }
//        )
    }

    private fun generateFile(): File? {
        val csvFile=File(requireContext().filesDir,"StromtrackerRoomExport.csv")
        csvFile.createNewFile()

        return if (csvFile.exists()) {
            csvFile
        } else {
            null
        }
    }

    private fun goToFileIntent(context: Context, file: File): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        val contentUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val mimeType = context.contentResolver.getType(contentUri)
        intent.setDataAndType(contentUri, mimeType)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION

        return intent
    }

}
