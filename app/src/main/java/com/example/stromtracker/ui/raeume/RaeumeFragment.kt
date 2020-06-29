package com.example.stromtracker.ui.raeume

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.R
import com.example.stromtracker.ui.raeume.raeumeErstellen.RaeumeErstellenFragment


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


