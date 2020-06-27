package com.example.stromtracker.ui.kategorien

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.R
import com.example.stromtracker.ui.kategorien.new_kategorie.KategorienNewFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.lifecycle.Observer
import java.util.*

class KategorienFragment : Fragment(), View.OnClickListener {

    private lateinit var kategorienViewModel: KategorienViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var buttonAdd: FloatingActionButton
    private lateinit var myDataset : Array<String>
    private lateinit var root:View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //View Model zuweisen, benötigt für DB Zugriff
        kategorienViewModel = ViewModelProviders.of(this).get(KategorienViewModel::class.java)

        //root festlegen -> root ist ConstraintLayout
        root = inflater.inflate(R.layout.fragment_kategorien, container, false)

        //Buttons finden und Click Listener zuweisen
        buttonAdd = root.findViewById(R.id.kategorie_button_add)
        buttonAdd.setOnClickListener(this)    
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        kategorienViewModel.getAllKategorie().observe(
            viewLifecycleOwner,
            Observer { kategorien ->
                if (kategorien != null) {
                    Log.d("TAGkategorien", kategorien.toString())
                    //StringArray mit Kategorien initialisieren
                    myDataset = emptyArray()
                    for(element in kategorien) {
                        myDataset += element.getName()
                    }

                    //RecyclerView mit geholten Daten aus DB initialisieren
                    viewAdapter = KategorienListAdapter(myDataset)
                    viewManager = LinearLayoutManager(this.context)
                    recyclerView = root.findViewById<RecyclerView>(R.id.my_recycler_view).apply {
                        setHasFixedSize(true)
                        layoutManager = viewManager
                        adapter = viewAdapter
                    }
                }

            }
        )

    }

    override fun onClick(v : View) {
        //switch-case in Kotlin: (Zur Unterscheidung der Buttons. Hier eigentlich nicht notwendig)
        when (v.id) {
            R.id.kategorie_button_add -> {
                //neues Fragment erstellen, Beim Klick soll ja auf die Seite zum neu erstellen weitergeleitet werden
                val frag = KategorienNewFragment()
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