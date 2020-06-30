package com.example.stromtracker.ui.geraete

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
import com.example.stromtracker.database.Geraete
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.database.Kategorie
import com.example.stromtracker.database.Raum
import com.example.stromtracker.ui.geraete.geraet_new.GeraeteNewFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.ArrayList


class GeraeteFragment : Fragment(), View.OnClickListener {

    private lateinit var geraeteViewModel: GeraeteViewModel
    private  lateinit var geraeteList:ArrayList<Geraete>
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var buttonAdd:FloatingActionButton
    private lateinit var root:View
    private lateinit var kategorieList:ArrayList<Kategorie>
    private lateinit var raumList:ArrayList<Raum>






    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_geraete, container, false)
        buttonAdd = root.findViewById(R.id.button_geraete_add)
        buttonAdd.setOnClickListener(this)
        geraeteList = ArrayList()
        kategorieList = ArrayList()
        raumList = ArrayList()

        viewManager = LinearLayoutManager(this.context)
        viewAdapter = GeraeteListAdapter(geraeteList)
        recyclerView = root.findViewById<RecyclerView>(R.id.geraete_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }



        return root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        geraeteViewModel = ViewModelProviders.of(this).get(GeraeteViewModel::class.java)

        geraeteViewModel.getAllGeraete().observe(
            viewLifecycleOwner,
            Observer { geraete ->
                if (geraete != null) {
                    geraeteList.clear()
                    geraeteList.addAll(geraete)
                    viewAdapter.notifyDataSetChanged();

                    Log.d("TAGGeraete", geraete.toString())

                    if (geraete.isEmpty()) {
                        geraeteViewModel.insertHaushalt(Haushalt("name", 0.0, 1, 0.0, null, false))
                        geraeteViewModel.insertKategorie(Kategorie("test", null))
                        geraeteViewModel.insertRaum(Raum("test", 1))
                    }


                }
            })

        geraeteViewModel.getAllRaeume().observe(
            viewLifecycleOwner,
            Observer { raeume ->
                if (raeume != null) {
                    raumList.clear()
                    raumList.addAll(raeume)

                }
            })

        geraeteViewModel.getAllKategorie().observe(
            viewLifecycleOwner,
            Observer { kategorie ->
                if (kategorie != null) {
                    kategorieList.clear()
                    kategorieList.addAll(kategorie)

                }
            })






    }

    override fun onClick(v : View) {
        when(v.id) {
            R.id.button_geraete_add -> {
                //TODO: Zwischen Haushalten unterscheiden!

                val frag = GeraeteNewFragment(kategorieList, raumList)
                val fragMan = parentFragmentManager
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).addToBackStack(null).commit()

            }

            else -> {
                //Toast.makeText(v.context, String.format(Locale.GERMAN,"%d was pressed.", v.id), Toast.LENGTH_SHORT).show()
            }
        }
    }


    }