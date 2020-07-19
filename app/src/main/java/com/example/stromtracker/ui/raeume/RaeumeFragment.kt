package com.example.stromtracker.ui.raeume

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.R
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.database.Raum
import com.example.stromtracker.ui.SharedViewModel
import com.example.stromtracker.ui.raeume.raeumeErstellen.RaeumeErstellenFragment
import com.google.android.material.navigation.NavigationView


//deklariert Raeumefragment als Unterklasse von Fragment
class RaeumeFragment : Fragment() {
    private lateinit var raeumeViewModel: RaeumeViewModel
    private lateinit var datain: ArrayList<Raum>
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var currHaushalt: Haushalt
    private lateinit var sharedViewModel: SharedViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //Haushalt ID aus Spinner und übergeben sie in für das hohlen Räume die im Haushalt erzeugt sind

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //sharedViewModel um auf das gemeinsame ViewModel zuzugreifen
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        //View Model zuweisen, benötigt für DB Zugriff
        raeumeViewModel = ViewModelProvider(this).get(RaeumeViewModel::class.java)
        val root = inflater.inflate(
            R.layout.fragment_raeume,
            container,
            false
        )//false weil es nur teil des root ist, aber nicht selber die root

        val raumData: LiveData<List<Raum>> =
            Transformations.switchMap(sharedViewModel.getHaushalt()) { haushalt ->
                raeumeViewModel.getAllRaeumeByHaushaltId(haushalt.getHaushaltID())
            }
        raumData.observe(
            viewLifecycleOwner,
            Observer { raeume ->
                if (raeume != null) {
                    datain.clear()
                    datain.addAll(raeume)
                    currHaushalt= sharedViewModel.selectedHaushalt.value!!
                    viewAdapter.notifyDataSetChanged()
                }
            }
        )

        //TODO: Entfernen wenn sicher, dass es funktioniert
        //val navView = requireActivity().findViewById<NavigationView>(R.id.nav_view)
        //val sp: Spinner = navView.menu.findItem(R.id.nav_haushalt).actionView as Spinner
        //currHaushalt = sp.selectedItem as Haushalt

        datain = ArrayList()
        //Recyclerview, wo eine Liste aller Raeume angezeigt wird. Alles weitere in ListAdapterraeume:
        val recyclerView = root.findViewById<RecyclerView>(R.id.recycler_view_raeume)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        viewAdapter = ListAdapterraeume(datain, currHaushalt)
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
                    .addToBackStack(null).commit()
                //und anschließend noch ein commit()

            }

        }
        return root
    }

}
