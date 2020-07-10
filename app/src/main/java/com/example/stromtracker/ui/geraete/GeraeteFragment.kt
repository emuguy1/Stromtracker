package com.example.stromtracker.ui.geraete

import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.MainActivity
import com.example.stromtracker.R
import com.example.stromtracker.database.Geraete
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.database.Kategorie
import com.example.stromtracker.database.Raum
import com.example.stromtracker.ui.SharedViewModel
import com.example.stromtracker.ui.geraete.geraet_new.GeraeteNewProduzentFragment
import com.example.stromtracker.ui.geraete.geraet_new.GeraeteNewVerbraucherFragment
import com.getbase.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView


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
    private lateinit var raumListHaushalt:ArrayList<Raum>
    private lateinit var buttonSortVerbrauch: Button
    private lateinit var buttonSortRaum: Button
    private lateinit var buttonSortName: Button
    private lateinit var sharedViewModel:SharedViewModel





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
        raumListHaushalt = ArrayList()

       /* val sp: Spinner = navView.menu.findItem(R.id.nav_haushalt).actionView as Spinner
        currHaushalt = sp.selectedItem as Haushalt

        */



        viewManager = LinearLayoutManager(this.context)
        viewAdapter = GeraeteListAdapter(geraeteList, kategorieList, raumListHaushalt)
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
        sharedViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)


        val geraeteData: LiveData<List<Geraete>> = Transformations.switchMap(sharedViewModel.getHaushalt()) { haushalt -> geraeteViewModel.getAllVerbraucherByHaushaltID(haushalt.getHaushaltID())}
        geraeteData.observe(
                viewLifecycleOwner,
        Observer { geraete ->
            if (geraete != null) {
                geraeteList.clear()
                geraeteList.addAll(geraete)
                viewAdapter.notifyDataSetChanged();


            }
        })


        val raumDataHaushalt: LiveData<List<Raum>> = Transformations.switchMap(sharedViewModel.getHaushalt()) { haushalt -> (geraeteViewModel.getAllRaumByHaushaltID(haushalt.getHaushaltID()))}
        raumDataHaushalt.observe(
            viewLifecycleOwner,
            Observer { raum ->
                if (raum != null) {
                    raumListHaushalt.clear()
                    raumListHaushalt.addAll(raum)
                    viewAdapter.notifyDataSetChanged();


                    if(raum.isEmpty()) {
                        geraeteViewModel.insertRaum(Raum("test", 1))
                        geraeteViewModel.insertRaum(Raum("zet", 1))
                        geraeteViewModel.insertRaum(Raum("tret", 2))


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
                    viewAdapter.notifyDataSetChanged();




                }
            })






    }

    override fun onClick(v : View) {
        when(v.id) {
            R.id.button_geraete_add_verbraucher -> {
                val frag = GeraeteNewVerbraucherFragment(kategorieList, raumListHaushalt)
                val fragMan = parentFragmentManager
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).addToBackStack(null).commit()
            }
            R.id.button_geraete_add_produzent -> {
                val frag = GeraeteNewProduzentFragment(kategorieList, raumListHaushalt)
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
                //TODO schönere Lösung?
                buttonSortRaum.typeface = Typeface.DEFAULT_BOLD
                buttonSortName.typeface = Typeface.DEFAULT_BOLD
                buttonSortVerbrauch.typeface = Typeface.DEFAULT_BOLD


            }

            R.id.geraete_button_sort_name -> {
                var sortedName = geraeteList.sortedWith(compareBy{it.getName().toLowerCase()})
                geraeteList.clear()
                geraeteList.addAll(sortedName)
                viewAdapter.notifyDataSetChanged();
                buttonSortName.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                buttonSortVerbrauch.paintFlags = 0
                buttonSortRaum.paintFlags = 0
                buttonSortRaum.typeface = Typeface.DEFAULT_BOLD
                buttonSortName.typeface = Typeface.DEFAULT_BOLD
                buttonSortVerbrauch.typeface = Typeface.DEFAULT_BOLD
            }

            R.id.geraete_button_sort_raum -> {
                //TODO sortieren über Name? Problem: Gerät zu dem jeweiligen Raum matchen, eventuell for Schleife
                var sortedRaum = geraeteList.sortedWith(compareBy{it.getRaumID()})
                geraeteList.clear()
                geraeteList.addAll(sortedRaum)
                viewAdapter.notifyDataSetChanged();
                buttonSortRaum.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                buttonSortVerbrauch.paintFlags = 0
                buttonSortName.paintFlags = 0
                buttonSortRaum.typeface = Typeface.DEFAULT_BOLD
                buttonSortName.typeface = Typeface.DEFAULT_BOLD
                buttonSortVerbrauch.typeface = Typeface.DEFAULT_BOLD

            }
            else -> {
                //Toast.makeText(v.context, String.format(Locale.GERMAN,"%d was pressed.", v.id), Toast.LENGTH_SHORT).show()
            }
        }
    }


    }