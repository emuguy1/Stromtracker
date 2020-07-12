package com.example.stromtracker.ui.haushalt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.R
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.ui.haushalt.haushaltErstellen.HaushaltErstellenFragment

//deklariert Haushaltfragment als Unterklasse von Fragment
class HaushaltFragment: Fragment() {
    private lateinit var haushaltViewModel: HaushaltViewModel
    private lateinit var datain: ArrayList<Haushalt>
    private lateinit var viewAdapter : RecyclerView.Adapter<*>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //View Model zuweisen, benötigt für DB Zugriff
        haushaltViewModel = ViewModelProviders.of(this).get(HaushaltViewModel::class.java)
        haushaltViewModel.getAllHaushalt().observe(
            viewLifecycleOwner,
            Observer { haushalte ->
                if (haushalte != null) {
                    if(haushalte.isEmpty()) {
                        //Zur Testbarkeit werden erstmal ein paar Einträge erzeugt
                        initHaushalt()
                    }
                    datain.clear()
                    datain.addAll(haushalte)
                    viewAdapter.notifyDataSetChanged()
                }
            }
        )
    }
    private fun initHaushalt () {
        var haushalt : Haushalt = Haushalt("Haushalt 1",12.5, 5, null, null, true )
        haushaltViewModel.insertHaushalt(haushalt)
        haushalt = Haushalt("Haushalt 2",12.5, 1, null, null, false )
        haushaltViewModel.insertHaushalt(haushalt)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        haushaltViewModel =
            ViewModelProviders.of(this).get(HaushaltViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_haushalt, container, false)//false weil es nur teil des root ist, aber nicht selber die root

        //Initalisieren der Datenliste und des ViewAdapters
        datain =ArrayList()
        viewAdapter=ListAdapterHaushalt(datain)

        //Recyclerview, wo eine Liste aller Haushalte angezeigt wird. Alles weitere wird in ListAdapterHaushalt gesteuert
        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerViewHaushalt)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = viewAdapter

        //Floating Action Button zum erstellen neuer Haushalte
        //Floating actionbutton finden
        val fab: View = root.findViewById(R.id.haushalt_fab)
        //Click listener setzen
        fab.setOnClickListener { view ->
            if (view != null) {
                //neues Fragment erstellen auf das weitergeleitet werden soll
                val frag = HaushaltErstellenFragment()
                //Fragment Manager aus Main Activity holen
                val fragMan = parentFragmentManager
                //Ftagment container aus content_main.xml muss ausgeählt werden, dann mit neuen Fragment ersetzen, dass oben erstellt wurde
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                .addToBackStack(null).commit();
                //und anschließend noch ein commit()
            }
        }
        return root
    }
}
