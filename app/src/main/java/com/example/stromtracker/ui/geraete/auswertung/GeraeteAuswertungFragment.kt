package com.example.stromtracker.ui.geraete.auswertung

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.Align
import com.anychart.enums.LegendLayout
import com.example.stromtracker.R
import com.example.stromtracker.database.Geraete
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.ui.geraete.GeraeteFragment
import com.example.stromtracker.ui.geraete.GeraeteViewModel
import com.google.android.material.navigation.NavigationView


class GeraeteAuswertungFragment : Fragment() , View.OnClickListener{

    private lateinit var root:View
    private lateinit var geraeteViewModel: GeraeteViewModel

    private lateinit var currHaushalt: Haushalt
    private  lateinit var geraeteList:ArrayList<Geraete>

    private lateinit var anyChartView: AnyChartView
    private lateinit var btnBack : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_geraete_auswertung, container, false)

        val navView = requireActivity().findViewById<NavigationView>(R.id.nav_view)
        val sp: Spinner = navView.menu.findItem(R.id.nav_haushalt).actionView as Spinner
        currHaushalt = sp.selectedItem as Haushalt

        geraeteList = ArrayList()

        anyChartView = root.findViewById(R.id.any_chart_view) as AnyChartView
        btnBack = root.findViewById(R.id.geraete_auswertung_button_back)
        btnBack.setOnClickListener(this)


        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        geraeteViewModel = ViewModelProviders.of(this).get(GeraeteViewModel::class.java)

        //Alle Geräte des aktuellen Haushalts aus DB holen
        geraeteViewModel.getAllGeraeteByHaushaltID(currHaushalt.getHaushaltID()).observe(
            viewLifecycleOwner,
            Observer { geraete ->
                if (geraete != null) {
                    geraeteList.clear()
                    geraeteList.addAll(geraete)
                    reloadVerbrauchsChart()
                }
            })
    }

    fun reloadVerbrauchsChart() {
        //Geräte nach Verbrauchern filtern
        val verbrauchsList = ArrayList<Geraete>()
        for (geraet in geraeteList) {
            if(geraet.getJahresverbrauch() > 0)
                verbrauchsList.add(geraet)
        }

        val pie = AnyChart.pie()

        val data: MutableList<DataEntry> = ArrayList()
        for (geraet in verbrauchsList)
            data.add(ValueDataEntry(geraet.getName(), geraet.getJahresverbrauch()))

        pie.data(data)
        pie.title("Gesamtverbrauch")
        pie.legend().title().enabled(true)
        pie.legend().title()
            .text("Verbraucher")
            .padding(0.0, 0.0, 10.0, 0.0)

        pie.legend()
            .position("center-bottom")
            .itemsLayout(LegendLayout.HORIZONTAL)
            .align(Align.CENTER)
        anyChartView.setChart(pie)
    }

    override fun onClick(v: View) {
        when (v.id){
            R.id.geraete_auswertung_button_back -> {
                val frag = GeraeteFragment()
                val fragMan = parentFragmentManager
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).addToBackStack(null).commit()
            }
        }
    }

}