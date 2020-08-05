package com.example.stromtracker.ui.importexport

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.stromtracker.R
import com.example.stromtracker.database.*
import com.example.stromtracker.ui.SharedViewModel
import com.example.stromtracker.ui.importexport.Import.CompanionImport
import com.example.stromtracker.ui.importexport.Import.ImportFragmentRaumKategorien
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File
import java.io.FileReader
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ImportExportFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var haushaltlist: ArrayList<Haushalt>
    private lateinit var raumlist: ArrayList<Raum>
    private lateinit var kategorielist: ArrayList<Kategorie>
    private lateinit var geraetlist: ArrayList<Geraete>
    private lateinit var urlaublist: ArrayList<Urlaub>
    private lateinit var haushaltidlist: ArrayList<Int>
    private lateinit var textview: TextView
    private lateinit var outputtextfield: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel =
            ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_importexport, container, false)

        textview = root.findViewById(R.id.text_view_import_export_erklärung)
        outputtextfield = root.findViewById(R.id.edit_text_multiline_import)

        val exportbut: Button = root.findViewById(R.id.importexport_exportbutton)
        exportbut.setOnClickListener { view ->
            if (view != null) {
                writeCSVFile()
            }

        }
        val infobutton: Button = root.findViewById(R.id.button_import_export_info)
        infobutton.setOnClickListener { view ->
            if (view != null) {
                erklaerung()
            }
        }

        val importbut: Button = root.findViewById(R.id.importexport_importbutton)
        importbut.setOnClickListener { view ->
            if (view != null) {
                if (outputtextfield.text.isNotEmpty()) {
                    val csvFile = generateFile()
                    if (csvFile != null) {
                        // Bestätigungsdialog mithilfe von AlertDialog
                        val builder1: AlertDialog.Builder = AlertDialog.Builder(context)
                        builder1.setMessage(R.string.import_confirm)
                        builder1.setPositiveButton(
                            R.string.ja
                        ) { _, _ ->
                            csvFile.writeText(outputtextfield.text.toString())
                            getDatafromcsv(csvFile)
                        }
                        builder1.setNegativeButton(
                            R.string.nein
                        ) { dialog, _ -> dialog.cancel() }
                        val alert11: AlertDialog = builder1.create()
                        alert11.show()
                    }
                } else {
                    outputtextfieldInitialisierung()
                }
            }
        }

        createLists()
        return root
    }

    private fun erklaerung() {
        val builder1: AlertDialog.Builder = AlertDialog.Builder(context)
        builder1.setMessage(R.string.import_erklaerung3)
        builder1.setPositiveButton(
            R.string.ok
        ) { dialog, _ ->
            dialog.cancel()
        }

        var alert11: AlertDialog = builder1.create()
        alert11.show()

        builder1.setMessage(R.string.import_erklaerung2)
        builder1.setPositiveButton(
            R.string.ok
        ) { dialog, _ ->
            dialog.cancel()
        }

        alert11 = builder1.create()
        alert11.show()

        builder1.setMessage(R.string.import_erklaerung1)
        builder1.setPositiveButton(
            R.string.ok
        ) { dialog, _ ->
            dialog.cancel()
        }
        alert11 = builder1.create()
        alert11.show()
    }

    private fun getDatafromcsv(csv: File) {
        var zustand = 0
        var headerread = false
        val tempraumlist = ArrayList<String>()
        val tempkategorielist = ArrayList<String>()
        val tempgeraetelist = ArrayList<String>()
        val tempurlaublist = ArrayList<String>()
        haushaltidlist = ArrayList()

        val daten: ArrayList<String> = ArrayList()
        FileReader(csv).forEachLine { row: String ->

            //Zusand 0 sind die Haushalte
            if (zustand == 0) {
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
                                sharedViewModel.insertHaushalt(tempHaushalt)
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
                            sharedViewModel.insertHaushalt(tempHaushalt)
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

        CompanionImport.setHaushaltaltlist(haushaltlist)
        CompanionImport.setkategorienaltlist(kategorielist)
        CompanionImport.setraeumealtlist(raumlist)
        //neues Fragment erstellen auf das weitergeleitet werden soll
        val frag = ImportFragmentRaumKategorien(
            daten,
            haushaltidlist,
            tempraumlist,
            tempkategorielist,
            tempgeraetelist,
            tempurlaublist
        )
        //Fragment Manager aus Main Activity holen
        val fragMan = parentFragmentManager
        //Fragment container aus content_main.xml muss ausgeählt werden, dann mit neuen Fragment
        // ersetzen, dass oben erstellt wurde
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
                        "[GeraeteTyp]",  //Um  beim Import die unterschiedlichen Typen zu
                        // unterscheiden. 1 ist Produzent; 2,3,4 und 5 sind unterschiedliche
                        // verbraucher mit unterschidlichen Feldern leer
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
                    val geraetetyp = if (geraet.getJahresverbrauch() < 0) {
                        1
                    } else if (geraet.getStromStandBy() == null && geraet.getNotiz() != null) {
                        2
                    } else if (geraet.getStromStandBy() == null && geraet.getNotiz() == null) {
                        3
                    } else if (geraet.getStromStandBy() != null && geraet.getNotiz() == null) {
                        4
                    } else {
                        5
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
                            urlaub.getName(),
                            SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).format(
                                //Kann nicht null sein, da danach geprüft wird.
                                urlaub.getDateVon()
                            ),
                            SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).format(
                                //Kann nicht null sein, da danach geprüft wird.
                                urlaub.getDateBis()
                            ),
                            urlaub.getErsparnisProTag(),
                            urlaub.getHaushaltID()
                        )
                    )
                }


            }
            //Schreiben der CSV Dateiinhalte in den Input um dort dann die Daten rauszukopieren
            outputtextfield.setText(csvFile.readText())

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

        sharedViewModel.getAllHaushalt().observe(
            viewLifecycleOwner,
            Observer { haushalte ->
                if (haushalte != null) {
                    haushaltlist.clear()
                    haushaltlist.addAll(haushalte)
                }
            }
        )
        sharedViewModel.getAllRaeume().observe(
            viewLifecycleOwner,
            Observer { raeume ->
                if (raeume != null) {
                    raumlist.clear()
                    raumlist.addAll(raeume)
                }
            }
        )
        sharedViewModel.getAllKategorie().observe(
            viewLifecycleOwner,
            Observer { kategorien ->
                if (kategorien != null) {
                    kategorielist.clear()
                    kategorielist.addAll(kategorien)
                }
            }
        )
        sharedViewModel.getAllGeraete().observe(
            viewLifecycleOwner,
            Observer { geraete ->
                if (geraete != null) {
                    geraetlist.clear()
                    geraetlist.addAll(geraete)
                }
            }
        )
        sharedViewModel.getAllUrlaub().observe(
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

    private fun outputtextfieldInitialisierung() {
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
                //Jetzt die Raeume
                writeRow("-----------------------------------")
                writeRow(listOf("[Raumid]", "[Haushaltid]", "[Raumname]"))
                //Als naechstes die Kategorien
                writeRow("-----------------------------------")
                writeRow(listOf("[KategorienId]", "[Kategorienname]", "[Iconindex]"))
                //Jetzt die Geraete
                writeRow("-----------------------------------")
                writeRow(
                    listOf(
                        "[GeraeteTyp]",  //Um  beim Import die unterschiedlichen Typen zu
                        // unterscheiden. 1 ist Produzent; 2,3,4 und 5 sind unterschiedliche
                        // verbraucher mit unterschidlichen Feldern leer
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

            }
            outputtextfield.setText(csvFile.readText())
        }
    }
}
