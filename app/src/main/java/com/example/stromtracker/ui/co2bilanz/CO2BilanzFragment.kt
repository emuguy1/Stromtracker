package com.example.stromtracker.ui.co2bilanz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProvider
import com.example.stromtracker.R
import com.example.stromtracker.database.*
import com.example.stromtracker.ui.SharedViewModel
import com.example.stromtracker.ui.urlaub.UrlaubCompanion
import kotlin.math.round
import kotlin.math.withSign

class CO2BilanzFragment : Fragment() {

    private lateinit var co2bilanzViewModel: CO2BilanzViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var root: View
    private lateinit var verbraucherList: ArrayList<Geraete>
    private lateinit var urlaubList: ArrayList<Urlaub>
    private lateinit var produzentList: ArrayList<Geraete>
    private lateinit var raumListHaushalt: ArrayList<Raum>
    private lateinit var currHaushalt: Haushalt
    private val co2: Double = 0.401 //Deutscher Strommix, kg CO2 pro kWh im Jahre 2019
    private val avgVerbrauchGrund: Double =
        500.0 //Durchschnittlicher Verbrauch des Haushalts, siehe UrlaubsFragment
    private val avgVerbrauchPerson: Double = 1000.0
    private lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_co2bilanz, container, false)

        verbraucherList = ArrayList()
        produzentList = ArrayList()
        raumListHaushalt = ArrayList()
        urlaubList = ArrayList()

        textView = root.findViewById(R.id.co2_text_bilanz)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        co2bilanzViewModel = ViewModelProvider(this).get(CO2BilanzViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)


        val verbraucherData: LiveData<List<Geraete>> =
            Transformations.switchMap(sharedViewModel.getHaushalt()) { haushalt ->
                co2bilanzViewModel.getAllVerbraucherByHaushaltID(haushalt.getHaushaltID())
            }
        verbraucherData.observe(
            viewLifecycleOwner,
            Observer { geraete ->
                if (geraete != null) {
                    verbraucherList.clear()
                    verbraucherList.addAll(geraete)
                    updateText()
                }
            })

        val raumDataHaushalt: LiveData<List<Raum>> =
            Transformations.switchMap(sharedViewModel.getHaushalt()) { haushalt ->
                co2bilanzViewModel.getAllRaumByHaushaltID(haushalt.getHaushaltID())
            }
        raumDataHaushalt.observe(
            viewLifecycleOwner,
            Observer { raum ->
                if (raum != null) {
                    raumListHaushalt.clear()
                    raumListHaushalt.addAll(raum)
                    updateText()

                }
            })

        sharedViewModel.getHaushalt().observe(
            viewLifecycleOwner,
            Observer { haushalt ->
                currHaushalt = haushalt
            }
        )

        val produzentData: LiveData<List<Geraete>> =
            Transformations.switchMap(sharedViewModel.getHaushalt()) { haushalt ->
                co2bilanzViewModel.getAllProduzentenByHaushaltID(haushalt.getHaushaltID())
            }
        produzentData.observe(
            viewLifecycleOwner,
            Observer { produzenten ->
                if (produzenten != null) {
                    produzentList.clear()
                    produzentList.addAll(produzenten)
                    updateText()
                }
            }
        )

        val urlaubData: LiveData<List<Urlaub>> =
            Transformations.switchMap(sharedViewModel.getHaushalt()) { haushalt ->
                co2bilanzViewModel.getAllUrlaubByHaushaltID(haushalt.getHaushaltID())
            }
        urlaubData.observe(
            viewLifecycleOwner,
            Observer { urlaub ->
                if (urlaub != null) {
                    urlaubList.clear()
                    urlaubList.addAll(urlaub)
                    updateText()

                }
            })
    }

    fun updateText() {
        val gesamtverbrauch =
            verbraucherList.sumByDouble { geraet -> geraet.getJahresverbrauch() }
        val urlaubsVerbrauch = urlaubList.sumByDouble { urlaub ->
            urlaub.getErsparnisProTag() * (urlaub.getDateBis().time / UrlaubCompanion.dateTimeToDays - urlaub.getDateVon().time / UrlaubCompanion.dateTimeToDays + 1)
        }
        val produzentenVerbrauch =
            produzentList.sumByDouble { geraete -> getProdVerbrauch(geraete) }

        val verbrauch = gesamtverbrauch - urlaubsVerbrauch - produzentenVerbrauch
        val personen = currHaushalt.getBewohnerAnzahl()
        val avgAusstoss = (avgVerbrauchGrund + (avgVerbrauchPerson * personen)) * co2
        val ausstoss = roundDouble(verbrauch * co2)
        val proPerson = ausstoss / personen
        val percentage = (100 - roundDouble((ausstoss / avgAusstoss) * 100)).withSign(1)
        val avgString: String
        var string: String
        val diff: Double
        var comp: String

        if (!currHaushalt.getOekostrom()) {
            if (ausstoss > avgAusstoss) {
                diff = roundDouble(ausstoss - avgAusstoss)
                comp = getString(R.string.co2_text_groeßer)
                avgString =
                    String.format(getString(R.string.co2_text_comp_main), percentage, comp, diff)
            } else if (ausstoss < avgAusstoss) {
                diff = roundDouble(avgAusstoss - ausstoss)

                comp = getString(R.string.co2_text_kleiner)
                avgString =
                    String.format(
                        getString(R.string.co2_text_comp_main),
                        percentage,
                        comp,
                        diff
                    )
            } else {
                avgString = getString(R.string.co2_text_comp_avg)
            }
            string = String.format(
                getString(R.string.co2_text_main_normal),
                verbrauch,
                ausstoss,
                proPerson
            )
            string += "\n$avgString"
        } else {

            string = String.format(
                getString(R.string.co2_text_main_oeko),
                verbrauch,
                ausstoss,
                proPerson
            )
        }
        textView.text = string
    }

    private fun getProdVerbrauch(geraet: Geraete): Double {
        return geraet.getJahresverbrauch().withSign(1) * geraet.getEigenverbrauch()!! / 100
    }

    private fun roundDouble(number: Double): Double {
        return round(number * 100) / 100
    }

}
