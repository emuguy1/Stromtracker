package com.example.stromtracker.ui.urlaub.urlaub_new

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R
import com.example.stromtracker.database.Geraete
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.database.Urlaub
import com.example.stromtracker.ui.urlaub.UrlaubFragment
import com.example.stromtracker.ui.urlaub.UrlaubCompanion
import com.example.stromtracker.ui.urlaub.UrlaubCompanion.Companion.centToEuro
import com.example.stromtracker.ui.urlaub.UrlaubCompanion.Companion.dateTimeToDays
import com.example.stromtracker.ui.urlaub.UrlaubCompanion.Companion.dayLen
import com.example.stromtracker.ui.urlaub.UrlaubCompanion.Companion.wattToKW
import com.example.stromtracker.ui.urlaub.UrlaubCompanion.Companion.yearToDay
import com.example.stromtracker.ui.urlaub.UrlaubViewModel
import java.text.SimpleDateFormat
import java.util.*

class UrlaubNewFragment(private val geraete: List<Geraete>, private val currHaushalt: Haushalt) :
    Fragment(), View.OnClickListener {

    private lateinit var urlaubViewModel: UrlaubViewModel

    private var gesamtverbrauchNeuPT: Double = 0.0
    private var gesamtverbrauchAktPT: Double = 0.0

    private lateinit var name: EditText
    private lateinit var start: EditText
    private lateinit var ende: EditText

    private lateinit var outTage: TextView
    private lateinit var outErsparnis: TextView

    private lateinit var btnAbbrechen: Button
    private lateinit var btnSpeichern: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        urlaubViewModel =
            ViewModelProviders.of(this).get(UrlaubViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_urlaub_new, container, false)

        name = root.findViewById(R.id.urlaub_new_edit_name)
        start = root.findViewById(R.id.urlaub_new_edit_datum_von)
        addCustomTextChangedListener(start)
        ende = root.findViewById(R.id.urlaub_new_edit_datum_bis)
        addCustomTextChangedListener(ende)

        outTage = root.findViewById(R.id.urlaub_new_text_tage)
        outErsparnis = root.findViewById(R.id.urlaub_new_text_ersparnis)

        btnAbbrechen = root.findViewById(R.id.urlaub_new_button_abbrechen)
        btnAbbrechen.setOnClickListener(this)
        btnSpeichern = root.findViewById(R.id.urlaub_new_button_speichern)
        btnSpeichern.setOnClickListener(this)

        loadGesamtverbrauch()

        return root
    }

    private fun loadGesamtverbrauch() {
        for (geraet in geraete) {
            if (geraet.getUrlaubsmodus() == false) {
                if (geraet.getStromStandBy() != null)    //TODO && geraet.getStandByZeit != null
                    gesamtverbrauchNeuPT += geraet.getStromStandBy() * dayLen
                else {
                    gesamtverbrauchNeuPT += geraet.getStromVollast() * geraet.getBetriebszeit() as Double
                }
            }
            gesamtverbrauchAktPT += geraet.getJahresverbrauch()
        }
        //Beide werden nun einheitlich gespeichert, als kWh pro Tag
        gesamtverbrauchNeuPT *= wattToKW
        gesamtverbrauchAktPT *= yearToDay
    }

    private fun addCustomTextChangedListener(edit: EditText): EditText {
        edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (UrlaubCompanion.checkDates(start, ende)) {
                    val start =
                        SimpleDateFormat(
                            "dd.MM.yyyy",
                            Locale.GERMAN
                        ).parse(start.text.toString())
                    val ende =
                        SimpleDateFormat(
                            "dd.MM.yyyy",
                            Locale.GERMAN
                        ).parse(ende.text.toString())
                    updateOutputs(start, ende)
                }
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        return edit
    }

    fun updateOutputs(st: Date?, end: Date?) {
        var neuStr: String
        if (st != null && end != null) {
            val countTage: Double = end.time / dateTimeToDays - st.time / dateTimeToDays + 1
            neuStr = "Der Urlaub dauert insgesamt $countTage Tage"
            outTage.text = neuStr
            val diffkWh = (gesamtverbrauchAktPT - gesamtverbrauchNeuPT) * countTage
            val diffEuro = diffkWh * currHaushalt.getStromkosten() * centToEuro
            neuStr = "Währenddessen werden " + String.format(
                "%.2f",
                diffkWh
            ) + "kWh, also " + String.format("%.2f", diffEuro) + "€ gespart"
            outErsparnis.text = neuStr
        } else {
            outTage.text = null
            outErsparnis.text = null
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.urlaub_new_button_abbrechen -> {
                val frag = UrlaubFragment()
                val fragMan = parentFragmentManager
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                    .addToBackStack(null).commit()
            }
            R.id.urlaub_new_button_speichern -> {
                if (name.text.isNotEmpty() && UrlaubCompanion.checkDates(start, ende)) {

                    val tempDateStart =
                        SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).parse(start.text.toString())
                    val tempDateEnde =
                        SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).parse(ende.text.toString())
                    if (tempDateStart != null && tempDateEnde != null) {
                        val urlaub = Urlaub(
                            name.text.toString(),
                            tempDateStart,
                            tempDateEnde,
                            gesamtverbrauchNeuPT,
                            currHaushalt.getHaushaltID()
                        )
                        urlaubViewModel.insertUrlaub(urlaub)
                        val frag = UrlaubFragment()
                        val fragman = parentFragmentManager
                        fragman.beginTransaction().replace(R.id.nav_host_fragment, frag)
                            .addToBackStack(null).commit()
                    } else
                        Toast.makeText(
                            this.context,
                            R.string.toast_invalid_date,
                            Toast.LENGTH_SHORT
                        ).show()
                } else
                    Toast.makeText(this.context, R.string.toast_invalid_values, Toast.LENGTH_SHORT)
                        .show()
            }
        }
    }
}
