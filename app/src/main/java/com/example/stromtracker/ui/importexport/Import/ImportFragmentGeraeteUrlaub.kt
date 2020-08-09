package com.example.stromtracker.ui.importexport.Import

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.stromtracker.R
import com.example.stromtracker.database.Geraete
import com.example.stromtracker.database.Kategorie
import com.example.stromtracker.database.Raum
import com.example.stromtracker.database.Urlaub
import com.example.stromtracker.ui.SharedViewModel
import com.example.stromtracker.ui.home.HomeFragment
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ImportFragmentGeraeteUrlaub(
    private var haushaltIDList: IntArray,
    private var kategorienIDList: IntArray,
    private var kategorieNeuIDList: ArrayList<Int>,
    private var raumErzeugtIDList: ArrayList<Int>,
    private var geraeteList: ArrayList<String>,
    private var urlaubList: ArrayList<String>
) : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var raumList: ArrayList<Raum>
    private lateinit var kategorieNeuList: ArrayList<Kategorie>
    private lateinit var haushalttext: TextView
    private lateinit var kategorietext: TextView
    private lateinit var raumtext: TextView
    private lateinit var geraetetext: TextView
    private lateinit var urlaubtext: TextView
    private lateinit var tmpGeraet: Geraete
    private lateinit var fertigButton: Button
    private lateinit var raumIDArray: IntArray

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedViewModel =
            ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_importexport_import, container, false)

        //Button finden und Invisible setzen,bis man fertig ist mit Einfügen der restlichen Elemente
        fertigButton = root.findViewById(R.id.import_export_button_fertig)
        fertigButton.visibility = View.INVISIBLE


        //Suchen der Fortschrittsindikatoren
        haushalttext = root.findViewById(R.id.text_view_import_haushalte)
        kategorietext = root.findViewById(R.id.text_view_import_kategorien)
        raumtext = root.findViewById(R.id.text_view_import_raeume)
        geraetetext = root.findViewById(R.id.text_view_import_geraete)
        urlaubtext = root.findViewById(R.id.text_view_import_urlaube)

        //Erstellen der Listen
        createList1()

        fertigButton.setOnClickListener { view ->
            if (view != null) {
                //neues Fragment erstellen auf das weitergeleitet werden soll
                val frag = HomeFragment()
                //Fragment Manager aus Main Activity holen
                val fragMan = parentFragmentManager
                //Ftagment container aus content_main.xml muss ausgeählt werden, dann mit neuen
                // ragment ersetzen, dass oben erstellt wurde
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).commit()
                //und anschließend noch ein commit()
            }
        }

        return root
    }

    private fun makeGeraete() {


        //Anzahl an eingefügten Kategorien
        val eingefuegteKategorien = kategorieNeuIDList.size
        //Die ID des ersten Elements, das neu Eingefügt wurde
        var ersteneueID =
            kategorieNeuList[kategorieNeuList.size - 1 - eingefuegteKategorien].getKategorieID()
        //Fügt dem bereits existierenden Array die neuen IDs noch hinzu
        var zaehler = 0
        kategorieNeuIDList.forEach { id ->
            kategorienIDList[id] = ersteneueID + zaehler
            zaehler++
        }

        //Jetzt noch das ganze für Raeume auch noch machen
        //Anzahl an eingefügten Raeume herausfinden
        val eingefuegteRaeume = raumErzeugtIDList.size
        //Die ID des ersten Elements, das neu Eingefügt wurde
        ersteneueID = raumList[raumList.size - eingefuegteRaeume].getRaumID()
        //Legt ein Array an, mit der größe der letzten ID um die neue ID zur vereinfachten
        // Erstellung der neuen Objekte direkt dort hinein zu speichern
        raumIDArray = IntArray(raumErzeugtIDList[raumErzeugtIDList.size - 1] + 1)
        zaehler = 0
        //dem idarray die neuen IDs hinzufügen
        raumErzeugtIDList.forEach { id ->
            raumIDArray[id] = ersteneueID + zaehler
            zaehler++
        }

        //Die neuen Geräte einfügen, mit der neuen HaushaltID und der neuen KategorieID
        geraeteErstellen()
        geraetetext.text = getString(R.string.import_text_geraete)

        //Als nächstes werden noch die Urlaube eingefügt
        urlaubeErstellen()
        urlaubtext.text = getString(R.string.import_text_urlaub)
        fertigButton.visibility = View.VISIBLE
        //Texte setzen
        haushalttext.text = getString(R.string.import_text_haushalte)
        kategorietext.text = getString(R.string.import_text_kategorien)
        raumtext.text = getString(R.string.import_text_raeume)

    }

    private fun urlaubeErstellen() {
        try {
            urlaubList.forEach { row ->
                val data = row.split(",")
                val tmpurlaub = Urlaub(
                    data[1],
                    SimpleDateFormat(
                        "dd.MM.yyyy",
                        Locale.GERMAN
                    ).parse(data[2])!!,
                    SimpleDateFormat(
                        "dd.MM.yyyy",
                        Locale.GERMAN
                    ).parse(data[3])!!,
                    data[4].toDouble(),
                    haushaltIDList[data[5].toInt()]
                )
                sharedViewModel.insertUrlaub(tmpurlaub)
            }
        } catch (e: ParseException) {
            Toast.makeText(
                this.context,
                R.string.parse_error_datum,
                Toast.LENGTH_SHORT
            )
                .show()
            e.printStackTrace()
        }

    }

    private fun geraeteErstellen() {
        geraeteList.forEach { row ->
            val data = row.split(",")
            when {
                data[0].toInt() == 1 -> {
                    //Gerät ist Produzent
                    tmpGeraet = Geraete(
                        data[2],
                        kategorienIDList[data[3].toInt()],
                        raumIDArray[data[4].toInt()],
                        haushaltIDList[data[5].toInt()],
                        0.0,
                        null,
                        0.0,
                        null,
                        false,
                        data[11].toDouble(),
                        data[12].toDouble(),
                        data[13]
                    )
                    sharedViewModel.insertGeraet(tmpGeraet)
                }
                data[0].toInt() == 2 -> {
                    //Gerät ist Verbraucher und hat null bei standby und ausgefüllte Notiz
                    tmpGeraet = Geraete(
                        data[2],
                        kategorienIDList[data[3].toInt()],
                        raumIDArray[data[4].toInt()],
                        haushaltIDList[data[5].toInt()],
                        data[6].toDouble(),
                        null,
                        data[8].toDouble(),
                        null,
                        data[10].toBoolean(),
                        data[11].toDouble(),
                        null,
                        data[13]
                    )
                    sharedViewModel.insertGeraet(tmpGeraet)
                }
                data[0].toInt() == 3 -> {
                    //Gerät ist Verbraucher und hat null bei standby und null bei Notiz
                    tmpGeraet = Geraete(
                        data[2],
                        kategorienIDList[data[3].toInt()],
                        raumIDArray[data[4].toInt()],
                        haushaltIDList[data[5].toInt()],
                        data[6].toDouble(),
                        null,
                        data[8].toDouble(),
                        null,
                        data[10].toBoolean(),
                        data[11].toDouble(),
                        null,
                        null
                    )
                    sharedViewModel.insertGeraet(tmpGeraet)
                }
                data[0].toInt() == 4 -> {
                    //Gerät ist Verbraucher und hat standby und null bei Notiz
                    tmpGeraet = Geraete(
                        data[2],
                        kategorienIDList[data[3].toInt()],
                        raumIDArray[data[4].toInt()],
                        haushaltIDList[data[5].toInt()],
                        data[6].toDouble(),
                        data[7].toDouble(),
                        data[8].toDouble(),
                        data[9].toDouble(),
                        data[10].toBoolean(),
                        data[11].toDouble(),
                        null,
                        null
                    )
                    sharedViewModel.insertGeraet(tmpGeraet)
                }
                data[0].toInt() == 5 -> {
                    //Geraet ist Verbraucher und hat sowohl Standby als auch Notiz
                    tmpGeraet = Geraete(
                        data[2],
                        kategorienIDList[data[3].toInt()],
                        raumIDArray[data[4].toInt()],
                        haushaltIDList[data[5].toInt()],
                        data[6].toDouble(),
                        data[7].toDouble(),
                        data[8].toDouble(),
                        data[9].toDouble(),
                        data[10].toBoolean(),
                        data[11].toDouble(),
                        null,
                        data[13]
                    )
                    sharedViewModel.insertGeraet(tmpGeraet)
                }
                else -> {
                    Toast.makeText(
                        this.context,
                        getString(R.string.import_export_inkonsistenz_geraet),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }

    private fun createList1() {
        raumList = ArrayList()
        var aufgerufen = false
        sharedViewModel.getAllRaeume().observe(
            viewLifecycleOwner,
            Observer { raeume ->
                if (raeume != null) {
                    raumList.clear()
                    raumList.addAll(raeume)
                    //Überprüfung ob alle Eingefügt, damit Funktion nicht mehrmals aufgerufen wird
                    if (!aufgerufen && raumList.size == CompanionImport.getraeumealtlist().size +
                        raumErzeugtIDList.size
                    ) {
                        createList2()
                        aufgerufen = true
                    }
                }
            }
        )
    }

    private fun createList2() {
        kategorieNeuList = ArrayList()
        var executed = false
        sharedViewModel.getAllKategorie().observe(
            viewLifecycleOwner,
            Observer { kategorie ->
                if (kategorie != null) {
                    kategorieNeuList.clear()
                    kategorieNeuList.addAll(kategorie)
                    //Überprüfung ob alle eingefügt, sodass nicht mehrmals Funktion aufgerufen wird
                    if (kategorieNeuList.size == CompanionImport.getkategoriealtlist().size +
                        kategorieNeuIDList.size && !executed
                    ) {
                        makeGeraete()
                        executed = true
                    }
                }
            }
        )
    }
}
