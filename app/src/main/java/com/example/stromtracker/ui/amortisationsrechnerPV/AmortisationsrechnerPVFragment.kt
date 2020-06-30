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
import com.example.stromtracker.R
import java.math.RoundingMode
import java.text.DecimalFormat


class AmortisationsrechnerPVFragment : Fragment() {

    private lateinit var editLeistung: EditText
    private lateinit var editAK: EditText
    private lateinit var editErtrag : EditText
    private lateinit var editVerguetung : EditText
    private lateinit var editEigenverbrauch : EditText
    private lateinit var editPreisKwh : EditText

    private lateinit var outJahresertragkWh: TextView
    private lateinit var outJahresertragEuro: TextView
    private lateinit var outAmortdauer: TextView
    private lateinit var outAmortersparnis: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_amortisationsrechnerpv, container, false)

        editLeistung = root.findViewById(R.id.amortPV_edit_leistung)
        editAK = root.findViewById(R.id.amortPV_edit_anschaffung)
        editErtrag = root.findViewById(R.id.amortPV_edit_ertrag)
        editVerguetung = root.findViewById(R.id.amortPV_edit_vergütung)
        editEigenverbrauch = root.findViewById(R.id.amortPV_edit_eigenverbrauch)
        editPreisKwh = root.findViewById(R.id.amortPV_edit_preis_kwh)

        outJahresertragkWh = root.findViewById(R.id.amortPV_text_jahresertragkWh_zahl)
        outJahresertragEuro= root.findViewById(R.id.amortPV_text_JahresertragEuro_zahl)
        outAmortdauer = root.findViewById(R.id.amortPV_text_amortdauer_zahl)
        outAmortersparnis = root.findViewById(R.id.amortPV_text_amort_ersparnis)

        addCustomTextChangedListener(editLeistung)
        addCustomTextChangedListener(editAK)
        addCustomTextChangedListener(editErtrag)
        addCustomTextChangedListener(editVerguetung)
        addCustomTextChangedListener(editEigenverbrauch)
        addCustomTextChangedListener(editPreisKwh)

        return root
    }

    fun addCustomTextChangedListener(edit:EditText) : EditText {
        edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val AK:Int? = editAK.text.toString().toIntOrNull()
                val leistung:Double? = editLeistung.text.toString().toDoubleOrNull()
                val ertrag:Int? = editErtrag.text.toString().toIntOrNull()
                val vergütung:Double? = editVerguetung.text.toString().toDoubleOrNull()
                val eigenverbrauch:Int? = editEigenverbrauch.text.toString().toIntOrNull()
                val preisKwh:Double? = editPreisKwh.text.toString().toDoubleOrNull()

                var outStr : String
                if(leistung != null && ertrag != null && vergütung != null) {
                    outStr = (leistung * ertrag).toInt().toString() + " kWh"
                    outJahresertragkWh.text = outStr

                    if (AK != null && eigenverbrauch != null && preisKwh != null) {
                        val JEeuro : Double = ((leistung * ertrag * vergütung/100 * (1-eigenverbrauch/100)) + (leistung * ertrag * preisKwh/100 * eigenverbrauch/100))
                        outStr = String.format("%.2f", JEeuro)+" €"
                        outJahresertragEuro.text = outStr

                        val amortDouble = (AK / JEeuro)
                        //Das Jahr wird immer auf ganze Zahlen abgerundet
                        val df = DecimalFormat("#")
                        df.roundingMode = RoundingMode.DOWN
                        outStr = df.format(amortDouble) + " Jahre und " + String.format("%.1f", (amortDouble.rem(1)*365)) + " Tage bis zur Amortisation"
                        outAmortdauer.text = outStr
                        outStr = "Danach: " + JEeuro.toString() + "€ Ersparnis im Jahr."
                        outAmortersparnis.text = outStr
                    }
                    else {
                        outJahresertragEuro.text = null
                        outAmortdauer.text = null
                    }
                }
                else {
                    outAmortdauer.text = null
                    outJahresertragkWh.text = null
                    outJahresertragEuro.text = null
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { }
        })
        return edit
    }
}