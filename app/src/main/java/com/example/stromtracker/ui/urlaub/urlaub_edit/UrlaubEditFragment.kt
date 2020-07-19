package com.example.stromtracker.ui.urlaub.urlaub_edit

import android.app.AlertDialog
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
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.database.Urlaub
import com.example.stromtracker.ui.urlaub.UrlaubCompanion.Companion.centToEuro
import com.example.stromtracker.ui.urlaub.UrlaubFragment
import com.example.stromtracker.ui.urlaub.UrlaubCompanion.Companion.checkDates
import com.example.stromtracker.ui.urlaub.UrlaubCompanion.Companion.dateTimeToDays
import com.example.stromtracker.ui.urlaub.UrlaubViewModel
import java.text.SimpleDateFormat
import java.util.*

class UrlaubEditFragment(private var urlaub: Urlaub, private val currHaushalt: Haushalt) :
    Fragment(), View.OnClickListener {

    private lateinit var urlaubViewModel: UrlaubViewModel
    private lateinit var fragMan: FragmentManager

    private lateinit var name: EditText
    private lateinit var start: EditText
    private lateinit var ende: EditText

    private lateinit var outTage: TextView
    private lateinit var outErsparnis: TextView

    private lateinit var btnAbbrechen: Button
    private lateinit var btnSpeichern: Button
    private lateinit var btnLoeschen: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //TODO viewModel ändern
        urlaubViewModel =
            ViewModelProviders.of(this).get(UrlaubViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_urlaub_edit, container, false)
        fragMan = parentFragmentManager

        outTage = root.findViewById(R.id.urlaub_edit_text_tage)
        outErsparnis = root.findViewById(R.id.urlaub_edit_text_ersparnis)
        name = root.findViewById(R.id.urlaub_edit_edit_name)
        start = root.findViewById(R.id.urlaub_edit_edit_datum_von)
        addCustomTextChangedListener(start)
        ende = root.findViewById(R.id.urlaub_edit_edit_datum_bis)
        addCustomTextChangedListener(ende)

        name.setText(urlaub.getName())
        var tempStr = SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).format(urlaub.getDateVon())
        start.setText(tempStr)
        tempStr = SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).format(urlaub.getDateBis())
        ende.setText(tempStr)

        btnAbbrechen = root.findViewById(R.id.urlaub_edit_button_abbrechen)
        btnAbbrechen.setOnClickListener(this)
        btnSpeichern = root.findViewById(R.id.urlaub_edit_button_speichern)
        btnSpeichern.setOnClickListener(this)
        btnLoeschen = root.findViewById(R.id.urlaub_edit_button_delete)
        btnLoeschen.setOnClickListener(this)

        return root
    }


    private fun addCustomTextChangedListener(edit: EditText): EditText {
        edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (checkDates(start, ende)) {
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
            val diffkWh = urlaub.getGesamtVerbrauch() * countTage
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
            R.id.urlaub_edit_button_abbrechen -> {
                val frag = UrlaubFragment()
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                    .addToBackStack(null).commit()
            }
            R.id.urlaub_edit_button_speichern -> {
                if (name.text.isNotEmpty() && checkDates(start, ende)) {

                    val tempDateStart =
                        SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).parse(start.text.toString())
                    val tempDateEnde =
                        SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).parse(ende.text.toString())
                    if (tempDateStart != null && tempDateEnde != null) {
                        urlaub.setName(name.text.toString())
                        urlaub.setDateVon(tempDateStart)
                        urlaub.setDateBis(tempDateEnde)

                        urlaubViewModel.updateUrlaub(urlaub)

                        val frag = UrlaubFragment()
                        fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
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
            R.id.urlaub_edit_button_delete -> {
                val confirmDeleteBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
                confirmDeleteBuilder.setMessage(R.string.urlaub_edit_LöschenConfirm)
                confirmDeleteBuilder.setPositiveButton(
                    R.string.ja
                ) { dialog, _ ->
                    //Daten werden aus der Datenbank gelöscht
                    urlaubViewModel.deleteUrlaub(urlaub)
                    //Man wir nur weitergeleitet, wenn man wirkllich löschen will. Deswegen nur bei positiv der Fragmentwechsel.
                    //neues Fragment erstellen auf das weitergeleitet werden soll
                    val frag = UrlaubFragment()
                    //Fragment container aus content_main.xml muss ausgeählt werden, dann mit neuen Fragment ersetzen, dass oben erstellt wurde
                    fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                        .addToBackStack(null).commit()
                    //und anschließend noch ein commit()
                    dialog.cancel()
                }

                confirmDeleteBuilder.setNegativeButton(
                    R.string.nein
                ) { dialog, _ -> dialog.cancel() }

                val confirmDeleteDialog: AlertDialog = confirmDeleteBuilder.create()
                confirmDeleteDialog.show()
            }
        }
    }

}
