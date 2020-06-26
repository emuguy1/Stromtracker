package com.example.stromtracker.ui.amortisationsrechnerPV
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R


class AmortisationsrechnerPVFragment : Fragment() {

    private lateinit var amortisationsrechnerPVViewModel: AmortisationsrechnerPVViewModel
    private lateinit var editLeistung: EditText
    private lateinit var editAK: EditText
    private lateinit var outJahresertragkWh: TextView
    private lateinit var outJahresertragEuro: TextView
    private lateinit var outAmort: TextView
    private val durchschnittErtragDE = 910 //kWh pro Jahr pro kWp
    private val durchschnittVerguetungDE : Double = 0.0903 // Ct/kWh Ab 01.07.2020 bis 10kWP

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        amortisationsrechnerPVViewModel =
            ViewModelProviders.of(this).get(com.example.stromtracker.ui.amortisationsrechnerPV.AmortisationsrechnerPVViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_amortisationsrechnerpv, container, false)

        editLeistung = root.findViewById(R.id.amortPV_edit_leistung)
        editAK = root.findViewById(R.id.amortPV_edit_anschaffung)
        outJahresertragkWh = root.findViewById(R.id.amortPV_text_jahresertragkWh_zahl)
        outJahresertragEuro= root.findViewById(R.id.amortPV_text_JahresertragEuro_zahl)
        outAmort = root.findViewById(R.id.amortPV_text_amortdauer_zahl)

        //Hier wird der Jahresertrag in kWh jedes mal berechnet, wenn sich der Text des Leistungsinputs ändert
        editLeistung.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val leistung:Double? = editLeistung.text.toString().toDoubleOrNull()
                if(leistung != null) {
                    outJahresertragkWh.text = (leistung * durchschnittErtragDE).toInt().toString()
                    val JEeuro : Double = (leistung * durchschnittErtragDE * durchschnittVerguetungDE)
                    outJahresertragEuro.text = String.format("%.2f", JEeuro)
                }
                else {
                    outJahresertragkWh.text = null
                    outJahresertragEuro.text = null
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { }
        })

        editAK.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val AK:Int? = editAK.text.toString().toIntOrNull()
                val leistung:Double? = editLeistung.text.toString().toDoubleOrNull()
                if(AK != null)
                    if (leistung != null) {
                        val amortDouble = (AK / (leistung * durchschnittErtragDE * durchschnittVerguetungDE))
                        //Ausgabe mit 3 Nachkommastellen
                        outAmort.text = String.format("%.3f", amortDouble)
                    }
                else
                    outAmort.text = null
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { }
        })

        return root
    }
}