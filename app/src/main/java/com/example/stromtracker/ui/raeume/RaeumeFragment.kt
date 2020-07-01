package com.example.stromtracker.ui.raeume

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.R
import com.example.stromtracker.ui.raeume.raeumeErstellen.RaeumeErstellenFragment
import com.google.android.material.navigation.NavigationView


//deklariert Raeumefragment als Unterklasse von Fragment
class RaeumeFragment: Fragment() {
    private lateinit var raeumeViewModel: RaeumeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        raeumeViewModel =
            ViewModelProviders.of(this).get(RaeumeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_raeume, container, false)//false weil es nur teil des root ist, aber nicht selber die root


        //Akutell gewählten Haushalt ermitteln und bei Wechsel des Haushaltes im selben Fragment anpassen
        val navView = requireActivity().findViewById<NavigationView>(R.id.nav_view)
        val sp: Spinner = navView.menu.findItem(R.id.nav_haushalt).getActionView() as Spinner
        var selected = sp.selectedItem
        Log.d("RäumeNav3", selected.toString() + selected.javaClass)

        sp.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long) {
                //Wenn man im RäumeFramgent (MenuPos=3) ist, soll bei einem Wechsel des Haushaltes der akutelle Haushalt angepasst / ausgegeben werden
                if(navView.menu.getItem(3).isChecked) {
                    selected = parentView!!.getItemAtPosition(position)
                    Log.d("RäumeSelectedHaushalt", selected.toString())
                }
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Nothing
            }
        })


        //Recyclerview, wo eine Liste aller Raeume angezeigt wird. Alles weitere in ListAdapterraeume:
        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerViewRaeume)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = ListAdapterraeume()

        //Floating Action Button zum erstellen neuer Raeume
        //Floating actionbutton finden
        val fab: View = root.findViewById(R.id.fab)
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


