package com.example.stromtracker.ui.geraete.auswertung

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.anychart.enums.Align
import com.anychart.enums.LegendLayout
import com.example.stromtracker.R
import com.example.stromtracker.database.Geraete
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.database.Kategorie
import com.example.stromtracker.database.Raum
import com.example.stromtracker.ui.geraete.GeraeteFragment
import com.example.stromtracker.ui.geraete.GeraeteViewModel
import com.google.android.material.navigation.NavigationView


class GeraeteAuswertungFragment (private val verbraucherList : ArrayList<Geraete>, private val produzentList : ArrayList<Geraete>, private val kategorieList: ArrayList<Kategorie>, private val raumList: ArrayList<Raum>)
    : Fragment() , View.OnClickListener{

    private lateinit var root:View
    private lateinit var geraeteViewModel: GeraeteViewModel

    private lateinit var currHaushalt: Haushalt

    private lateinit var anyChartVerbraucher: AnyChartView
    private lateinit var anyChartProduzent : AnyChartView
    private lateinit var anyChartKategorie : AnyChartView
    private lateinit var anyChartRaum : AnyChartView
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

        anyChartVerbraucher = root.findViewById(R.id.any_chart_verbraucher)
        anyChartProduzent = root.findViewById(R.id.any_chart_produzent)
        anyChartKategorie = root.findViewById(R.id.any_chart_kategorie)
        anyChartRaum = root.findViewById(R.id.any_chart_raum)

        reloadVerbrauchsChart()
        reloadProduzentChart()
        reloadKategorieChart()
        reloadRaumChart()

        btnBack = root.findViewById(R.id.geraete_auswertung_button_back)
        btnBack.setOnClickListener(this)


        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        geraeteViewModel = ViewModelProviders.of(this).get(GeraeteViewModel::class.java)
    }

    fun initPieChart(pie : Pie) : Pie{

        pie.legend().title().enabled(true)
        pie.legend().title().padding(0.0, 0.0, 10.0, 0.0)
        pie.legend()
            .position("center-bottom")
            .itemsLayout(LegendLayout.HORIZONTAL)
            .align(Align.CENTER)
        return pie
    }

    fun reloadVerbrauchsChart() {
        //Geräte nach Verbrauchern filtern & data vorbereiten
        val data: MutableList<DataEntry> = ArrayList()
        for (geraet in verbraucherList)
            data.add(ValueDataEntry(geraet.getName(), geraet.getJahresverbrauch()))

        //WICHTIG! Bei Verwendung von mehr als einem Chart muss man beim erstellen / neu Zeichnen das aktuelle als aktiv markieren
        APIlib.getInstance().setActiveAnyChartView(anyChartVerbraucher)

        var pie = AnyChart.pie()
        pie = initPieChart(pie)

        pie.data(data)
        pie.title("Gesamtverbrauch "
                + String.format("%.2f", verbraucherList.sumByDouble{ geraete -> geraete.getJahresverbrauch() })
                + " kWh")
        pie.legend().title().text("Verbraucher")

        anyChartVerbraucher.setChart(pie)
    }

    fun reloadProduzentChart() {
        //Geräte nach Produzenten filtern & data vorbereiten
        val data: MutableList<DataEntry> = ArrayList()
        for (geraet in produzentList)
            data.add(ValueDataEntry(geraet.getName(), geraet.getJahresverbrauch()*(-1)))

        //WICHTIG! Bei Verwendung von mehr als einem Chart muss man beim erstellen / neu Zeichnen das aktuelle als aktiv markieren
        APIlib.getInstance().setActiveAnyChartView(anyChartProduzent)

        var pie = AnyChart.pie()
        pie = initPieChart(pie)

        pie.data(data)
        pie.title("Gesamtproduktion "
                + String.format("%.2f", produzentList.sumByDouble{ geraete -> geraete.getJahresverbrauch() }*(-1))
                + " kWh")
        pie.legend().title().text("Produzenten")

        anyChartProduzent.setChart(pie)
    }


    fun reloadKategorieChart() {
        val data: MutableList<DataEntry> = ArrayList()

        //Geräte nach Kategorie Gruppieren, dann die Summe der Jahresverbrauche berechnen
        //TODO Test mit mehreren Kategorien und mit Produzenten (diese sollten hier nicht angezeigt werden)
        val sortedgeraete : Map<Int, List<Geraete>> = verbraucherList.groupBy(keySelector = {it.getKategorieID()})
        for ( kat in sortedgeraete) {
            val currGeraeteInKat = kat.value
            var sum : Double = 0.0
            for (geraet in currGeraeteInKat) {
                sum += geraet.getJahresverbrauch()
            }
            if(sum > 0)
                data.add(ValueDataEntry(kategorieList[currGeraeteInKat.first().getKategorieID()-1].getName(), sum))
        }

        //WICHTIG! Bei Verwendung von mehr als einem Chart muss man beim erstellen / neu Zeichnen das aktuelle als aktiv markieren
        APIlib.getInstance().setActiveAnyChartView(anyChartKategorie)

        var pie = AnyChart.pie()
        pie = initPieChart(pie)

        pie.data(data)

        pie.title("Gesamtverbrauch Kategorien")
        pie.legend().title().text("Kategorien")

        anyChartKategorie.setChart(pie)
    }

    fun reloadRaumChart() {
        val data: MutableList<DataEntry> = ArrayList()

        //Geräte nach Raum Gruppieren, dann die Summe der Jahresverbrauche berechnen
        val sortedgeraete : Map<Int, List<Geraete>> = verbraucherList.groupBy(keySelector = {it.getRaumID()})
        for ( raum in sortedgeraete) {
            val currGeraeteInRaum = raum.value
            var sum : Double = 0.0
            for (geraet in currGeraeteInRaum) {
                sum += geraet.getJahresverbrauch()
            }
            if(sum > 0)
                data.add(ValueDataEntry(raumList[currGeraeteInRaum.first().getRaumID()-1].getName(), sum))
        }

        //WICHTIG! Bei Verwendung von mehr als einem Chart muss man beim erstellen / neu Zeichnen das aktuelle als aktiv markieren
        APIlib.getInstance().setActiveAnyChartView(anyChartRaum)

        var pie = AnyChart.pie()
        pie = initPieChart(pie)

        pie.data(data)

        pie.title("Gesamtverbrauch Räume")
        pie.legend().title().text("Räume")

        anyChartRaum.setChart(pie)
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