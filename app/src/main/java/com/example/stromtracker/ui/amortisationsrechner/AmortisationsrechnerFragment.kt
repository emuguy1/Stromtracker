package com.example.stromtracker.ui.amortisationsrechner

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R
import java.math.RoundingMode
import java.text.DecimalFormat

class AmortisationsrechnerFragment : Fragment() {

    private lateinit var amortisationsrechnerViewModel: AmortisationsrechnerViewModel
    private lateinit var editVerbrauchAkt : EditText
    private lateinit var editVerbrauchNeu : EditText
    private lateinit var editAnschaffung : EditText
    private lateinit var editStrompreis : EditText

    private lateinit var outKostenAkt : TextView
    private lateinit var outKostenNeu : TextView
    private lateinit var outAmortdauer : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        amortisationsrechnerViewModel =
            ViewModelProviders.of(this).get(com.example.stromtracker.ui.amortisationsrechner.AmortisationsrechnerViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_amortisationsrechner, container, false)

        editVerbrauchAkt = root.findViewById(R.id.amort_edit_verbrauch_akt)
        editVerbrauchNeu = root.findViewById(R.id.amort_edit_verbrauch_neu)
        editAnschaffung = root.findViewById(R.id.amort_edit_anschaffung)
        editStrompreis = root.findViewById(R.id.amort_edit_strompreis)


        outKostenAkt = root.findViewById(R.id.amort_text_kosten_akt_num)
        outKostenNeu = root.findViewById(R.id.amort_text_kosten_neu_num)
        outAmortdauer = root.findViewById(R.id.amort_text_amortdauer_num)

        addCustomTextChangedListener(editVerbrauchAkt)
        addCustomTextChangedListener(editVerbrauchNeu)
        addCustomTextChangedListener(editAnschaffung)
        addCustomTextChangedListener(editStrompreis)

        return root
    }

    fun addCustomTextChangedListener(edit:EditText) : EditText {
        edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val verbrAkt : Double? = editVerbrauchAkt.text.toString().toDoubleOrNull()
                val verbrNeu : Double? = editVerbrauchNeu.text.toString().toDoubleOrNull()
                val AK : Double? = editAnschaffung.text.toString().toDoubleOrNull()
                val stromkosten : Double? = editStrompreis.text.toString().toDoubleOrNull()
                Log.d("AMORTISATION", verbrAkt.toString() + verbrNeu.toString()+ AK.toString()+ stromkosten.toString())
                var neuStr : String
                if(stromkosten != null) {
                    if (verbrAkt != null) {
                        val aktDouble : Double = (verbrAkt * stromkosten / 100)
                        neuStr = String.format("%.2f",aktDouble)+"€ "
                        outKostenAkt.text = neuStr
                    } else {
                        outKostenAkt.text = null
                    }
                    if (verbrNeu != null) {
                        val neuDouble : Double = (verbrNeu * stromkosten / 100)
                        neuStr = String.format("%.2f",neuDouble)+"€ "
                        outKostenNeu.text = neuStr
                    } else {
                        outKostenNeu.text = null
                    }
                    if(verbrAkt != null && verbrNeu != null && AK != null && verbrAkt > verbrNeu) {
                        val amortDouble = (AK/((verbrAkt-verbrNeu) * stromkosten / 100))
                        //Das Jahr wird immer auf ganze Zahlen abgerundet
                        val df = DecimalFormat("#")
                        df.roundingMode = RoundingMode.DOWN
                        neuStr = "Das neue Gerät wird sich innerhalb von "+
                                df.format(amortDouble)+" Jahren und "+
                                String.format("%.1f", (amortDouble.rem(1)*365))+" Tagen amortisieren. Danach sparen sie "+
                                String.format("%.2f", (verbrAkt-verbrNeu)*stromkosten/100)+ "€ Stromkosten pro Jahr."
                        outAmortdauer.text = neuStr
                    }
                    else
                        outAmortdauer.text = null
                }
                else {
                    outKostenAkt.text = null
                    outKostenNeu.text = null
                    outAmortdauer.text = null
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { }
        })
        return edit
    }

}