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
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.stromtracker.R
import com.example.stromtracker.database.*
import com.example.stromtracker.ui.importexport.Import.CompanionImport
import com.example.stromtracker.ui.importexport.Import.ImportFragmentHaushalte
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File
import java.io.FileReader
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ImportExportFragment : Fragment() {

    private lateinit var importexportViewModel: ImportExportViewModel
    private lateinit var haushaltlist: ArrayList<Haushalt>
    private lateinit var raumlist: ArrayList<Raum>
    private lateinit var kategorielist: ArrayList<Kategorie>
    private lateinit var geraetlist: ArrayList<Geraete>
    private lateinit var urlaublist: ArrayList<Urlaub>
    private lateinit var haushaltidlist: ArrayList<Int>


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

        val importbut: Button = root.findViewById(R.id.importexport_importbutton)
        importbut.setOnClickListener { view ->
            if (view != null) {

            }
        }

        createLists()
        return root
    }

    private fun getDatafromcsv(csv: File) {
        var zustand = 0
        var headerread = false
        var tempraumlist = ArrayList<String>()
        val tempkategorielist = ArrayList<String>()
        var tempgeraetelist = ArrayList<String>()
        var tempurlaublist = ArrayList<String>()
        var zaehler = 0
        haushaltidlist = ArrayList()

        val daten: ArrayList<String> = ArrayList()
        FileReader(csv).forEachLine { row: String ->

            //Zusand 0 sind die Haushalte
            if (zustand == 0) {
                var akthaushaltid = haushaltlist[haushaltlist.size - 1].getHaushaltID()
                var haushaltzaehler = 0
                if (!headerread) {
                    headerread = true
                } else {
                    if (row == "-----------------------------------") {
                        zustand = 1
                        headerread = false
                    } else {
                        daten.add(row)
                        val data = row.split(",")
                        haushaltidlist.add(data[0].toInt())
                        if (data[5].isNotEmpty()) {
                            //try catch Block um Parser Fehler beim Datum abzufangen
                            try {
                                val haushaltname: String = data[1]
                                val strompreis = data[2].toDouble()
                                val personenanzahl = data[3].toInt()
                                val zaehlerstand = data[4].toDoubleOrNull()
                                val datum = SimpleDateFormat(
                                    "dd.MM.yyyy",
                                    Locale.GERMAN
                                ).parse(data[5])
                                val oekomix = data[6].toBoolean()
                                val tempHaushalt = Haushalt(
                                    haushaltname,
                                    strompreis,
                                    personenanzahl,
                                    zaehlerstand,
                                    datum,
                                    oekomix
                                )
                                //haushaltneuidlist.add(importexportViewModel.insertHaushalt(tempHaushalt))
                            } catch (e: ParseException) {
                                Toast.makeText(
                                    this.context,
                                    R.string.parse_error_datum,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                e.printStackTrace()
                            }

                        } else {
                            val haushaltname: String = data[1]
                            val strompreis = data[2].toDouble()
                            val personenanzahl = data[3].toInt()
                            val oekomix = data[4].toBoolean()
                            val tempHaushalt = Haushalt(
                                haushaltname,
                                strompreis,
                                personenanzahl,
                                null,
                                null,
                                oekomix
                            )
                            importexportViewModel.insertHaushalt(tempHaushalt)
                        }

                    }
                }
            }
            //Räume
            else if (zustand == 1) {
                if (!headerread) {
                    headerread = true
                } else {
                    if (row == "-----------------------------------") {
                        zustand = 2
                        headerread = false
                        zaehler = 0
                    } else {
                        tempraumlist.add(row)
                    }
                }
            }
            //Kategorie
            else if (zustand == 2) {
                if (!headerread) {
                    headerread = true
                } else {
                    if (row == "-----------------------------------") {
                        zustand = 3
                        headerread = false
                    } else {
                        tempkategorielist.add(row)
                    }
                }
            }
            //Geräte
            else if (zustand == 3) {
                if (!headerread) {
                    headerread = true
                } else {
                    if (row == "-----------------------------------") {
                        zustand = 4
                        headerread = false
                    } else {
                        tempgeraetelist.add(row)
                    }
                }
            }
            //Urlaub
            else if (zustand == 4) {
                if (!headerread) {
                    headerread = true
                } else {
                    if (row == "-----------------------------------") {
                        zustand = 5
                        headerread = false
                    } else {
                        tempurlaublist.add(row)
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    row,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        val impCompanion = CompanionImport(importexportViewModel)
        impCompanion.setHaushaltaltlist(haushaltlist)
        impCompanion.setgeraetealtlist(geraetlist)
        impCompanion.setkategorienaltlist(kategorielist)
        impCompanion.setraeumealtlist(raumlist)
        impCompanion.seturlaubaltlist(urlaublist)
        //neues Fragment erstellen auf das weitergeleitet werden soll
        val frag = ImportFragmentHaushalte(
            impCompanion,
            daten,
            haushaltidlist,
            tempraumlist,
            tempkategorielist,
            tempgeraetelist,
            tempurlaublist
        )
        //Fragment Manager aus Main Activity holen
        val fragMan = parentFragmentManager
        //Ftagment container aus content_main.xml muss ausgeählt werden, dann mit neuen Fragment ersetzen, dass oben erstellt wurde
        fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
            .addToBackStack(null).commit()
        //und anschließend noch ein commit()


    }

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
                        "[GeraeteTyp]",  //Um  beim Import die unterschiedlichen Typen zu unterscheiden. 1 ist Produzent; 2,3,4 und 5 sind unterschiedliche verbraucher mit unterschidlichen Feldern leer
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
                    var geraetetyp = 0
                    if (geraet.getJahresverbrauch() < 0) {
                        geraetetyp = 1
                    } else if (geraet.getStromStandBy() == null && geraet.getNotiz() != null) {
                        geraetetyp = 2
                    } else if (geraet.getStromStandBy() == null && geraet.getNotiz() == null) {
                        geraetetyp = 3
                    } else if (geraet.getStromStandBy() != null && geraet.getNotiz() == null) {
                        geraetetyp = 4
                    } else {
                        geraetetyp = 5
                    }
                    writeRow(
                        listOf(
                            geraetetyp,
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
                //Als letztes die Urlaube
                writeRow("-----------------------------------")
                writeRow(
                    listOf(
                        "[Urlaubid]",
                        "[HaushaltId]",
                        "[DatumVon]",
                        "[DatumBis]",
                        "[name]",
                        "[Ersparniss Pro Tag]"
                    )
                )
                urlaublist.forEachIndexed { _, urlaub ->
                    writeRow(
                        listOf(
                            urlaub.getUrlaubID(),
                            urlaub.getHaushaltID(),
                            urlaub.getDateVon(),
                            urlaub.getDateBis(),
                            urlaub.getName(),
                            urlaub.getErsparnisProTag()
                        )
                    )
                }


            }
            Toast.makeText(
                this.context,
                "CSV File wurde Erstellet" + csvFile.readText(),
                Toast.LENGTH_SHORT
            ).show()
            //TODO : für besseres Testen
            getDatafromcsv(csvFile)
            //val intent = goToFileIntent(requireContext(), csvFile)
            //startActivity(intent)

        } else {
            Toast.makeText(
                this.context,
                "CSV File konnte nicht erstellt werden",
                Toast.LENGTH_LONG
            ).show()

        }
    }

    private fun createLists() {
        haushaltlist = ArrayList()
        raumlist = ArrayList()
        kategorielist = ArrayList()
        geraetlist = ArrayList()
        urlaublist = ArrayList()

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
        importexportViewModel.getAllUrlaub().observe(
            viewLifecycleOwner,
            Observer { urlaube ->
                if (urlaube != null) {
                    urlaublist.clear()
                    urlaublist.addAll(urlaube)
                }
            }
        )
    }

    private fun generateFile(): File? {
        val csvFile = File(requireContext().filesDir, "StromtrackerRoomExport.csv")
        csvFile.createNewFile()

        return if (csvFile.exists()) {
            csvFile
        } else {
            null
        }
    }

    private fun goToFileIntent(context: Context, file: File): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        val contentUri =
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val mimeType = context.contentResolver.getType(contentUri)
        intent.setDataAndType(contentUri, mimeType)
        intent.flags =
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION

        return intent
    }

}
