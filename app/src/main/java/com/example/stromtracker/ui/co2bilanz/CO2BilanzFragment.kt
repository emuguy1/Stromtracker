package com.example.stromtracker.ui.co2bilanz

import android.os.Bundle
import android.util.Log
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
    private val avgVerbrauch: Double = 1500.0 //Durchschnittlicher Verbrauch
    private val avgAusstoss: Double = avgVerbrauch * co2

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
                }
            })
    }

    fun updateText() {
        var gesamtverbrauch =
            verbraucherList.sumByDouble { geraet -> geraet.getJahresverbrauch() }
        var urlaubsVerbrauch = urlaubList.sumByDouble { urlaub ->
            urlaub.getErsparnisProTag() * (urlaub.getDateBis().time / UrlaubCompanion.dateTimeToDays - urlaub.getDateVon().time / UrlaubCompanion.dateTimeToDays + 1)
        }
        var produzentenVerbrauch =
            produzentList.sumByDouble { geraete -> getProdVerbrauch(geraete) }

        var verbrauch = gesamtverbrauch - urlaubsVerbrauch - produzentenVerbrauch
        var personen = currHaushalt.getBewohnerAnzahl()
        if (!currHaushalt.getOekostrom()) {
            val ausstoss = verbrauch * co2
            var proPerson = ausstoss / personen
            if (proPerson > avgAusstoss) {
                var percentage = avgAusstoss / proPerson
                var avgString = "Dies ist um"
            }
            var string =
                "Der derzeitige Gesamtverbrauch beträgt $verbrauch kwH pro Jahr. Dies entspricht einem" +
                        "gesamten CO2-Ausstoß von $ausstoss" + "kg, oder $proPerson" + "kg pro Person."

        }


    }


    fun getProdVerbrauch(geraet: Geraete): Double {
        return geraet.getJahresverbrauch().withSign(1) * geraet.getEigenverbrauch()!! / 100
    }
}
