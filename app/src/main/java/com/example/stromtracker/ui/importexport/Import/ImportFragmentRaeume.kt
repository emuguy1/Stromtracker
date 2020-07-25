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
import com.example.stromtracker.database.Raum
import com.example.stromtracker.ui.importexport.ImportExportViewModel

class ImportFragmentRaeume(
    private var companion: CompanionImport,
    private var haushaltidlist: IntArray,
    private var kategorielist: ArrayList<String>,
    private var geraetelist: ArrayList<String>,
    private var urlauiblist: ArrayList<String>
) : Fragment() {

    private lateinit var importexportViewModel: ImportExportViewModel
    private lateinit var raumlist: ArrayList<Raum>
    private lateinit var haushalttext: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        importexportViewModel =
            ViewModelProvider(this).get(ImportExportViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_importexport_import, container, false)
        createLists()
        root.findViewById<Button>(R.id.import_export_button_fertig).visibility = View.INVISIBLE
        haushalttext = root.findViewById(R.id.text_view_import_haushalte)
        return root
    }

    fun makeKategorien() {

        //Alte Haushaltliste setzen, sodass nicht die Standardräume nochmal erzeugt werden
//        val mainact = requireActivity() as MainActivity
//        mainact.setOldHaushaltList(haushaltlist)
//        //Anzahl an eingefügten Haushalten
//        val eingefügteHaushalte=daten.size
//        //Die ID des ersten Elements, das neu Eingefügt wurde
//        val ersteneueID=haushaltlist[haushaltlist.size-eingefügteHaushalte].getHaushaltID()
//        //Legt ein Array an, mit der größe der letzten ID um die neue ID zur vereinfachten Erstellung der neuen Objekte direkt dort hinein zu speichern
//        val idarray=IntArray(alteHaushalteIDList[alteHaushalteIDList.size-1]+1)
//        var zaehler=0
//        //dem idarray die neuen IDs hinzufügen
//        alteHaushalteIDList.forEach{id->
//            idarray[id]=ersteneueID+zaehler
//            zaehler++
//        }
//        //Die neuen Räume einfügen, mit der neuen HaushaltID
//        val raumidlist:ArrayList<Int> = ArrayList()
//        raumlist.forEach{row->
//            val data= row.split(",")
//            raumidlist.add(data[0].toInt())
//            var tmpraum=Raum(data[2],idarray[data[1].toInt()])
//            importexportViewModel.insertRaum(tmpraum)
//        }
//
//
//        Toast.makeText(
//            requireContext(),
//            idarray[alteHaushalteIDList[0]].toString(),
//            Toast.LENGTH_SHORT
//        ).show()
//
//        haushalttext.text="Haushalte wurden erstellt"
//        //neues Fragment erstellen auf das weitergeleitet werden soll
////        val frag = ImportFragmentRaeume(companion,idarray,)
//        //Fragment Manager aus Main Activity holen
//        val fragMan = parentFragmentManager
//        //Ftagment container aus content_main.xml muss ausgeählt werden, dann mit neuen Fragment ersetzen, dass oben erstellt wurde
////        fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
////            .addToBackStack(null).commit()
//        //und anschließend noch ein commit()
    }

    private fun createLists() {
        raumlist = ArrayList()

        importexportViewModel.getAllRaeume().observe(
            viewLifecycleOwner,
            Observer { haushalte ->
                if (haushalte != null) {
                    raumlist.clear()
                    raumlist.addAll(haushalte)
                    makeKategorien()
                }
            }
        )
    }
}




