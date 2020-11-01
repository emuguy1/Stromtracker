package com.example.stromtracker.ui.geraete.auswertung

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.example.stromtracker.R
import com.example.stromtracker.database.*
import com.example.stromtracker.ui.geraete.GeraeteFragment
import com.example.stromtracker.ui.urlaub.UrlaubCompanion
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.round
import kotlin.math.withSign

private const val numToProzent = 100.0
private const val centToEuro = 0.01

// Quelle (Stand 07.2020): https://www.stromvergleich.de/durchschnittlicher-stromverbrauch
private const val dsBasisDeutschland: Double = 500.0
private const val dsVerbrauchDeutschland: Double = 1000.0

class GeraeteAuswertungNeu(
    private val verbraucherList: ArrayList<Geraete>,
    private val produzentList: ArrayList<Geraete>,
    private val kategorieList: ArrayList<Kategorie>,
    private val raumList: ArrayList<Raum>,
    private val urlaubList: ArrayList<Urlaub>,
    private val currHaushalt: Haushalt
) : Fragment(), View.OnClickListener {

    private var anzPersonen: Int = 1
    private var gesamtverbrauchkWh: Double = 0.0
    private var gesamtverbrauchEuro: Double = 0.0

    private lateinit var root: View

    private lateinit var aaChartVerbraucher: AAChartView
    private lateinit var textAvg: TextView
    private lateinit var aaChartBilanz: AAChartView
    private lateinit var textBilanz: TextView
    private lateinit var aaChartProduzent: AAChartView
    private lateinit var aaChartKategorie: AAChartView
    private lateinit var aaChartRaum: AAChartView
    private lateinit var btnBack: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_geraete_auswertung_neu, container, false)

        aaChartVerbraucher = root.findViewById<AAChartView>(R.id.aa_chart_verbraucher)
        aaChartBilanz = root.findViewById(R.id.aa_chart_bilanz)
        aaChartProduzent = root.findViewById(R.id.aa_chart_produzent)
        aaChartKategorie = root.findViewById(R.id.aa_chart_kategorie)
        aaChartRaum = root.findViewById(R.id.aa_chart_raum)


        textAvg = root.findViewById(R.id.geraete_auswertung_text_durchschnitt)
        textBilanz = root.findViewById(R.id.geraete_auswertung_text_bilanz)

        try {
            reloadVerbrauchsChart()
            reloadBilanzChart()
            reloadProduzentChart()
            reloadKategorieChart()
            reloadRaumChart()
        }
        catch (e : Exception) {
            Log.d("ERROR", "ERROR while loading chart: $e")
        }

        textAvg.text = generateDurchschnittText(gesamtverbrauchkWh)

        btnBack = root.findViewById(R.id.geraete_auswertung_button_back)
        btnBack.setOnClickListener(this)

        return root;
    }

    private fun getProdVerbrauch(geraet: Geraete): Double {
        return geraet.getJahresverbrauch().withSign(1) *
                geraet.getEigenverbrauch()!! / numToProzent
    }

    private fun initPieChart(chart: AAChartModel): AAChartModel {
        chart
            .chartType(AAChartType.Pie)
            .title("title")
            .dataLabelsEnabled(true)
        return chart
    }

    private fun initWaterfallChart(chart: AAChartModel): AAChartModel {
        chart
            .chartType(AAChartType.Waterfall)
            .title("title")
            .dataLabelsEnabled(true)
            .yAxisTitle("Verbrauch [kWh]")
            .categories( arrayOf("Verbrauch", "Produktion", "Urlaube", "Ergebnis") )
            .legendEnabled(false)
        return chart
    }

    private fun loadVerbraucherData(): Array<Any> {
        var totalData: ArrayList<Any> = ArrayList()
        for (geraet in verbraucherList) {
            totalData.add(arrayOf(geraet.getName(), geraet.getJahresverbrauch()))
        }
        return totalData.toTypedArray()
    }

    private fun reloadVerbrauchsChart() {
        // Geräte nach Verbrauchern filtern & data vorbereiten
        val data = loadVerbraucherData()
        if (data.isNotEmpty()) {
            var pie: AAChartModel = AAChartModel()
            pie = initPieChart(pie)
            pie.series(
                arrayOf(
                    AASeriesElement()
                        .name("Verbrauch in kWh")
                        .data(
                            loadVerbraucherData()
                        )
                )
            );
            pie.title(
                "Gesamtverbrauch"
            )
            gesamtverbrauchkWh = roundTo2Decimal(
                verbraucherList.sumByDouble { geraete -> geraete.getJahresverbrauch() })
            gesamtverbrauchEuro =
                roundTo2Decimal(
                    gesamtverbrauchkWh * currHaushalt.getStromkosten() * centToEuro
                )
            pie.subtitle("$gesamtverbrauchkWh kWh bzw. $gesamtverbrauchEuro€")

            aaChartVerbraucher.aa_drawChartWithChartModel(pie)
        } else {
            aaChartVerbraucher.visibility = View.GONE
        }
    }

    private fun loadBilanzData() : Array<Any> {
        var totalData: ArrayList<Any> = ArrayList()
        var tempSum = roundTo2Decimal(verbraucherList.sumByDouble { geraete -> geraete.getJahresverbrauch() })
        val verbraucher = HashMap<String, Any>()
        verbraucher["name"] = "Verbraucher"
        verbraucher["y"] = tempSum
        verbraucher["color"] = loadColorFromRes(R.color.colorRed)
        totalData.add(verbraucher)

        tempSum = produzentList.sumByDouble { geraete ->
            getProdVerbrauch(geraete)
        }.withSign(-1)
        tempSum = roundTo2Decimal(tempSum)
        val produzenten = HashMap<String, Any>()
        produzenten["name"] = "Produzenten"
        produzenten["y"] = tempSum
        produzenten["color"] = loadColorFromRes(R.color.colorPrimaryLight)
        totalData.add(produzenten)

        val tempList: ArrayList<Urlaub> = ArrayList()
        for (currUrlaub in urlaubList) {
            if ((currUrlaub.getDateVon().year + UrlaubCompanion.dateTimeToYears)
                == (Calendar.getInstance()
                    .get(Calendar.YEAR))
            ) {
                tempList.add(currUrlaub)
            }
        }
        tempSum = tempList.sumByDouble { urlaub ->
            urlaub.getErsparnisProTag() *
                    (urlaub.getDateBis().time / UrlaubCompanion.dateTimeToDays -
                            urlaub.getDateVon().time / UrlaubCompanion.dateTimeToDays + 1)
        }
        tempSum = roundTo2Decimal(tempSum.withSign(-1))
        val urlaube = HashMap<String, Any>()
        urlaube["name"] = "Urlaube"
        urlaube["y"] = tempSum
        urlaube["color"] = loadColorFromRes(R.color.colorPrimaryLight)
        totalData.add(urlaube)

        val ergebnis = HashMap<String, Any>()
        ergebnis["name"] = "Ergebnis"
        ergebnis["isSum"] = true
        ergebnis["color"] = loadColorFromRes(R.color.colorGreyLight)
        totalData.add(ergebnis)

        return totalData.toTypedArray()
    }

    private fun reloadBilanzChart() {
        val data = loadBilanzData()

        if (data.isNotEmpty()) {
            var waterfall: AAChartModel = AAChartModel()
            waterfall = initWaterfallChart(waterfall)
            waterfall.series(
                arrayOf(

                    AASeriesElement()
                        .name("kWh")
                        .data(
                            data
                        )

                    /*
                    AASeriesElement()
                        .name("Verbraucher")
                        .data(arrayOf("Verbrauchter Strom in kWh", 300)),
                    AASeriesElement()
                        .name("Produzenten")
                        .data(arrayOf("Produzierter Strom in kWh", -123)),
                    AASeriesElement()
                        .name("Urlaube")
                        .data(arrayOf("Ersparter Strom durch Urlaube in kWh", 48))
                     */
                )
            );
            waterfall.title("Bilanz")

            aaChartBilanz.aa_drawChartWithChartModel(waterfall)
        } else {
            aaChartBilanz.visibility = View.GONE
            textBilanz.visibility = View.GONE
        }
    }

    private fun loadProduzentData(): Array<Any> {
        var totalData: ArrayList<Any> = ArrayList()
        for (geraet in produzentList) {
            totalData.add(arrayOf(geraet.getName(), geraet.getJahresverbrauch().withSign(1)))
        }
        return totalData.toTypedArray()
    }

    private fun reloadProduzentChart() {
        val data = loadProduzentData()
        if (data.isNotEmpty()) {
            var pie: AAChartModel = AAChartModel()
            pie = initPieChart(pie)
            pie.series(arrayOf(
                    AASeriesElement()
                        .name("Produziert in kWh")
                        .data(data)
            ));
            pie.title("Gesamtproduktion")
            pie.subtitle(
                roundTo2Decimal(
                    produzentList.sumByDouble { geraete -> geraete.getJahresverbrauch() }
                        .withSign(1)
                ).toString() + " kWh"
            )

            aaChartProduzent.aa_drawChartWithChartModel(pie)
        } else {
            aaChartProduzent.visibility = View.GONE
        }
    }

    private fun loadKategorieData(): Array<Any> {
        val totalData: ArrayList<Any> = ArrayList()
        // Geräte nach Kategorie Gruppieren, dann die Summe der Jahresverbrauche berechnen
        val sortedgeraete: Map<Int, List<Geraete>> =
            verbraucherList.groupBy(keySelector = { it.getKategorieID() })
        for (kat in sortedgeraete) {
            val currGeraeteInKat = kat.value
            var sum = 0.0
            for (geraet in currGeraeteInKat) {
                sum += geraet.getJahresverbrauch()
            }
            if (sum > 0) {
                var name = ""
                for (currKat in kategorieList) {
                    if (currKat.getKategorieID() == currGeraeteInKat.first().getKategorieID()) {
                        name = currKat.getName()
                    }
                }
                totalData.add(
                    arrayOf(name, sum)
                )
            }
        }
        return totalData.toTypedArray()
    }

    private fun reloadKategorieChart() {
        val data = loadKategorieData()
        if (data.isNotEmpty()) {
            var pie: AAChartModel = AAChartModel()
            pie = initPieChart(pie)
            pie.series(arrayOf(
                AASeriesElement()
                    .name("Verbrauch in kWh")
                    .data(data)
            ));
            pie.title("Gesamtverbrauch Kategorien")
            aaChartKategorie.aa_drawChartWithChartModel(pie)
        } else {
            aaChartKategorie.visibility = View.GONE
        }
    }

    private fun loadRaumData(): Array<Any> {
        var totalData: ArrayList<Any> = ArrayList()
        // Geräte nach Raum Gruppieren, dann die Summe der Jahresverbrauche berechnen
        val sortedgeraete: Map<Int, List<Geraete>> =
            verbraucherList.groupBy(keySelector = { it.getRaumID() })
        for (raum in sortedgeraete) {
            val currGeraeteInRaum = raum.value
            var sum = 0.0
            for (geraet in currGeraeteInRaum) {
                sum += geraet.getJahresverbrauch()
            }
            if (sum > 0) {
                var name = ""
                for (currRaum in raumList) {
                    if (currRaum.getRaumID() == currGeraeteInRaum.first().getRaumID()) {
                        name = currRaum.getName()
                    }
                }
                totalData.add(arrayOf(name, sum))
            }
        }
        return totalData.toTypedArray()
    }

    private fun reloadRaumChart() {
        val data = loadRaumData()
        if (data.isNotEmpty()) {
            var pie: AAChartModel = AAChartModel()
            pie = initPieChart(pie)
            pie.series(arrayOf(
                AASeriesElement()
                    .name("Verbrauch in kWh")
                    .data(data)
            ));
            pie.title("Gesamtverbrauch Räume")
            aaChartRaum.aa_drawChartWithChartModel(pie)
        } else {
            aaChartRaum.visibility = View.GONE
        }
    }

    private fun generateDurchschnittText(verbr: Double): String {
        val proPerson: Double = roundTo2Decimal(verbr / anzPersonen)
        val dsGes: Double =
            roundTo2Decimal(dsVerbrauchDeutschland * anzPersonen + dsBasisDeutschland)
        var prozent = roundTo2Decimal(gesamtverbrauchkWh / dsGes)
        var str: String =
            "Der durchschnittliche Basisverbrauch pro Haushalt liegt in Deutschland bei" +
                    " $dsBasisDeutschland kWh/Jahr " +
                    "und der Verbrauch pro Person bei $dsVerbrauchDeutschland kWh/Jahr.\n" +
                    "Im aktuellen Haushalt befinden sich $anzPersonen Personen " +
                    "mit einem Gesamtverbrauch von $gesamtverbrauchkWh kWh. " +
                    "Daraus ergibt sich ein Verbrauch von $proPerson kWh pro Person.\n"
        if (prozent > 1.0) {
            prozent = (prozent - 1) * numToProzent
            str += "Somit ist der Verbrauch um " + roundTo2Decimal(prozent) + "% " +
                    "höher als der Durchschnitt."
        } else if (prozent == 1.0) {
            str += "Somit liegt der Verbrauch genau im Durchschnitt."
        } else {
            prozent = (1 - prozent) * numToProzent
            str += "Somit ist der Verbrauch um " + roundTo2Decimal(prozent) + "% " +
                    "geringer als der Durchschnitt."
        }
        return str
    }

    private fun loadColorFromRes(colorId : Int): String {
        return "#" + resources.getString(colorId).substring(3)
    }

    private fun roundTo2Decimal(num: Double): Double {
        return round(num * 100) / 100;
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
