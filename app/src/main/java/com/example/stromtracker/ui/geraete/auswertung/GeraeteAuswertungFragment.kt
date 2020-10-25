package com.example.stromtracker.ui.geraete.auswertung

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.anychart.enums.Align
import com.anychart.enums.LegendLayout
import com.anychart.enums.Orientation
import com.example.stromtracker.R
import com.example.stromtracker.database.*
import com.example.stromtracker.ui.geraete.GeraeteFragment
import com.example.stromtracker.ui.urlaub.UrlaubCompanion
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.round
import kotlin.math.withSign

private const val numToProzent = 100.0
private const val centToEuro = 0.01

// Quelle (Stand 07.2020): https://www.stromvergleich.de/durchschnittlicher-stromverbrauch
private const val dsBasisDeutschland: Double = 500.0
private const val dsVerbrauchDeutschland: Double = 1000.0

private const val piePaddingTop = 10.0
private const val piePaddingSides = 0.0

class GeraeteAuswertungFragment(
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

    private lateinit var anyChartVerbraucher: AnyChartView
    private lateinit var textAvg: TextView
    private lateinit var anyChartProduziertVerbrauch: AnyChartView
    private lateinit var anyChartBilanz: AnyChartView
    private lateinit var textBilanz: TextView
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

        anyChartVerbraucher = root.findViewById(R.id.any_chart_verbraucher)
        anyChartProduziertVerbrauch = root.findViewById(R.id.any_chart_produziert_verbrauch)
        anyChartBilanz = root.findViewById(R.id.any_chart_bilanz)
        anyChartProduzent = root.findViewById(R.id.any_chart_produzent)
        anyChartKategorie = root.findViewById(R.id.any_chart_kategorie)
        anyChartRaum = root.findViewById(R.id.any_chart_raum)
        textAvg = root.findViewById(R.id.geraete_auswertung_text_durchschnitt)
        textBilanz = root.findViewById(R.id.geraete_auswertung_text_bilanz)

        reloadVerbrauchsChart()
        reloadProduziertVerbrauchChart()
        reloadBilanzChart()
        reloadProduzentChart()
        reloadKategorieChart()
        reloadRaumChart()

        textAvg.text = generateDurchschnittText(gesamtverbrauchkWh)

        btnBack = root.findViewById(R.id.geraete_auswertung_button_back)
        btnBack.setOnClickListener(this)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun getProdVerbrauch(geraet: Geraete): Double {
        return geraet.getJahresverbrauch().withSign(1) *
                geraet.getEigenverbrauch()!! / numToProzent
    }

    private fun initPieChart(pie: Pie): Pie {
        pie.legend().title().enabled(true)
        pie.legend().title().padding(
            piePaddingTop, piePaddingSides, piePaddingSides, piePaddingSides
        )
        pie.legend()
            .position(Orientation.RIGHT)
            .itemsLayout(LegendLayout.VERTICAL)
            .align(Align.TOP)

        return pie
    }

    private fun reloadVerbrauchsChart() {
        // Geräte nach Verbrauchern filtern & data vorbereiten
        val data: MutableList<DataEntry> = ArrayList()
        for (geraet in verbraucherList)
            data.add(ValueDataEntry(geraet.getName(), geraet.getJahresverbrauch()))

        if (data.isNotEmpty()) {
            // WICHTIG! Bei Verwendung von mehr als einem Chart muss man beim erstellen /
            // neu Zeichnen das aktuelle als aktiv markieren
            APIlib.getInstance().setActiveAnyChartView(anyChartVerbraucher)

            var pie = AnyChart.pie()
            pie = initPieChart(pie)

            pie.data(data)
            gesamtverbrauchkWh = roundTo2Decimal(
                verbraucherList.sumByDouble { geraete -> geraete.getJahresverbrauch() })
            gesamtverbrauchEuro =
                roundTo2Decimal(
                    gesamtverbrauchkWh * currHaushalt.getStromkosten() * centToEuro
                )
            pie.title(
                "Gesamtverbrauch $gesamtverbrauchkWh kWh bzw. $gesamtverbrauchEuro€"
            )
            pie.legend().title().text("Verbraucher")

            anyChartVerbraucher.setChart(pie)
        } else {
            anyChartVerbraucher.visibility = View.GONE
        }
    }

    private fun reloadProduziertVerbrauchChart() {
        val data: MutableList<DataEntry> = ArrayList()
        for (geraet in produzentList)
            data.add(ValueDataEntry(geraet.getName(), getProdVerbrauch(geraet)))

        if (data.isNotEmpty()) {
            APIlib.getInstance().setActiveAnyChartView(anyChartProduziertVerbrauch)

            var pie = AnyChart.pie()
            pie = initPieChart(pie)

            pie.data(data)
            pie.title(
                "Produzierte Energie, die verbraucht wird " +
                        roundTo2Decimal(produzentList.sumByDouble { geraete ->
                            getProdVerbrauch(
                                geraete
                            )
                        }) +
                        " kWh"
            )
            pie.legend().title().text("Produzenten")

            anyChartProduziertVerbrauch.setChart(pie)
        } else {
            anyChartProduziertVerbrauch.visibility = View.GONE
        }
    }

    private fun reloadBilanzChart() {
        val data: MutableList<DataEntry> = ArrayList()
        var tempSum = verbraucherList.sumByDouble { geraete -> geraete.getJahresverbrauch() }
        data.add(ValueDataEntry("Verbraucher", tempSum))

        tempSum = produzentList.sumByDouble { geraete ->
            getProdVerbrauch(geraete)
        }.withSign(-1)
        data.add(ValueDataEntry("Produzenten", tempSum))

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
        tempSum = tempSum.withSign(-1)
        tempSum = roundTo2Decimal(tempSum)
        data.add(ValueDataEntry("Urlaube", tempSum))

        if (data.isNotEmpty()) {
            APIlib.getInstance().setActiveAnyChartView(anyChartBilanz)

            val wat = AnyChart.waterfall()
            wat.yAxis(0).labels().format("{%Value}{scale:(1)(1)|(kWh)}")
            wat.labels().enabled(true)

            val end = DataEntry()
            end.setValue("x", "Ergebnis")
            end.setValue("isTotal", true)
            data.add(end)
            wat.data(data)
            wat.title("Bilanz")

            anyChartBilanz.setChart(wat)
        } else {
            anyChartBilanz.visibility = View.GONE
            textBilanz.visibility = View.GONE
        }
    }

    private fun reloadProduzentChart() {
        // Geräte nach Produzenten filtern & data vorbereiten
        val data: MutableList<DataEntry> = ArrayList()
        for (geraet in produzentList)
            data.add(
                ValueDataEntry(
                    geraet.getName(), geraet.getJahresverbrauch().withSign(1)
                )
            )

        if (data.isNotEmpty()) {
            // WICHTIG! Bei Verwendung von mehr als einem Chart muss man beim erstellen /
            // neu Zeichnen das aktuelle als aktiv markieren
            APIlib.getInstance().setActiveAnyChartView(anyChartProduzent)

            var pie = AnyChart.pie()
            pie = initPieChart(pie)

            pie.data(data)
            pie.title(
                "Gesamtproduktion " +
                        roundTo2Decimal(
                            produzentList.sumByDouble { geraete -> geraete.getJahresverbrauch() }
                                .withSign(1)
                        ) +
                        " kWh"
            )
            pie.legend().title().text("Produzenten")

            anyChartProduzent.setChart(pie)
        } else {
            anyChartProduzent.visibility = View.GONE
        }
    }

    private fun reloadKategorieChart() {
        val data: MutableList<DataEntry> = ArrayList()

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
                data.add(
                    ValueDataEntry(
                        name, sum
                    )
                )
            }
        }

        if (data.isNotEmpty()) {
            // WICHTIG! Bei Verwendung von mehr als einem Chart muss man beim erstellen /
            // neu Zeichnen das aktuelle als aktiv markieren
            APIlib.getInstance().setActiveAnyChartView(anyChartKategorie)

            var pie = AnyChart.pie()
            pie = initPieChart(pie)

            pie.data(data)

            pie.title("Gesamtverbrauch Kategorien")
            pie.legend().title().text("Kategorien")

            anyChartKategorie.setChart(pie)
        } else {
            anyChartKategorie.visibility = View.GONE
        }
    }

    private fun reloadRaumChart() {
        val data: MutableList<DataEntry> = ArrayList()

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
                data.add(
                    ValueDataEntry(
                        name, sum
                    )
                )
            }
        }
        if (data.isNotEmpty()) {

            // WICHTIG! Bei Verwendung von mehr als einem Chart muss man beim erstellen /
            // neu Zeichnen das aktuelle als aktiv markieren
            APIlib.getInstance().setActiveAnyChartView(anyChartRaum)

            var pie = AnyChart.pie()
            pie = initPieChart(pie)

            pie.data(data)

            pie.title("Gesamtverbrauch Räume")
            pie.legend().title().text("Räume")

            anyChartRaum.setChart(pie)
        } else {
            anyChartRaum.visibility = View.GONE
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

    private fun roundTo2Decimal(num: Double): Double {
        return round(num * 100)/100;
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
