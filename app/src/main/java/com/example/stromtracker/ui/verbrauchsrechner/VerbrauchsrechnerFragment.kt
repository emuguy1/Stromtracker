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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R
import kotlinx.android.synthetic.*

class VerbrauchsrechnerFragment : Fragment() {

    private lateinit var strompreis: EditText
    private lateinit var leistungsverbrauch: EditText
    private lateinit var standbyverbrauch: EditText
    private lateinit var standbyzeit: EditText
    private lateinit var lastzeit: EditText

    private lateinit var verbrauchprojahr: TextView
    private lateinit var kostenprojahr: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_verbrauchsrechner, container, false)
        strompreis=root.findViewById(R.id.verbrauchsrechner_Strompreispkwh_input)
        leistungsverbrauch=root.findViewById(R.id.verbrauchsrechner_Leistungkwh_input)
        standbyverbrauch=root.findViewById(R.id.verbrauchsrechner_StandByLeistungpkwh_input)
        standbyzeit=root.findViewById(R.id.verbrauchsrechner_StandByLeistungZeitpkwh_input)
        lastzeit=root.findViewById(R.id.verbrauchsrechner_Leistungzeitpkwh_input)

        verbrauchprojahr=root.findViewById(R.id.verbrauchsrechner_erg_verbrauchJahr)
        kostenprojahr=root.findViewById(R.id.verbrauchsrechner_erg_kostenJahr)


        CustomTextListener(strompreis)
        CustomTextListener(leistungsverbrauch)
        CustomTextListener(standbyverbrauch)
        CustomTextListener(standbyzeit)
        CustomTextListener(lastzeit)
        return root
    }
    fun CustomTextListener(edit:EditText) : EditText {
        edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val neustrompreis:Double? = strompreis.text.toString().toDoubleOrNull()
                val lastverbrauch:Double? = leistungsverbrauch.text.toString().toDoubleOrNull()
                val standbystromverbrauch:Double? = standbyverbrauch.text.toString().toDoubleOrNull()
                val volllastzeit:Double? = lastzeit.text.toString().toDoubleOrNull()
                val standbydauer:Double? = standbyzeit.text.toString().toDoubleOrNull()
                var verbrauch:Double
                var euro:Double
                if(neustrompreis != null && lastverbrauch != null && volllastzeit != null) {
                    verbrauch=(((lastverbrauch * volllastzeit)/1000)*365)
                    euro= (((lastverbrauch * volllastzeit)/1000) * neustrompreis/100*365)
                    if (standbystromverbrauch != null && standbydauer != null ) {
                        verbrauch+=((standbystromverbrauch*standbydauer)/1000)*365
                        euro=verbrauch*neustrompreis/100
                    }
                    verbrauchprojahr.text = String.format("%.2f kWh pro Jahr", verbrauch)
                    kostenprojahr.text = String.format("%.2f € pro Jahr", euro)
                }
                else {
                    verbrauchprojahr.text = null
                    kostenprojahr.text = null
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { }
        })
        return edit

    }
}