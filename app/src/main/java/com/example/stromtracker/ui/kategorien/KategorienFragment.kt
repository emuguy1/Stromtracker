package com.example.stromtracker.ui.kategorien

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.MainActivity
import com.example.stromtracker.R
import com.example.stromtracker.database.Kategorie
import com.example.stromtracker.ui.kategorien.new_kategorie.KategorienNewFragment
import com.getbase.floatingactionbutton.AddFloatingActionButton
import java.util.*

import kotlin.collections.ArrayList

class KategorienFragment : Fragment(), View.OnClickListener {

    private lateinit var kategorienViewModel: KategorienViewModel
    private lateinit var myKategorien: ArrayList<Kategorie>
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var buttonAdd: AddFloatingActionButton
    private lateinit var root: View
    private lateinit var iconArray: Array<Int>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // IconArray initialisieren
        val mainAct = requireActivity() as MainActivity
        iconArray = mainAct.getIconArray()

        // root festlegen -> root ist ConstraintLayout
        root = inflater.inflate(R.layout.fragment_kategorien, container, false)
        myKategorien = ArrayList()

        // RecyclerView mit geholten Daten aus DB initialisieren
        viewAdapter = KategorienListAdapter(myKategorien, iconArray)

        viewManager = LinearLayoutManager(this.context)
        recyclerView = root.findViewById<RecyclerView>(R.id.kategorien_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        // Buttons finden und Click Listener zuweisen
        buttonAdd = root.findViewById(R.id.kategorie_button_add)
        buttonAdd.setOnClickListener(this)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // View Model zuweisen, benötigt für DB Zugriff
        kategorienViewModel = ViewModelProvider(this).get(KategorienViewModel::class.java)
        kategorienViewModel.getAllKategorie().observe(
            viewLifecycleOwner,
            Observer { kategorien ->
                if (kategorien != null) {
                    myKategorien.clear()

                    // Kategorien alphabetisch sortieren.
                    // Geht nicht in DB, da dort kein toLower Case angewendet wird
                    // -> klein geschriebene Kategorien würden unter groß geschriebenen stehen
                    myKategorien.addAll(kategorien.sortedWith(compareBy({
                        it.getName().toLowerCase(Locale.ROOT)
                    }, { it.getKategorieID() })))

                    viewAdapter.notifyDataSetChanged()
                }
            }
        )
    }

    override fun onClick(v: View) {
        // switch-case in Kotlin: (Zur Unterscheidung der Buttons. Hier eigentlich nicht notwendig)
        when (v.id) {
            R.id.kategorie_button_add -> {
                // neues Fragment erstellen,
                // Beim Klick soll ja auf die Seite zum neu erstellen weitergeleitet werden
                val frag = KategorienNewFragment(iconArray)
                // Fragment Manager aus Main Activity holen
                val fragMan = parentFragmentManager
                // Wichtig: Hier bei R.id. die Fragment View aus dem content_main.xml auswählen!
                // mit dem neuen Fragment ersetzen und dann committen.
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                    .addToBackStack(null).commit()
            }
            else -> {
                Toast.makeText(
                    v.context,
                    String.format(Locale.GERMAN, "%d was pressed.", v.id),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
