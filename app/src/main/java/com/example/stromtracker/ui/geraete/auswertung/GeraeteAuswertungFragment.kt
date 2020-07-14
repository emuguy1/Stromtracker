package com.example.stromtracker.ui.geraete.auswertung

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.anychart.charts.Waterfall
import com.anychart.enums.Align
import com.anychart.enums.LegendLayout
import com.anychart.enums.Orientation
import com.example.stromtracker.R
import com.example.stromtracker.database.Geraete
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.database.Kategorie
import com.example.stromtracker.database.Raum
import com.example.stromtracker.ui.geraete.GeraeteFragment
import com.example.stromtracker.ui.geraete.GeraeteViewModel
import com.google.android.material.navigation.NavigationView
import kotlin.math.withSign


class GeraeteAuswertungFragment(
    private val verbraucherList: ArrayList<Geraete>,
    private val produzentList: ArrayList<Geraete>,
    private val kategorieList: ArrayList<Kategorie>,
    private val raumList: ArrayList<Raum>
) : Fragment(), View.OnClickListener {

    //Quelle (Stand 07.2020): https://www.stromvergleich.de/durchschnittlicher-stromverbrauch
    private val dsBasisDeutschland : Double = 500.0
    private val dsVerbrauchDeutschland : Double = 1000.0

    private var anzPersonen : Int = 1
    private var gesamtverbrauch : Double = 0.0

    private lateinit var root: View
    private lateinit var geraeteViewModel: GeraeteViewModel

    private lateinit var currHaushalt: Haushalt

    private lateinit var anyChartVerbraucher: AnyChartView
    private lateinit var textAvg : TextView
    private lateinit var anyChartProduziertVerbrauch: AnyChartView
    private lateinit var anyChartVerbrMinusProd: AnyChartView
    private lateinit var anyChartProduzent: AnyChartView
    private lateinit var anyChartKategorie: AnyChartView
    private lateinit var anyChartRaum: AnyChartView
    private lateinit var btnBack: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_geraete_auswertung, container, false)

        val navView = requireActivity().findViewById<NavigationView>(R.id.nav_view)
        val sp: Spinner = navView.menu.findItem(R.id.nav_haushalt).actionView as Spinner
        currHaushalt = sp.selectedItem as Haushalt
        anzPersonen = currHaushalt.getBewohnerAnzahl()

        anyChartVerbraucher = root.findViewById(R.id.any_chart_verbraucher)
        anyChartProduziertVerbrauch = root.findViewById(R.id.any_chart_produziert_verbrauch)
        anyChartVerbrMinusProd = root.findViewById(R.id.any_chart_verbr_minus_prod)
        anyChartProduzent = root.findViewById(R.id.any_chart_produzent)
        anyChartKategorie = root.findViewById(R.id.any_chart_kategorie)
        anyChartRaum = root.findViewById(R.id.any_chart_raum)
        textAvg = root.findViewById(R.id.geraete_auswertung_text_durchschnitt)

        reloadVerbrauchsChart()
        reloadProduziertVerbrauchChart()
        reloadVerbrMinusProdChart()
        reloadProduzentChart()
        reloadKategorieChart()
        reloadRaumChart()

        textAvg.text = generateDurchschnittText(gesamtverbrauch)

        btnBack = root.findViewById(R.id.geraete_auswertung_button_back)
        btnBack.setOnClickListener(this)


        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        geraeteViewModel = ViewModelProviders.of(this).get(GeraeteViewModel::class.java)
    }

    fun getProdVerbrauch(geraet: Geraete): Double {
        return geraet.getJahresverbrauch().withSign(1) * geraet.getEigenverbrauch()!! / 100
    }


    fun initPieChart(pie: Pie): Pie {
        pie.legend().title().enabled(true)
        pie.legend().title().padding(10.0, 0.0, 0.0, 0.0)
        pie.legend()
            .position(Orientation.RIGHT)
            .itemsLayout(LegendLayout.VERTICAL)
            .align(Align.TOP)

        return pie
    }

    fun reloadVerbrauchsChart() {
        //Geräte nach Verbrauchern filtern & data vorbereiten
        val data: MutableList<DataEntry> = ArrayList()
        for (geraet in verbraucherList)
            data.add(ValueDataEntry(geraet.getName(), geraet.getJahresverbrauch()))

        if(data.isNotEmpty()) {
            //WICHTIG! Bei Verwendung von mehr als einem Chart muss man beim erstellen / neu Zeichnen das aktuelle als aktiv markieren
            APIlib.getInstance().setActiveAnyChartView(anyChartVerbraucher)

            var pie = AnyChart.pie()
            pie = initPieChart(pie)

            pie.data(data)
            gesamtverbrauch = roundTo2Decimal(verbraucherList.sumByDouble { geraete -> geraete.getJahresverbrauch() })
            pie.title(
                "Gesamtverbrauch "
                        + roundTo2Decimal(gesamtverbrauch)
                        + " kWh"
            )
            pie.legend().title().text("Verbraucher")

            anyChartVerbraucher.setChart(pie)
        }
        else
            anyChartVerbraucher.visibility = View.GONE
    }

    fun reloadProduziertVerbrauchChart() {
        val data: MutableList<DataEntry> = ArrayList()
        for (geraet in produzentList)
            data.add(ValueDataEntry(geraet.getName(), getProdVerbrauch(geraet)))

        if(data.isNotEmpty()) {
            APIlib.getInstance().setActiveAnyChartView(anyChartProduziertVerbrauch)

            var pie = AnyChart.pie()
            pie = initPieChart(pie)

            pie.data(data)
            pie.title(
                "Produzierte Energie, die verbraucht wird "
                        + roundTo2Decimal(produzentList.sumByDouble { geraete -> getProdVerbrauch(geraete) })
                        + " kWh"
            )
            pie.legend().title().text("Produzenten")

            anyChartProduziertVerbrauch.setChart(pie)
        }
        else
            anyChartProduziertVerbrauch.visibility = View.GONE
    }

    fun reloadVerbrMinusProdChart() {
        val data: MutableList<DataEntry> = ArrayList()
        var tempSum = verbraucherList.sumByDouble { geraete -> geraete.getJahresverbrauch() }
        data.add(ValueDataEntry("Verbraucher", tempSum))

        tempSum = produzentList.sumByDouble { geraete -> getProdVerbrauch(geraete) }.withSign(-1)
        data.add(ValueDataEntry("Produzenten", tempSum))

        if (data.isNotEmpty()) {
            APIlib.getInstance().setActiveAnyChartView(anyChartVerbrMinusProd)

            val wat = AnyChart.waterfall()
            wat.yAxis(0).labels().format("{%Value}{scale:(1)(1)|(kWh)}")
            wat.labels().enabled(true)

            val end = DataEntry()
            end.setValue("x", "Ergebnis")
            end.setValue("isTotal", true)
            data.add(end)
            wat.data(data)
            wat.title("Verbrauch - Produktion")

            anyChartVerbrMinusProd.setChart(wat)
        } else
            anyChartVerbrMinusProd.visibility = View.GONE
    }

    fun reloadProduzentChart() {
        //Geräte nach Produzenten filtern & data vorbereiten
        val data: MutableList<DataEntry> = ArrayList()
        for (geraet in produzentList)
            data.add(ValueDataEntry(geraet.getName(), geraet.getJahresverbrauch().withSign(1)))

        if (data.isNotEmpty()) {
            //WICHTIG! Bei Verwendung von mehr als einem Chart muss man beim erstellen / neu Zeichnen das aktuelle als aktiv markieren
            APIlib.getInstance().setActiveAnyChartView(anyChartProduzent)

            var pie = AnyChart.pie()
            pie = initPieChart(pie)

            pie.data(data)
            pie.title(
                "Gesamtproduktion "
                        + roundTo2Decimal(produzentList.sumByDouble { geraete -> geraete.getJahresverbrauch() } * (-1))
                        + " kWh"
            )
            pie.legend().title().text("Produzenten")

            anyChartProduzent.setChart(pie)
        } else
            anyChartProduzent.visibility = View.GONE
    }

    fun reloadKategorieChart() {
        val data: MutableList<DataEntry> = ArrayList()

        //Geräte nach Kategorie Gruppieren, dann die Summe der Jahresverbrauche berechnen
        //TODO Test mit mehreren Kategorien und mit Produzenten (diese sollten hier nicht angezeigt werden)
        val sortedgeraete: Map<Int, List<Geraete>> =
            verbraucherList.groupBy(keySelector = { it.getKategorieID() })
        for (kat in sortedgeraete) {
            val currGeraeteInKat = kat.value
            var sum: Double = 0.0
            for (geraet in currGeraeteInKat) {
                sum += geraet.getJahresverbrauch()
            }
            if (sum > 0)
                data.add(
                    ValueDataEntry(
                        kategorieList[currGeraeteInKat.first().getKategorieID() - 1].getName(), sum
                    )
                )
        }

        if(data.isNotEmpty()) {
            //WICHTIG! Bei Verwendung von mehr als einem Chart muss man beim erstellen / neu Zeichnen das aktuelle als aktiv markieren
            APIlib.getInstance().setActiveAnyChartView(anyChartKategorie)

            var pie = AnyChart.pie()
            pie = initPieChart(pie)

            pie.data(data)

            pie.title("Gesamtverbrauch Kategorien")
            pie.legend().title().text("Kategorien")

            anyChartKategorie.setChart(pie)
        }
        else
            anyChartKategorie.visibility = View.GONE
    }

    fun reloadRaumChart() {
        val data: MutableList<DataEntry> = ArrayList()

        //Geräte nach Raum Gruppieren, dann die Summe der Jahresverbrauche berechnen
        val sortedgeraete: Map<Int, List<Geraete>> =
            verbraucherList.groupBy(keySelector = { it.getRaumID() })
        for (raum in sortedgeraete) {
            val currGeraeteInRaum = raum.value
            var sum: Double = 0.0
            for (geraet in currGeraeteInRaum) {
                sum += geraet.getJahresverbrauch()
            }
            if (sum > 0)
                data.add(
                    ValueDataEntry(
                        raumList[currGeraeteInRaum.first().getRaumID() - 1].getName(), sum
                    )
                )
        }
        if (data.isNotEmpty()) {

            //WICHTIG! Bei Verwendung von mehr als einem Chart muss man beim erstellen / neu Zeichnen das aktuelle als aktiv markieren
            APIlib.getInstance().setActiveAnyChartView(anyChartRaum)

            var pie = AnyChart.pie()
            pie = initPieChart(pie)

            pie.data(data)

            pie.title("Gesamtverbrauch Räume")
            pie.legend().title().text("Räume")

            anyChartRaum.setChart(pie)
        } else
            anyChartRaum.visibility = View.GONE
    }

    fun generateDurchschnittText (verbr : Double) : String{
        val proPerson : Double = roundTo2Decimal(gesamtverbrauch / anzPersonen)
        val dsGes : Double = roundTo2Decimal (dsVerbrauchDeutschland * anzPersonen + dsBasisDeutschland)
        var prozent = roundTo2Decimal(gesamtverbrauch / dsGes)
        var str : String =
            "Der durchschnittliche Basisverbrauch pro Haushalt liegt in Deutschland bei $dsBasisDeutschland kWh/Jahr " +
                    "und der Verbrauch pro Person bei $dsVerbrauchDeutschland kWh/Jahr.\n" +
                    "Im aktuellen Haushalt befinden sich $anzPersonen Personen " +
                    "mit einem Gesamtverbrauch von $gesamtverbrauch kWh. " +
                    "Daraus ergibt sich ein Verbrauch von $proPerson kWh pro Person.\n"
        if(prozent > 1.0) {
            prozent = (prozent % 1) * 100
            str += "Somit ist der Verbrauch um " + roundTo2Decimal(prozent) + "% höher als der Durchschnitt."
        }
        else if(prozent == 1.0)
            str += "Somit liegt der Verbrauch genau im Durchschnitt."
        else {
            prozent = (1-prozent) * 100
            str += "Somit ist der Verbrauch um " + roundTo2Decimal(prozent) + "% geringer als der Durchschnitt."
        }
        return str
    }

    fun roundTo2Decimal(num : Double) : Double {
        return String.format("%.2f", num).toDouble()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.geraete_auswertung_button_back -> {
                val frag = GeraeteFragment()
                val fragMan = parentFragmentManager
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                    .addToBackStack(null).commit()
            }
        }
    }

}