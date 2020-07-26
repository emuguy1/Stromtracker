package com.example.stromtracker.ui.importexport.Import

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.stromtracker.R
import com.example.stromtracker.database.Geraete
import com.example.stromtracker.database.Kategorie
import com.example.stromtracker.database.Raum
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        importexportViewModel =
            ViewModelProvider(this).get(ImportExportViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_importexport_import, container, false)
        createList1()
        root.findViewById<Button>(R.id.import_export_button_fertig).visibility = View.INVISIBLE
        haushalttext = root.findViewById(R.id.text_view_import_haushalte)
        kategorietext = root.findViewById(R.id.text_view_import_kategorien)
        raumtext = root.findViewById(R.id.text_view_import_raeume)
        return root
    }

    fun makeGeraete() {
        //Texte setzen
        haushalttext.text = "Haushalte wurden erstellt"
        kategorietext.text = "Kategorien wurden erstellt"
        raumtext.text = "Raeume wurden erstellt"

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
        geraetelist.forEach { row ->
            val data = row.split(",")
            var tmpgeraet = Geraete(
                data[1],
                kategorienidlist[data[2].toInt()],
                raumErzeugtidlist[data[3].toInt()],
                haushaltidlist[data[4].toInt()],
                data[5].toDouble(),
                data[6].toDouble(),
                data[7].toDouble(),
                data[8].toDouble(),
                data[9].toBoolean(),
                data[10].toDouble(),
                data[11].toDouble(),
                data[12]
            )
            importexportViewModel.insertGeraet(tmpgeraet)
        }


        //neues Fragment erstellen auf das weitergeleitet werden soll
//        val frag = ImportFragmentRaeume(companion,idarray,)
        //Fragment Manager aus Main Activity holen
        val fragMan = parentFragmentManager
        //Ftagment container aus content_main.xml muss ausgeählt werden, dann mit neuen Fragment ersetzen, dass oben erstellt wurde
//        fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
//            .addToBackStack(null).commit()
        //und anschließend noch ein commit()
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




