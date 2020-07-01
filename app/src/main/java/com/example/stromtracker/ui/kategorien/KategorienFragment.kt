package com.example.stromtracker.ui.kategorien

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.R
import com.example.stromtracker.database.Kategorie
import com.example.stromtracker.ui.kategorien.new_kategorie.KategorienNewFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

import kotlin.collections.ArrayList


class KategorienFragment : Fragment(), View.OnClickListener {

    private lateinit var kategorienViewModel: KategorienViewModel
    private lateinit var myKategorien : ArrayList<Kategorie>
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var buttonAdd: FloatingActionButton
    private lateinit var root:View
    private val iconArray:Array<Int> = arrayOf<Int>(
        R.drawable.ic_kategorien_monitor, R.drawable.ic_kategorien_joystick, R.drawable.ic_kategorien_speaker,
        R.drawable.ic_kategorien_refrigerator, R.drawable.ic_kategorie_oven, R.drawable.ic_kategorien_washing_machine,
        R.drawable.ic_kategorien_light, R.drawable.ic_kategorien_plug)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //root festlegen -> root ist ConstraintLayout
        root = inflater.inflate(R.layout.fragment_kategorien, container, false)

        myKategorien = ArrayList()

        //RecyclerView mit geholten Daten aus DB initialisieren
        viewAdapter = KategorienListAdapter(myKategorien, iconArray)
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //View Model zuweisen, benötigt für DB Zugriff
        kategorienViewModel = ViewModelProviders.of(this).get(KategorienViewModel::class.java)
        kategorienViewModel.getAllKategorie().observe(
            viewLifecycleOwner,
            Observer { kategorien ->
                if (kategorien != null) {
                    if(kategorien.isEmpty()) {
                        //Fügt Standardeinträge zu den Kategorien hinzu, diese sind editier- und löschbar
                        initKategorien()
                    }
                    Log.d("TAGkategorien", kategorien.toString())
                    myKategorien.clear()

                    //Kategorien alphabetisch sortieren. Geht nicht in DB, da dort kein toLower Case angewendet wird
                    // -> klein geschriebene Kategorien würden unter groß geschriebenen stehen
                    myKategorien.addAll(kategorien.sortedWith(compareBy({it.getName().toLowerCase(Locale.ROOT)}, {it.getKategorieID()})))

                    viewAdapter.notifyDataSetChanged()
                }
            }
        )
    }

    private fun initKategorien () {
        var kat : Kategorie = Kategorie("Fernseher", 0)
        kategorienViewModel.insertKategorie(kat)
        kat = Kategorie("Gaming", 1)
        kategorienViewModel.insertKategorie(kat)
        kat = Kategorie("Unterhaltung", 2)
        kategorienViewModel.insertKategorie(kat)
        kat = Kategorie("Kühlung", 3)
        kategorienViewModel.insertKategorie(kat)
        kat = Kategorie("Kochen", 4)
        kategorienViewModel.insertKategorie(kat)
        kat = Kategorie("Waschen", 5)
        kategorienViewModel.insertKategorie(kat)
        kat = Kategorie("Lampen", 6)
        kategorienViewModel.insertKategorie(kat)
        kat = Kategorie("Sonstiges", 7)
        kategorienViewModel.insertKategorie(kat)
    }

    override fun onClick(v : View) {
        //switch-case in Kotlin: (Zur Unterscheidung der Buttons. Hier eigentlich nicht notwendig)
        when (v.id) {
            R.id.kategorie_button_add -> {
                //neues Fragment erstellen, Beim Klick soll ja auf die Seite zum neu erstellen weitergeleitet werden
                val frag = KategorienNewFragment(iconArray)
                //Fragment Manager aus Main Activity holen
                val fragMan = parentFragmentManager
                //Wichtig: Hier bei R.id. die Fragment View aus dem content_main.xml auswählen! mit dem neuen Fragment ersetzen und dann committen.
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).addToBackStack(null).commit()
            }
            else -> {
                Toast.makeText(v.context, String.format(Locale.GERMAN,"%d was pressed.", v.id), Toast.LENGTH_SHORT).show()
            }
        }
    }
}