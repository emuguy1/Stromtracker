package com.example.stromtracker.ui.verbrauchsrechner

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.stromtracker.R

private const val tagStunden = 24
private const val jahrTag = 365
private const val centToEuro = 0.01
private const val wattToKiloWatt = 0.001

class VerbrauchsrechnerFragment : Fragment() {

    private lateinit var strompreis: EditText
    private lateinit var leistungsverbrauch: EditText
    private lateinit var standbyverbrauch: EditText
    private lateinit var standbyzeit: EditText
    private lateinit var lastzeit: EditText

    private lateinit var verbrauchprojahr: TextView
    private lateinit var kostenprojahr: TextView
    private lateinit var warnungstext: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_verbrauchsrechner, container, false)
        strompreis = root.findViewById(R.id.edit_text_verbrauchsrechner_strompreis_kwh_input)
        leistungsverbrauch = root.findViewById(R.id.edit_text_verbrauchsrechner_leistung_kwh_input)
        standbyverbrauch =
            root.findViewById(R.id.edit_text_verbrauchsrechner_stand_by_leistung_kwh_input)
        standbyzeit =
            root.findViewById(R.id.edit_text_verbrauchsrechner_stand_by_leistung_zeit_kwh_input)
        lastzeit = root.findViewById(R.id.edit_text_verbrauchsrechner_leistung_zeit_kwh_input)

        verbrauchprojahr = root.findViewById(R.id.text_view_verbrauchsrechner_erg_verbrauch_jahr)
        kostenprojahr = root.findViewById(R.id.text_view_verbrauchsrechner_erg_kosten_jahr)
        warnungstext = root.findViewById(R.id.text_view_verbrauchsrechner_warnung)

        customTextListener(strompreis)
        customTextListener(leistungsverbrauch)
        customTextListener(standbyverbrauch)
        customTextListener(standbyzeit)
        customTextListener(lastzeit)
        return root
    }

    private fun customTextListener(edit: EditText): EditText {
        edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val neustrompreis: Double? = strompreis.text.toString().toDoubleOrNull()
                val lastverbrauch: Double? = leistungsverbrauch.text.toString().toDoubleOrNull()
                val standbystromverbrauch: Double? =
                    standbyverbrauch.text.toString().toDoubleOrNull()
                val volllastzeit: Double? = lastzeit.text.toString().toDoubleOrNull()
                val standbydauer: Double? = standbyzeit.text.toString().toDoubleOrNull()
                var verbrauch: Double
                var euro: Double
                if (neustrompreis != null && lastverbrauch != null && volllastzeit != null) {

                    verbrauch = (((lastverbrauch * volllastzeit) * wattToKiloWatt) * jahrTag)
                    euro =
                        (((lastverbrauch * volllastzeit) * wattToKiloWatt) *
                                neustrompreis * centToEuro * jahrTag)
                    if (volllastzeit > tagStunden) {
                        warnungstext.text = String.format(
                            getString(R.string.verbrauchsrechner_zeitueberschreitung) + " %.1f h",
                            volllastzeit
                        )
                    } else {
                        warnungstext.text = null
                    }
                    if (standbystromverbrauch != null && standbydauer != null) {
                        verbrauch +=
                            ((standbystromverbrauch * standbydauer) * wattToKiloWatt) * jahrTag
                        euro = verbrauch * neustrompreis * centToEuro
                        if ((volllastzeit + standbydauer) > tagStunden) {
                            warnungstext.text = String
                                .format(
                                    getString(R.string.verbrauchsrechner_zeitueberschreitung) +
                                            " %.1f h",
                                    (volllastzeit + standbydauer)
                                )
                        } else {
                            warnungstext.text = null
                        }
                    }
                    verbrauchprojahr.text = String
                        .format(
                            "%.2f" + getString(R.string.verbrauchsrechner_kwhprojahr),
                            verbrauch
                        )
                    kostenprojahr.text = String
                        .format("%.2f" + getString(R.string.verbrauchsrechner_euro_pro_jahr), euro)
                } else {
                    verbrauchprojahr.text = null
                    kostenprojahr.text = null
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        return edit
    }
}
