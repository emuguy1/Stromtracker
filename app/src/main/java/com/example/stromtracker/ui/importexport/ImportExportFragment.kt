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

        //for better texting
        //TODO: delete before publish

        val testText =
            "[HaushaltIdid],[Name],[Stromkosten],[bewohnerAnzahl],[zaehlerstand],[datum],[oekostrom]\n" +
                    "5,Haushalt1,26.5,1,2500.0,22.10.2020,true\n" +
                    "6,Haushalt2,26.534,1,187187.0,11.11.2020,true\n" +
                    "-----------------------------------\n" +
                    "[Raumid],[Haushaltid],[Raumname]\n" +
                    "19,5,Sonstiges\n" +
                    "20,5,Wohnzimmer\n" +
                    "21,5,Schlafzimmer\n" +
                    "22,5,Garten\n" +
                    "23,6,Sonstiges\n" +
                    "24,6,Wohnzimmer\n" +
                    "25,6,Schlafzimmer\n" +
                    "26,6,Garten\n" +
                    "27,5,Küche\n" +
                    "28,5,Badezimmer\n" +
                    "-----------------------------------\n" +
                    "[KategorienId],[Kategorienname],[Iconindex]\n" +
                    "1,Sonstiges,7\n" +
                    "2,Fernseher,0\n" +
                    "3,Gaming,1\n" +
                    "4,Unterhaltung,2\n" +
                    "5,Kühlung,3\n" +
                    "6,Kochen,4\n" +
                    "7,Waschen,5\n" +
                    "8,Lampen,6\n" +
                    "9,Stromerzeugung,8\n" +
                    "11,MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM,5\n" +
                    "-----------------------------------\n" +
                    "[GeraeteTyp],[GeraeteID],[Geraetename],[KategorieID],[RaumID],[HaushaltID],[StromverbrauchVollast],[StromverbrauchStandBy],[BetriebszeitVollast],[BetriebszeitStandBy],[Urlaubsmodus],[Jahresverbrauch],[Eigenverbrauch],[Notiz]\n" +
                    "4,22,PC-Bildschirm Groß,2,20,5,45.0,1.0,3.0,20.0,true,56.58,,\n" +
                    "5,23,Fernseher,2,20,5,70.0,2.0,2.0,22.0,true,67.16,,Richtig nice 4K crispy\n" +
                    "2,24,Kühlschrank,5,27,5,10.0,,24.0,,false,87.6,,Schön mit Eiswürfeln Dies das Annanas\n" +
                    "3,25,Diebstahlschutzlampe,8,20,5,30.0,,5.0,,false,54.75,,\n" +
                    "1,26,PV Gartenhaus,9,19,5,0.0,,0.0,,false,-2000.0,3.6,\n" +
                    "1,27,PVDach,9,22,5,0.0,,0.0,,false,-6800.0,2.9,ballert die sonne richtig rein\n" +
                    "4,28,PC-Bildschirm Wohzimmer,3,24,6,45.0,1.0,4.0,20.0,true,73.0,,\n" +
                    "5,29,Fernseher,4,24,6,70.0,2.0,2.0,22.0,true,67.16,,Richtig nice 4K crispy\n" +
                    "2,30,Kühlschrank,6,23,6,10.0,,24.0,,false,87.6,,Schön mit Eiswürfeln Dies das Annanas\n" +
                    "3,31,Diebstahlschutzlampe,8,24,6,30.0,,5.0,,false,54.75,,\n" +
                    "1,32,PV Gartenhaus,9,23,6,0.0,,0.0,,false,-2000.0,2.3,\n" +
                    "1,33,PVDach,9,26,6,0.0,,0.0,,false,-6800.0,3.7,ballert die sonne richtig rein\n" +
                    "4,34,Computer,3,20,5,130.0,0.5,3.0,21.0,true,146.18,,\n" +
                    "4,35,PC Bildschirm IPS,2,20,5,60.0,1.5,3.0,21.0,true,77.2,,\n" +
                    "3,36,Spülmaschine,7,27,5,400.0,,0.5,,true,73.0,,\n" +
                    "3,37,Herd,6,27,5,500.0,,1.0,,true,182.5,,\n" +
                    "3,38,Mikrowelle,6,27,5,600.0,,0.25,,true,54.75,,\n" +
                    "3,39,Kaffemaschine,1,27,5,165.0,,1.0,,false,60.225,,\n" +
                    "3,40,Küchenlampe,8,27,5,10.0,,5.0,,false,18.25,,\n" +
                    "3,41,Musikbox,4,21,5,50.0,,0.7,,false,12.775,,\n" +
                    "4,42,Elektrische Zahnbürste,1,28,5,10.0,2.0,2.0,22.0,false,23.36,,\n" +
                    "3,43,Föhn,1,28,5,100.0,,0.2,,false,7.3,,\n" +
                    "3,44,Nachttischlampe,1,27,5,15.0,,2.0,,false,10.95,,\n" +
                    "3,45,Badlicht,8,28,5,10.0,,1.0,,false,3.65,,\n" +
                    "4,46,Radio,4,19,5,45.0,3.0,1.0,23.0,true,41.61,,\n" +
                    "-----------------------------------\n" +
                    "[Urlaubid],[HaushaltId],[DatumVon],[DatumBis],[name],[Ersparniss Pro Tag]\n" +
                    "5,wdaw,14.06.2018,14.07.2018,-0.628,5\n" +
                    "6,Alf der Rummelbär,20.03.2020,20.09.2020,0.384,5\n" +
                    "7,wdaw,14.06.2018,14.07.2018,-0.628,6\n" +
                    "8,Alf der Rummelbär,20.03.2020,20.09.2020,0.384,6"
        outputtextfield.setText(testText)

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
        // Importfunktion
        // Im ersten Schritt werden die Haushalte eingefügt und dann auf ein neues Fragment
        // übergeleitet. Dort wird dann Raum und Kategorie eingefügt und dann die Geräte und Urlaube
        // Es können immer nur komplette Datenätze eingefügt werden und nicht nur ein Gerät
        // oder einen einzelnen Raum zu einem bestehenem Haushalt.
        var zustand = 0
        var headerread = false
        val tempraumlist = ArrayList<String>()
        val tempkategorielist = ArrayList<String>()
        val tempgeraetelist = ArrayList<String>()
        val tempurlaublist = ArrayList<String>()
        haushaltidlist = ArrayList()

        val daten: ArrayList<String> = ArrayList()
        FileReader(csv).forEachLine { row: String ->

            // Zusand 0 sind die Haushalte
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
                            // try catch Block um Parser Fehler beim Datum abzufangen
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
            // Räume
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
            // Kategorie
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
            // Geräte
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
            // Urlaub
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
        // neues Fragment erstellen auf das weitergeleitet werden soll
        val frag = ImportFragmentRaumKategorien(
            daten,
            haushaltidlist,
            tempraumlist,
            tempkategorielist,
            tempgeraetelist,
            tempurlaublist
        )
        // Fragment Manager aus Main Activity holen
        val fragMan = parentFragmentManager
        // Fragment container aus content_main.xml muss ausgeählt werden, dann mit neuen Fragment
        // ersetzen, dass oben erstellt wurde
        fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
            .addToBackStack(null).commit()
        // und anschließend noch ein commit()

    }

    private fun writeCSVFile() {
        // Daten exportieren
        val csvFile = generateFile()
        if (csvFile != null) {
            // Als erstes werden die Haushalte ins File geschrieben
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
                                    // Kann nicht null sein, da danach geprüft wird.
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
                // Jetzt die Raeume
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
                // Als naechstes die Kategorien
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
                // Jetzt die Geraete
                writeRow("-----------------------------------")
                writeRow(
                    listOf(
                        "[GeraeteTyp]", // Um  beim Import die unterschiedlichen Typen zu
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
                // Als letztes die Urlaube
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
                                // Kann nicht null sein, da danach geprüft wird.
                                urlaub.getDateVon()
                            ),
                            SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).format(
                                // Kann nicht null sein, da danach geprüft wird.
                                urlaub.getDateBis()
                            ),
                            urlaub.getErsparnisProTag(),
                            urlaub.getHaushaltID()
                        )
                    )
                }

            }
            // Schreiben der CSV Dateiinhalte in den Input um dort dann die Daten rauszukopieren
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
            // Als erstes werden die Haushalte ins File geschrieben
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
                // Jetzt die Raeume
                writeRow("-----------------------------------")
                writeRow(listOf("[Raumid]", "[Haushaltid]", "[Raumname]"))
                // Als naechstes die Kategorien
                writeRow("-----------------------------------")
                writeRow(listOf("[KategorienId]", "[Kategorienname]", "[Iconindex]"))
                // Jetzt die Geraete
                writeRow("-----------------------------------")
                writeRow(
                    listOf(
                        "[GeraeteTyp]", // Um  beim Import die unterschiedlichen Typen zu
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

                // Als letztes die Urlaube
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
