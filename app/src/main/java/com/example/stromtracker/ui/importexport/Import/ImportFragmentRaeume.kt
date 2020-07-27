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
import com.example.stromtracker.ui.home.HomeFragment
import com.example.stromtracker.ui.importexport.ImportExportViewModel

class ImportFragmentRaeume(
    private var companion: CompanionImport,
    private var haushaltidlist: IntArray,
    private var kategorienidlist: IntArray,
    private var kategorienneuidlist: ArrayList<Int>,
    private var raumErzeugtidlist: ArrayList<Int>,
    private var geraetelist: ArrayList<String>,
    private var urlaublist: ArrayList<String>
) : Fragment() {

    private lateinit var importexportViewModel: ImportExportViewModel
    private lateinit var raumlist: ArrayList<Raum>
    private lateinit var kategorieneulist: ArrayList<Kategorie>
    private lateinit var haushalttext: TextView
    private lateinit var kategorietext: TextView
    private lateinit var raumtext: TextView
    private lateinit var geraetetext: TextView
    private lateinit var urlaubtext: TextView
    private lateinit var tmpgeraet: Geraete
    private lateinit var fertigButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        importexportViewModel =
            ViewModelProvider(this).get(ImportExportViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_importexport_import, container, false)

        //Button finden und auf Invisible setzen bis man fertig ist mit dem Einfügen der restlichen Elemente
        fertigButton = root.findViewById<Button>(R.id.import_export_button_fertig)
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
                //Ftagment container aus content_main.xml muss ausgeählt werden, dann mit neuen Fragment ersetzen, dass oben erstellt wurde
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).commit()
                //und anschließend noch ein commit()
            }
        }

        return root
    }

    fun makeGeraete() {
        //Texte setzen
        haushalttext.text = getString(R.string.import_text_haushalte)
        kategorietext.text = getString(R.string.import_text_kategorien)
        raumtext.text = getString(R.string.import_text_raeume)

        //Anzahl an eingefügten Kategorien
        val eingefuegtekategorien = kategorienneuidlist.size
        //Die ID des ersten Elements, das neu Eingefügt wurde
        //TODO:Schauen wie das ist mit 0 und ob das überall so ist.
        var ersteneueID =
            kategorieneulist[kategorieneulist.size - 1 - eingefuegtekategorien].getKategorieID()
        //Fügt dem bereits existierenden Array die neuen IDs noch hinzu
        var zaehler = 0
        kategorienneuidlist.forEach { id ->
            kategorienidlist[id] = ersteneueID + zaehler
            zaehler++
        }

        //Jetzt noch das ganze für Raeume auch noch machen
        //Anzahl an eingefügten Raeume herausfinden
        val eingefuegteraeume = raumErzeugtidlist.size
        //Die ID des ersten Elements, das neu Eingefügt wurde
        ersteneueID = raumlist[raumlist.size - eingefuegteraeume].getRaumID()
        //Legt ein Array an, mit der größe der letzten ID um die neue ID zur vereinfachten Erstellung der neuen Objekte direkt dort hinein zu speichern
        val raumidarray = IntArray(raumErzeugtidlist[raumErzeugtidlist.size - 1] + 1)
        zaehler = 0
        //dem idarray die neuen IDs hinzufügen
        raumErzeugtidlist.forEach { id ->
            raumidarray[id] = ersteneueID + zaehler
            zaehler++
        }

        //Die neuen Geräte einfügen, mit der neuen HaushaltID und der neuen KategorieID
        geraeteErstellen()
        geraetetext.text = getString(R.string.import_text_geraete)

        //Als nächstes werden noch die Urlaube eingefügt
        urlaubeErstellen()
        urlaubtext.text = getString(R.string.import_text_urlaub)
        fertigButton.visibility = View.VISIBLE

    }

    private fun urlaubeErstellen() {
        //TODO: Urlauibe einfügen
    }

    private fun geraeteErstellen() {
        geraetelist.forEach { row ->
            val data = row.split(",")
            if (data[0].toInt() == 1) {
                //Gerät ist Produzent
                tmpgeraet = Geraete(
                    data[2],
                    kategorienidlist[data[3].toInt()],
                    raumErzeugtidlist[data[4].toInt()],
                    haushaltidlist[data[5].toInt()],
                    0.0,
                    null,
                    0.0,
                    null,
                    false,
                    data[11].toDouble(),
                    data[12].toDouble(),
                    data[13]
                )
                importexportViewModel.insertGeraet(tmpgeraet)
            } else if (data[0].toInt() == 2) {
                //Gerät ist Verbraucher und hat null bei standby und ausgefüllte Notiz
                tmpgeraet = Geraete(
                    data[2],
                    kategorienidlist[data[3].toInt()],
                    raumErzeugtidlist[data[4].toInt()],
                    haushaltidlist[data[5].toInt()],
                    data[6].toDouble(),
                    null,
                    data[8].toDouble(),
                    null,
                    data[10].toBoolean(),
                    data[11].toDouble(),
                    null,
                    data[13]
                )
                importexportViewModel.insertGeraet(tmpgeraet)
            } else if (data[0].toInt() == 3) {

                Toast.makeText(this.context, data[9].toString(), Toast.LENGTH_LONG).show()
                //Gerät ist Verbraucher und hat null bei standby und null bei Notiz
                tmpgeraet = Geraete(
                    data[2],
                    kategorienidlist[data[3].toInt()],
                    raumErzeugtidlist[data[4].toInt()],
                    haushaltidlist[data[5].toInt()],
                    data[6].toDouble(),
                    null,
                    data[8].toDouble(),
                    null,
                    data[10].toBoolean(),
                    data[11].toDouble(),
                    null,
                    null
                )
                importexportViewModel.insertGeraet(tmpgeraet)
            } else if (data[0].toInt() == 4) {
                //Gerät ist Verbraucher und hat standby und null bei Notiz
                tmpgeraet = Geraete(
                    data[2],
                    kategorienidlist[data[3].toInt()],
                    raumErzeugtidlist[data[4].toInt()],
                    haushaltidlist[data[5].toInt()],
                    data[6].toDouble(),
                    data[7].toDouble(),
                    data[8].toDouble(),
                    data[9].toDouble(),
                    data[10].toBoolean(),
                    data[11].toDouble(),
                    null,
                    null
                )
                importexportViewModel.insertGeraet(tmpgeraet)
            } else if (data[0].toInt() == 5) {
                //Geraet ist Verbraucher und hat sowohl Standby als auch Notiz
                tmpgeraet = Geraete(
                    data[2],
                    kategorienidlist[data[3].toInt()],
                    raumErzeugtidlist[data[4].toInt()],
                    haushaltidlist[data[5].toInt()],
                    data[6].toDouble(),
                    data[7].toDouble(),
                    data[8].toDouble(),
                    data[9].toDouble(),
                    data[10].toBoolean(),
                    data[11].toDouble(),
                    null,
                    data[13]
                )
                importexportViewModel.insertGeraet(tmpgeraet)
            } else {
                Toast.makeText(
                    this.context, "Fehler beim Einfügen eines Gerätes. Inkonsistente Daten",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun createList1() {
        raumlist = ArrayList()

        importexportViewModel.getAllRaeume().observe(
            viewLifecycleOwner,
            Observer { raeume ->
                if (raeume != null) {
                    raumlist.clear()
                    raumlist.addAll(raeume)
                    createList2()
                }
            }
        )
    }

    private fun createList2() {
        kategorieneulist = ArrayList()

        importexportViewModel.getAllKategorie().observe(
            viewLifecycleOwner,
            Observer { kategorie ->
                if (kategorie != null) {
                    kategorieneulist.clear()
                    kategorieneulist.addAll(kategorie)
                    makeGeraete()
                }
            }
        )
    }
}




