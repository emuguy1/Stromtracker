package com.example.stromtracker.ui.haushalt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.MainActivity
import com.example.stromtracker.R
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.database.Raum
import com.example.stromtracker.ui.SharedViewModel
import com.example.stromtracker.ui.haushalt.haushaltErstellen.HaushaltErstellenFragment

private const val defaultName = "Haushalt1"
private const val defaultStromkosten = 26.5
private const val defaultPersonen = 1
private val defaultEntry: Haushalt = Haushalt(
    defaultName,
    defaultStromkosten,
    defaultPersonen,
    null,
    null,
    false
)

// deklariert Haushaltfragment als Unterklasse von Fragment
class HaushaltFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var datain: ArrayList<Haushalt>
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var datatemp: ArrayList<Haushalt>
    private lateinit var mainact: MainActivity
    private var isinit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        sharedViewModel.getAllHaushalt().observe(
            this,
            Observer { haushalte ->
                if (haushalte != null) {
                    if (haushalte.isEmpty()) {
                        //Zur Testbarkeit werden erstmal ein paar Einträge erzeugt
                        initHaushalt()
                        isinit = true
                    }
                    //die alte Haushaltliste aus Main Activity holen, um zu schauen,
                    // welcher Haushalt erzeugt wurde und dem dann Standardräume hinzuzufügen
                    datatemp = getOldHaushaltList()

                    datain.clear()
                    datain.addAll(haushalte)
                    //Die Haushaltliste in Main Activity erneuern.
                    setOldHaushaltList(datain)
                    //Standardräume erstellern, sobald ein neuer Haushalt erzeugt wird.
                    if (datain.size > datatemp.size && !isinit && datatemp.size > 0) {
                        initRaeume(datain[datain.size - 1].getHaushaltID())
                    }
                    // Neue Räume erzeugen, anchdem ein Brnad neuer Erzeugt wurde.
                    if (isinit && datain.size > 0) {
                        // Sobald neues Haushalt erstellt wurde,
                        // sollen ein paar Standardräume erzeugt werden.
                        // Hier nach leer initialisierung und sobald das ganze in die Datenbank
                        // gekommen ist,deswegen wird hier gewartet,
                        // bis datain bei einem element ist,
                        // bevor es in die if Schleife kommt
                        initRaeume(datain[datain.size - 1].getHaushaltID())
                        isinit = false
                    }
                    viewAdapter.notifyDataSetChanged()
                }
            }
        )
    }

    private fun initHaushalt() {
        sharedViewModel.insertHaushalt(defaultEntry)
    }

    private fun initRaeume(hausid: Int) {
        sharedViewModel.insertRaum(Raum("Sonstiges", hausid))
        sharedViewModel.insertRaum(Raum("Wohnzimmer", hausid))
        sharedViewModel.insertRaum(Raum("Schlafzimmer", hausid))
        sharedViewModel.insertRaum(Raum("Küche", hausid))
        sharedViewModel.insertRaum(Raum("Badezimmer", hausid))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(
            R.layout.fragment_haushalt,
            container,
            false
        ) // false weil es nur teil des root ist, aber nicht selber die root

        // Initalisieren der Datenliste und des ViewAdapters
        datain = ArrayList()
        viewAdapter = ListAdapterHaushalt(datain)

        // Recyclerview, wo eine Liste aller Haushalte angezeigt wird.
        // Alles weitere wird in ListAdapterHaushalt gesteuert
        val recyclerView = root.findViewById<RecyclerView>(R.id.recycler_view_haushalt)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = viewAdapter

        // Floating Action Button zum erstellen neuer Haushalte
        // Floating actionbutton finden
        val fab: View = root.findViewById(R.id.fab_haushalt)
        // Click listener setzen
        fab.setOnClickListener { view ->
            if (view != null) {
                // neues Fragment erstellen auf das weitergeleitet werden soll
                val frag = HaushaltErstellenFragment()
                // Fragment Manager aus Main Activity holen
                val fragMan = parentFragmentManager
                // Ftagment container aus content_main.xml muss ausgeählt werden,
                // dann mit neuen Fragment ersetzen, dass oben erstellt wurde
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                    .addToBackStack(null).commit()
                // und anschließend noch ein commit()
            }
        }
        return root
    }

    companion object {
        private lateinit var oldHaushaltList: ArrayList<Haushalt>

        fun setOldHaushaltList(h: ArrayList<Haushalt>) {
            oldHaushaltList = h
        }

        fun getOldHaushaltList(): ArrayList<Haushalt> {
            return oldHaushaltList
        }
    }
}
