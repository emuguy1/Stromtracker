package com.example.stromtracker.ui.geraete

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import com.example.stromtracker.ui.geraete.geraet_new.GeraeteNewProduzentFragment
import com.example.stromtracker.ui.geraete.geraet_new.GeraeteNewVerbraucherFragment
import com.getbase.floatingactionbutton.FloatingActionButton


class GeraeteFragment : Fragment(), View.OnClickListener {

    private lateinit var geraeteViewModel: GeraeteViewModel
    private  lateinit var geraeteList:ArrayList<Geraete>
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var buttonAddVerbraucher: FloatingActionButton
    private lateinit var buttonAddProduzent: FloatingActionButton
    private lateinit var root:View
    private lateinit var kategorieList:ArrayList<Kategorie>
    private lateinit var raumList:ArrayList<Raum>
    private lateinit var buttonSortVerbrauch: Button
    private lateinit var buttonSortRaum: Button
    private lateinit var buttonSortName: Button






    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_geraete, container, false)
        buttonAddVerbraucher = root.findViewById(R.id.button_geraete_add_verbraucher)
        buttonAddVerbraucher.setOnClickListener(this)
        buttonAddProduzent = root.findViewById(R.id.button_geraete_add_produzent)
        buttonAddProduzent.setOnClickListener(this)

        buttonSortVerbrauch = root.findViewById(R.id.geraete_button_sort_verbrauch)
        buttonSortVerbrauch.setOnClickListener(this)
        buttonSortRaum = root.findViewById(R.id.geraete_button_sort_raum)
        buttonSortRaum.setOnClickListener(this)
        buttonSortName = root.findViewById(R.id.geraete_button_sort_name)
        buttonSortName.setOnClickListener(this)

        geraeteList = ArrayList()
        kategorieList = ArrayList()
        raumList = ArrayList()


        viewManager = LinearLayoutManager(this.context)
        viewAdapter = GeraeteListAdapter(geraeteList, kategorieList, raumList)
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

        geraeteViewModel.getAllVerbraucher().observe(
            viewLifecycleOwner,
            Observer { geraete ->
                if (geraete != null) {
                    geraeteList.clear()
                    geraeteList.addAll(geraete)
                    viewAdapter.notifyDataSetChanged();

                }
            })

        geraeteViewModel.getAllRaeume().observe(
            viewLifecycleOwner,
            Observer { raeume ->
                if (raeume != null) {
                    raumList.clear()
                    raumList.addAll(raeume)
                    if(raeume.isEmpty()) {
                        //nur zum testen
                        geraeteViewModel.insertHaushalt(Haushalt("name", 0.0, 1, 0.0, null, false))
                        geraeteViewModel.insertRaum(Raum("test", 1))
                        geraeteViewModel.insertRaum(Raum("zet", 1))
                        geraeteViewModel.insertKategorie(Kategorie("test", null))


                    }

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
            R.id.button_geraete_add_verbraucher -> {
                //TODO: Zwischen Haushalten unterscheiden!
                val frag = GeraeteNewVerbraucherFragment(kategorieList, raumList)
                val fragMan = parentFragmentManager
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).addToBackStack(null).commit()
            }
            R.id.button_geraete_add_produzent -> {
                val frag = GeraeteNewProduzentFragment(kategorieList, raumList)
                val fragMan = parentFragmentManager
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).addToBackStack(null).commit()
            }
            R.id.geraete_button_sort_verbrauch -> {
                var sortedVerbrauch = geraeteList.sortedWith(compareByDescending {it.getJahresverbrauch()})
                geraeteList.clear()
                geraeteList.addAll(sortedVerbrauch)
                viewAdapter.notifyDataSetChanged();
                buttonSortVerbrauch.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                buttonSortName.paintFlags = 0
                buttonSortRaum.paintFlags = 0

            }

            R.id.geraete_button_sort_name -> {
                var sortedName = geraeteList.sortedWith(compareBy{it.getName().toLowerCase()})
                geraeteList.clear()
                geraeteList.addAll(sortedName)
                viewAdapter.notifyDataSetChanged();
                buttonSortName.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                buttonSortVerbrauch.paintFlags = 0
                buttonSortRaum.paintFlags = 0
            }

            R.id.geraete_button_sort_raum -> {
                var sortedRaum = geraeteList.sortedWith(compareBy{raumList[it.getRaumID() - 1].getName().toLowerCase()})
                Log.d("TAGSort", sortedRaum.toString())
                geraeteList.clear()
                geraeteList.addAll(sortedRaum)
                viewAdapter.notifyDataSetChanged();
                buttonSortRaum.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                buttonSortVerbrauch.paintFlags = 0
                buttonSortName.paintFlags = 0

            }
            else -> {
                //Toast.makeText(v.context, String.format(Locale.GERMAN,"%d was pressed.", v.id), Toast.LENGTH_SHORT).show()
            }
        }
    }


    }