package com.example.stromtracker.ui.kategorien

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class KategorienFragment : Fragment(), View.OnClickListener {

    private lateinit var kategorienViewModel: KategorienViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var buttonAdd: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //kategorienViewModel einbinden
        kategorienViewModel =
            ViewModelProviders.of(this).get(com.example.stromtracker.ui.kategorien.KategorienViewModel::class.java)

        //root festlegen -> root ist ConstraintLayout
        val root = inflater.inflate(R.layout.fragment_kategorien, container, false)

        //Datenarray anlegen, damit kein leerer String an die Recyclerview übergeben wird, so ist mindestens immer eine leere Kategorie vorhanden
        var myDataset = arrayOf("")

        //Daten für Recyclerview holen / erstellen
        //TODO Bestehende Kategorien holen und hier einfügen
        myDataset = arrayOf("Kat1", "Kat2", "Kat3", "Kat4", "Kat5", "Kat6", "Kat7", "Kat8", "Kat9", "Kat10")

        //RecyclerView initialisieren
        viewAdapter = KategorienListAdapter(myDataset)
        viewManager = LinearLayoutManager(this.context)
        recyclerView = root.findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        //Buttons finden und Click Listener zuweisen
        buttonAdd = root.findViewById(R.id.kategorie_button_add)
        buttonAdd.setOnClickListener(this)

        return root
    }

    override fun onClick(v : View) {
        //switch-case in Kotlin: (Zur Unterscheidung der Buttons. Hier eigentlich nicht notwendig)
        when (v.id) {
            R.id.kategorie_button_add -> {
                Toast.makeText(v.context, "Add Button clicked.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(v.context, String.format(Locale.GERMAN,"%d was pressed.", v.id), Toast.LENGTH_SHORT).show()
            }
        }
    }
}