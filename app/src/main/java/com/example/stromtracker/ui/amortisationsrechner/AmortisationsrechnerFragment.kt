package com.example.stromtracker.ui.amortisationsrechner

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
import java.math.RoundingMode
import java.text.DecimalFormat

class AmortisationsrechnerFragment : Fragment() {

    private lateinit var editVerbrauchAkt: EditText
    private lateinit var editVerbrauchNeu: EditText
    private lateinit var editAnschaffung: EditText
    private lateinit var editStrompreis: EditText

    private lateinit var outKostenAkt: TextView
    private lateinit var outKostenNeu: TextView
    private lateinit var outAmortdauer: TextView
    private lateinit var outAmortersparnis: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_amortisationsrechner, container, false)

        editVerbrauchAkt = root.findViewById(R.id.amort_edit_verbrauch_akt)
        editVerbrauchNeu = root.findViewById(R.id.amort_edit_verbrauch_neu)
        editAnschaffung = root.findViewById(R.id.amort_edit_anschaffung)
        editStrompreis = root.findViewById(R.id.amort_edit_strompreis)

        outKostenAkt = root.findViewById(R.id.amort_text_kosten_akt_num)
        outKostenNeu = root.findViewById(R.id.amort_text_kosten_neu_num)
        outAmortdauer = root.findViewById(R.id.amort_text_amortdauer_num)
        outAmortersparnis = root.findViewById(R.id.amort_text_amort_ersparnis_num)

        addCustomTextChangedListener(editVerbrauchAkt)
        addCustomTextChangedListener(editVerbrauchNeu)
        addCustomTextChangedListener(editAnschaffung)
        addCustomTextChangedListener(editStrompreis)

        return root
    }

    fun addCustomTextChangedListener(edit: EditText): EditText {
        edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val verbrAkt: Double? = editVerbrauchAkt.text.toString().toDoubleOrNull()
                val verbrNeu: Double? = editVerbrauchNeu.text.toString().toDoubleOrNull()
                val ak: Double? = editAnschaffung.text.toString().toDoubleOrNull()
                val stromkosten: Double? = editStrompreis.text.toString().toDoubleOrNull()
                updateOutputs(verbrAkt, verbrNeu, ak, stromkosten)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        return edit
    }

    fun updateOutputs(verbrAkt: Double?, verbrNeu: Double?, ak: Double?, stromkosten: Double?) {
        var neuStr: String
        if (stromkosten != null) {
            if (verbrAkt != null) {
                //Setzen der aktuellen Stromkosten pro Jahr
                val aktDouble: Double = berechneStromkosten(stromkosten, verbrAkt)
                neuStr = String.format("%.2f", aktDouble) + "€ "
                outKostenAkt.text = neuStr
            } else {
                outKostenAkt.text = null
            }
            if (verbrNeu != null) {
                //Setzen der neuen Stromkosten pro Jahr
                val neuDouble: Double = berechneStromkosten(stromkosten, verbrNeu)
                neuStr = String.format("%.2f", neuDouble) + "€ "
                outKostenNeu.text = neuStr
            } else {
                outKostenNeu.text = null
            }
            if (verbrAkt != null && verbrNeu != null && ak != null && verbrAkt > verbrNeu) {
                //Ausrechnen der Amortisationsdauer
                val amortDouble = (ak / (berechneStromkosten(stromkosten, (verbrAkt - verbrNeu))))
                //Das Jahr wird immer auf ganze Zahlen abgerundet
                val df = DecimalFormat("#")
                df.roundingMode = RoundingMode.DOWN
                //Ausgabe der Dauer in Jahren und Tagen
                neuStr = df.format(amortDouble) + " Jahre und " + String.format(
                    "%.1f",
                    (amortDouble.rem(1) * 365)
                ) + " Tage bis zur Amortisation."
                outAmortdauer.text = neuStr
                //Ausrechnen der Ersparnis nach Amortisation
                neuStr = "Danach: " + String.format(
                    "%.2f",
                    berechneStromkosten(stromkosten, (verbrAkt - verbrNeu))
                ) + "€ Ersparnis im Jahr."
                outAmortersparnis.text = neuStr
            } else {
                outAmortdauer.text = null
                outAmortersparnis.text = null
            }
        } else {
            outKostenAkt.text = null
            outKostenNeu.text = null
            outAmortdauer.text = null
            outAmortersparnis.text = null
        }
    }

    fun berechneStromkosten(stromkosten: Double, verbrauch: Double): Double {
        return (verbrauch * stromkosten / 100)
    }

}
