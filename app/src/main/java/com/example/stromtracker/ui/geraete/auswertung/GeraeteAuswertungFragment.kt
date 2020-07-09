package com.example.stromtracker.ui.geraete.auswertung

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.example.stromtracker.R
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.ui.geraete.GeraeteViewModel
import com.google.android.material.navigation.NavigationView


class GeraeteAuswertungFragment : Fragment() {

    private lateinit var root:View
    private lateinit var geraeteViewModel: GeraeteViewModel

    private lateinit var currHaushalt: Haushalt

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_geraete_auswertung, container, false)
        geraeteViewModel = ViewModelProviders.of(this).get(GeraeteViewModel::class.java)

        val navView = requireActivity().findViewById<NavigationView>(R.id.nav_view)

        val sp: Spinner = navView.menu.findItem(R.id.nav_haushalt).actionView as Spinner
        currHaushalt = sp.selectedItem as Haushalt

        val pie = AnyChart.pie()

        val data: MutableList<DataEntry> = ArrayList()
        data.add(ValueDataEntry("John", 10000))
        data.add(ValueDataEntry("Jake", 12000))
        data.add(ValueDataEntry("Peter", 18000))

        pie.data(data)

        val anyChartView = root.findViewById(R.id.any_chart_view) as AnyChartView
        anyChartView.setChart(pie)



        return root
    }

}