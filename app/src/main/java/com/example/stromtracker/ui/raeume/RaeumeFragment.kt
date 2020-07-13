package com.example.stromtracker.ui.raeume

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
import com.example.stromtracker.database.Raum
import com.example.stromtracker.ui.raeume.raeumeErstellen.RaeumeErstellenFragment


//deklariert Raeumefragment als Unterklasse von Fragment
class RaeumeFragment: Fragment() {
    private lateinit var raeumeViewModel: RaeumeViewModel
    private lateinit var datain: ArrayList<Raum>
    private lateinit var viewAdapter : RecyclerView.Adapter<*>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //View Model zuweisen, benötigt für DB Zugriff
        raeumeViewModel = ViewModelProviders.of(this).get(RaeumeViewModel::class.java)
        //TODO: Get Haushalt ID aus Spinner und setze sie in untenstehenden Befehl ein
        raeumeViewModel.getAllRaeumeById(1).observe(
            viewLifecycleOwner,
            Observer { raeume ->
                if (raeume != null) {
                    datain.clear()
                    datain.addAll(raeume)

                    viewAdapter.notifyDataSetChanged()
                }
            }
        )
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_raeume, container, false)//false weil es nur teil des root ist, aber nicht selber die root




        datain=ArrayList()
        //Recyclerview, wo eine Liste aller Raeume angezeigt wird. Alles weitere in ListAdapterraeume:
        val recyclerView = root.findViewById<RecyclerView>(R.id.recycler_view_raeume)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        viewAdapter=ListAdapterraeume(datain)
        recyclerView.adapter = viewAdapter

        //Floating Action Button zum erstellen neuer Raeume
        //Floating actionbutton finden
        val fab: View = root.findViewById(R.id.fab_raeume)
        //Click listener setzen
        fab.setOnClickListener { view ->
            if (view != null) {
                //neues Fragment erstellen auf das weitergeleitet werden soll
                val frag = RaeumeErstellenFragment()
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


