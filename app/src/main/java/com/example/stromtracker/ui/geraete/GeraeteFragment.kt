package com.example.stromtracker.ui.geraete

import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.MainActivity
import com.example.stromtracker.R
import com.example.stromtracker.database.Geraete
import com.example.stromtracker.database.Kategorie
import com.example.stromtracker.database.Raum
import com.example.stromtracker.ui.SharedViewModel
import com.example.stromtracker.ui.geraete.auswertung.GeraeteAuswertungFragment
import com.example.stromtracker.ui.geraete.geraet_new.GeraeteNewProduzentFragment
import com.example.stromtracker.ui.geraete.geraet_new.GeraeteNewVerbraucherFragment
import com.getbase.floatingactionbutton.FloatingActionButton


class GeraeteFragment : Fragment(), View.OnClickListener {

    private lateinit var geraeteViewModel: GeraeteViewModel
    private lateinit var verbraucherList: ArrayList<Geraete>
    private lateinit var produzentList: ArrayList<Geraete>


    private lateinit var recyclerView: RecyclerView
    private lateinit var produzentRecyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var produzentViewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var produzentViewManager: RecyclerView.LayoutManager

    private lateinit var buttonAddVerbraucher: FloatingActionButton
    private lateinit var buttonAddProduzent: FloatingActionButton
    private lateinit var root: View
    private lateinit var kategorieList: ArrayList<Kategorie>

    private lateinit var raumListHaushalt: ArrayList<Raum>
    private lateinit var buttonSortVerbrauch: Button
    private lateinit var buttonSortRaum: Button
    private lateinit var buttonSortName: Button
    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var buttonSortProduktion_prod: Button
    private lateinit var buttonSortRaum_prod: Button
    private lateinit var buttonSortName_prod: Button

    private lateinit var iconArray: Array<Int>

    private lateinit var buttonZuAuswertung: Button


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

        buttonSortProduktion_prod = root.findViewById(R.id.geraete_button_sort_produktion_prod)
        buttonSortProduktion_prod.setOnClickListener(this)
        buttonSortRaum_prod = root.findViewById(R.id.geraete_button_sort_raum_prd)
        buttonSortRaum_prod.setOnClickListener(this)
        buttonSortName_prod = root.findViewById(R.id.geraete_button_sort_name_prod)
        buttonSortName_prod.setOnClickListener(this)

        buttonZuAuswertung = root.findViewById(R.id.geraete_button_auswertung)
        buttonZuAuswertung.setOnClickListener(this)

        verbraucherList = ArrayList()
        produzentList = ArrayList()
        kategorieList = ArrayList()
        raumListHaushalt = ArrayList()

        var mainAct = requireActivity() as MainActivity
        iconArray = mainAct.getIconArray()



        viewManager = LinearLayoutManager(this.context)

        viewAdapter =
            GeraeteListAdapter(verbraucherList, kategorieList, raumListHaushalt, iconArray)

        recyclerView = root.findViewById<RecyclerView>(R.id.geraete_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        produzentViewManager = LinearLayoutManager(this.context)
        produzentViewAdapter =
            GeraeteListAdapter(produzentList, kategorieList, raumListHaushalt, iconArray)
        produzentRecyclerView =
            root.findViewById<RecyclerView>(R.id.produzent_recycler_view).apply {
                setHasFixedSize(true)
                layoutManager = produzentViewManager
                adapter = produzentViewAdapter
            }
        return root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        geraeteViewModel = ViewModelProvider(this).get(GeraeteViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)


        val verbraucherData: LiveData<List<Geraete>> =
            Transformations.switchMap(sharedViewModel.getHaushalt()) { haushalt ->
                geraeteViewModel.getAllVerbraucherByHaushaltID(haushalt.getHaushaltID())
            }
        verbraucherData.observe(
            viewLifecycleOwner,
            Observer { geraete ->
                if (geraete != null) {

                    verbraucherList.clear()
                    verbraucherList.addAll(geraete)
                    viewAdapter.notifyDataSetChanged();
                }
            })

        val raumDataHaushalt: LiveData<List<Raum>> =
            Transformations.switchMap(sharedViewModel.getHaushalt()) { haushalt ->
                geraeteViewModel.getAllRaumByHaushaltID(haushalt.getHaushaltID())
            }
        raumDataHaushalt.observe(
            viewLifecycleOwner,
            Observer { raum ->
                if (raum != null) {
                    raumListHaushalt.clear()
                    raumListHaushalt.addAll(raum)
                    viewAdapter.notifyDataSetChanged()

                }
            })

        val produzentData: LiveData<List<Geraete>> =
            Transformations.switchMap(sharedViewModel.getHaushalt()) { haushalt ->
                geraeteViewModel.getAllProduzentenByHaushaltID(haushalt.getHaushaltID())
            }
        produzentData.observe(
            viewLifecycleOwner,
            Observer { produzenten ->
                if (produzenten != null) {
                    produzentList.clear()
                    produzentList.addAll(produzenten)
                    produzentViewAdapter.notifyDataSetChanged()
                }
            }
        )
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

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_geraete_add_verbraucher -> {
                val frag = GeraeteNewVerbraucherFragment(kategorieList, raumListHaushalt)
                val fragMan = parentFragmentManager
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                    .addToBackStack(null).commit()
            }
            R.id.button_geraete_add_produzent -> {
                val frag = GeraeteNewProduzentFragment(kategorieList, raumListHaushalt)
                val fragMan = parentFragmentManager
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                    .addToBackStack(null).commit()
            }
            R.id.geraete_button_sort_verbrauch -> {
                var sortedVerbrauch =
                    verbraucherList.sortedWith(compareByDescending { it.getJahresverbrauch() })
                verbraucherList.clear()
                verbraucherList.addAll(sortedVerbrauch)
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
                var sortedName =
                    verbraucherList.sortedWith(compareBy { it.getName().toLowerCase() })
                verbraucherList.clear()
                verbraucherList.addAll(sortedName)
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
                var sortedRaum = verbraucherList.sortedWith(compareBy { it.getRaumID() })


                verbraucherList.clear()
                verbraucherList.addAll(sortedRaum)

                viewAdapter.notifyDataSetChanged();
                buttonSortRaum.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                buttonSortVerbrauch.paintFlags = 0
                buttonSortName.paintFlags = 0
                buttonSortRaum.typeface = Typeface.DEFAULT_BOLD
                buttonSortName.typeface = Typeface.DEFAULT_BOLD
                buttonSortVerbrauch.typeface = Typeface.DEFAULT_BOLD
            }

            R.id.geraete_button_sort_produktion_prod -> {

                val sortedProduzent =
                    produzentList.sortedWith(compareBy { it.getJahresverbrauch() })
                produzentList.clear()
                produzentList.addAll(sortedProduzent)
                produzentViewAdapter.notifyDataSetChanged()

                buttonSortProduktion_prod.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                buttonSortName_prod.paintFlags = 0
                buttonSortRaum_prod.paintFlags = 0
                buttonSortRaum_prod.typeface = Typeface.DEFAULT_BOLD
                buttonSortProduktion_prod.typeface = Typeface.DEFAULT_BOLD
                buttonSortName_prod.typeface = Typeface.DEFAULT_BOLD

            }

            R.id.geraete_button_sort_name_prod -> {
                val sortedName = produzentList.sortedWith(compareBy { it.getName().toLowerCase() })
                produzentList.clear()
                produzentList.addAll(sortedName)
                produzentViewAdapter.notifyDataSetChanged();
                buttonSortName_prod.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                buttonSortProduktion_prod.paintFlags = 0
                buttonSortRaum_prod.paintFlags = 0
                buttonSortRaum_prod.typeface = Typeface.DEFAULT_BOLD
                buttonSortProduktion_prod.typeface = Typeface.DEFAULT_BOLD
                buttonSortName_prod.typeface = Typeface.DEFAULT_BOLD
            }

            R.id.geraete_button_sort_raum_prd -> {
                /*val sortedRaum = produzentList.sortedWith(compareBy {
                    produzentList[it.getRaumID() - 1].getName().toLowerCase()
                })
                */
                /*produzentList.clear()
                produzentList.addAll(sortedRaum)

                 */
                produzentViewAdapter.notifyDataSetChanged();
                buttonSortRaum_prod.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                buttonSortProduktion_prod.paintFlags = 0
                buttonSortName_prod.paintFlags = 0
                buttonSortRaum_prod.typeface = Typeface.DEFAULT_BOLD
                buttonSortProduktion_prod.typeface = Typeface.DEFAULT_BOLD
                buttonSortName_prod.typeface = Typeface.DEFAULT_BOLD

            }

            R.id.geraete_button_auswertung -> {
                val frag = GeraeteAuswertungFragment(
                    verbraucherList,
                    produzentList,
                    kategorieList,
                    raumListHaushalt
                )
                val fragMan = parentFragmentManager
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                    .addToBackStack(null).commit()
            }

            else -> {
            }
        }
    }
}
