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
    private var raumList: ArrayList<String>,
    private var kategorieList: ArrayList<String>,
    private var geraeteStringList: ArrayList<String>,
    private var urlaubStringList: ArrayList<String>
) : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var haushaltList: ArrayList<Haushalt>
    private var kategorieAltList = CompanionImport.getkategoriealtlist()
    private lateinit var haushalttext: TextView
    private lateinit var kategorieNeuIDList: ArrayList<Int>
    private lateinit var kategorieIDArray: IntArray

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
        HaushaltFragment.setOldHaushaltList(haushaltList)

        //Anzahl an eingefügten Haushalten herausfinden
        val eingefuegteHaushalte = daten.size
        //Wenn keine Haushalte dabei sind, werden auuch keine Daten eingefügt,
        // da immer nur komplette Haushalte importiert werden können
        if (eingefuegteHaushalte != 0) {
            //Die ID des ersten Elements, das neu Eingefügt wurde
            val ersteneueID = haushaltList[haushaltList.size - eingefuegteHaushalte].getHaushaltID()

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
            val raumIDList: ArrayList<Int> = ArrayList()
            raumList.forEach { row ->
                val data = row.split(",")
                raumIDList.add(data[0].toInt())
                val tmpRaum = Raum(data[2], idarray[data[1].toInt()])
                sharedViewModel.insertRaum(tmpRaum)
            }

            //nun werden noch die kategorien erzeugt,
            // sodass im nächsten Schritt dann gleich die Geräte hinzugefügt werden können.
            createKategorien()

            haushalttext.text = getString(R.string.import_text_haushalte)
            //neues Fragment erstellen auf das weitergeleitet werden soll
            val frag = ImportFragmentGeraeteUrlaub(
                idarray,
                kategorieIDArray,
                kategorieNeuIDList,
                raumIDList,
                geraeteStringList,
                urlaubStringList
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

    private fun createKategorien() {
        //höchste ID der Kategorien bei denen die Importiert werden sollen, finden und damit
        // ein IntArray erzeugen, wo die Kategorieids hinzugefügt werden können
        kategorieIDArray = IntArray(
            kategorieList[kategorieList.size - 1].split(",")[0].toInt() + 1
        )
        //Damit wir den noch verbleibenden neu angelegten Kategorien noch die neuen IDs hinzufügen
        // können, werden diese noch in ein IntArray gespeichert, das mit übergeben wird
        kategorieNeuIDList = ArrayList()
        //kategorien erstellen
        kategorieList.forEach { row ->
            val data = row.split(",")
            var found = false
            kategorieAltList.forEach { kat ->
                if (kat.getName() == data[1] && kat.getIcon() == data[2].toInt()) {
                    found = true
                    //Wenn Kategorie gefunden wurde, neue ID in das entsprechende IntArray speichern
                    kategorieIDArray[data[0].toInt()] = kat.getKategorieID()
                }
            }
            if (!found) {
                kategorieNeuIDList.add(data[0].toInt())
                val tempkat = Kategorie(data[1], data[2].toInt())
                sharedViewModel.insertKategorie(tempkat)
            }
        }
    }

    private fun createList() {
        haushaltList = ArrayList()

        sharedViewModel.getAllHaushalt().observe(
            viewLifecycleOwner,
            Observer { haushalte ->
                if (haushalte != null) {
                    haushaltList.clear()
                    haushaltList.addAll(haushalte)
                    if (haushaltList.size == CompanionImport.getHaushaltaltlist().size
                        + daten.size
                    ) {
                        makeRaeume()
                    }
                }
            }
        )
    }
}




