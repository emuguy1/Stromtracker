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
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.database.Kategorie
import com.example.stromtracker.database.Raum
import com.example.stromtracker.ui.SharedViewModel
import com.example.stromtracker.ui.haushalt.HaushaltFragment
import com.example.stromtracker.ui.home.HomeFragment

class ImportFragmentRaumKategorien(
    private var daten: ArrayList<String>,
    private var alteHaushalteIDList: ArrayList<Int>,
    private var raumlist: ArrayList<String>,
    private var kategorielist: ArrayList<String>,
    private var geraetestringlist: ArrayList<String>,
    private var urlaubstringlist: ArrayList<String>
) : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var haushaltlist: ArrayList<Haushalt>
    private var kategoriealtlist = CompanionImport.getkategoriealtlist()
    private lateinit var haushalttext: TextView
    private lateinit var kategorieneuidlist: ArrayList<Int>
    private lateinit var katidarray: IntArray

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedViewModel =
            ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_importexport_import, container, false)

        //Text feld zur Fortschrittsanzeige finden
        haushalttext = root.findViewById(R.id.text_view_import_haushalte)

        //Button finden und Invisible setzen bis man fertig ist mit Einfügen der restlichen Elemente
        val fertigButton = root.findViewById<Button>(R.id.import_export_button_fertig)
        fertigButton.visibility = View.INVISIBLE

        //Haushaltlist holen, die die neuen Werte enthält.
        // Sobald diese die Datzen erhalten hat, wird die make Raeume Methode aufgerufen
        createList()

        return root
    }

    private fun makeRaeume() {
        //Alte Haushaltliste setzen, sodass nicht die Standardräume nochmal erzeugt werden
        HaushaltFragment.setOldHaushaltList(haushaltlist)

        //Anzahl an eingefügten Haushalten herausfinden
        val eingefuegteHaushalte = daten.size
        //Wenn keine Haushalte dabei sind, werden auuch keine Daten eingefügt,
        // da immer nur komplette Haushalte importiert werden können
        if (eingefuegteHaushalte != 0) {
            //Die ID des ersten Elements, das neu Eingefügt wurde
            val ersteneueID = haushaltlist[haushaltlist.size - eingefuegteHaushalte].getHaushaltID()

            //Legt ein Array an, mit der größe der letzten ID um die neue ID zur vereinfachten
            // Erstellung der neuen Objekte direkt dort hinein zu speichern
            val idarray = IntArray(alteHaushalteIDList[alteHaushalteIDList.size - 1] + 1)
            var zaehler = 0
            //dem idarray die neuen IDs hinzufügen
            alteHaushalteIDList.forEach { id ->
                idarray[id] = ersteneueID + zaehler
                zaehler++
            }

            //Die neuen Räume einfügen, mit der neuen HaushaltID
            val raumidlist: ArrayList<Int> = ArrayList()
            raumlist.forEach { row ->
                val data = row.split(",")
                raumidlist.add(data[0].toInt())
                val tmpraum = Raum(data[2], idarray[data[1].toInt()])
                sharedViewModel.insertRaum(tmpraum)
            }

            //nun werden noch die kategorien erzeugt,
            // sodass im nächsten Schritt dann gleich die Geräte hinzugefügt werden können.
            createkategorien()

            haushalttext.text = getString(R.string.import_text_haushalte)
            //neues Fragment erstellen auf das weitergeleitet werden soll
            val frag = ImportFragmentGeraeteUrlaub(
                idarray,
                katidarray,
                kategorieneuidlist,
                raumidlist,
                geraetestringlist,
                urlaubstringlist
            )
            //Fragment Manager aus Main Activity holen
            val fragMan = parentFragmentManager
            //Ftagment container aus content_main.xml muss ausgeählt werden,
            // dann mit neuen Fragment ersetzen, dass oben erstellt wurde
            fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                .addToBackStack(null).commit()
            //und anschließend noch ein commit()
        } else {
            Toast.makeText(
                this.context,
                getString(R.string.import_export_keine_daten_zum_importieren),
                Toast.LENGTH_LONG
            ).show()
            //neues Fragment erstellen auf das weitergeleitet werden soll
            val frag = HomeFragment()
            //Fragment Manager aus Main Activity holen
            val fragMan = parentFragmentManager
            //Ftagment container aus content_main.xml muss ausgeählt werden, dann mit neuen Fragment
            // ersetzen, dass oben erstellt wurde
            fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).commit()
            //und anschließend noch ein commit()
        }


    }

    private fun createkategorien() {
        //höchste ID der Kategorien bei denen die Importiert werden sollen, finden und damit
        // ein IntArray erzeugen, wo die Kategorieids hinzugefügt werden können
        katidarray = IntArray(
            kategorielist[kategorielist.size - 1].split(",")[0].toInt() + 1
        )
        //Damit wir den noch verbleibenden neu angelegten Kategorien noch die neuen IDs hinzufügen
        // können, werden diese noch in ein IntArray gespeichert, das mit übergeben wird
        kategorieneuidlist = ArrayList()
        //kategorien erstellen
        kategorielist.forEach { row ->
            val data = row.split(",")
            var found = false
            kategoriealtlist.forEach { kat ->
                if (kat.getName() == data[1] && kat.getIcon() == data[2].toInt()) {
                    found = true
                    //Wenn Kategorie gefunden wurde, neue ID in das entsprechende IntArray speichern
                    katidarray[data[0].toInt()] = kat.getKategorieID()
                }
            }
            if (!found) {
                kategorieneuidlist.add(data[0].toInt())
                val tempkat = Kategorie(data[1], data[2].toInt())
                sharedViewModel.insertKategorie(tempkat)
            }
        }
    }

    private fun createList() {
        haushaltlist = ArrayList()

        sharedViewModel.getAllHaushalt().observe(
            viewLifecycleOwner,
            Observer { haushalte ->
                if (haushalte != null) {
                    haushaltlist.clear()
                    haushaltlist.addAll(haushalte)
                    if (haushaltlist.size == CompanionImport.getHaushaltaltlist().size
                        + daten.size
                    ) {
                        makeRaeume()
                    }
                }
            }
        )
    }
}




