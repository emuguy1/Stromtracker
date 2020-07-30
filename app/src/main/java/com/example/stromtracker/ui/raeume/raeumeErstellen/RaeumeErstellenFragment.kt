package com.example.stromtracker.ui.raeume.raeumeErstellen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stromtracker.R
import com.example.stromtracker.database.Raum
import com.example.stromtracker.ui.SharedViewModel
import com.example.stromtracker.ui.raeume.RaeumeFragment

class RaeumeErstellenFragment(private val currHaushaltid: Int) : Fragment() {
    private lateinit var sharedViewModel:SharedViewModel
    private lateinit var raumnameneditfeld: EditText
    private lateinit var savebutton: Button
    private lateinit var informationfield: TextView
    private var raumnameleer: String =getString(R.string.raum_name_leer)
    private var raumnamesonstiges: String = getString(R.string.raum_sonstiges_erstellen)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // sharedViewModel um auf das gemeinsame ViewModel und die DB zuzugreifen
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val root = inflater.inflate(
            R.layout.fragment_raeumeerstellen,
            container,
            false
        )
        raumnameneditfeld = root.findViewById(R.id.edit_text_raum_erstellen_name)
        customTextListener(raumnameneditfeld)
        informationfield = root.findViewById(R.id.text_view_raum_erstellen_info)
        // Speicher Button zum speichern der eingegebenen Daten
        // finde den save button
        savebutton = root.findViewById(R.id.button_raeume_erstellen_speichern)
        // Click listener setzen
        savebutton.setOnClickListener { view ->
            if (view != null) {
                if (!raumnameneditfeld.text.isNullOrBlank()) {
                    // Die Daten in die RoomDatabase speichern
                    val raum =
                        Raum(raumnameneditfeld.text.toString(), currHaushaltid)
                    sharedViewModel.insertRaeume(raum)
                    // neues Fragment erstellen auf das weitergeleitet werden soll
                    val frag = RaeumeFragment()
                    // Fragment Manager aus Main Activity holen
                    val fragMan = parentFragmentManager
                    // Ftagment container aus content_main.xml muss ausgeählt werden,
                    // dann mit neuen Fragment ersetzen, dass oben erstellt wurde
                    fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).commit()
                    // und anschließend noch ein commit()
                } else {
                    informationfield.text = raumnameleer
                }
            }
        }

        // Das gleiche noch für den Abbrechen Button,
        // wobei hier einfach zurück gesprungen werden kann ohne etwas zu machen,
        // da wir ja das ganze nicht speichern wollen
        // finde den abbrechen button
        val abortbutton: View = root.findViewById(R.id.button_raeume_erstellen_abbrechen)
        // Click listener setzen
        abortbutton.setOnClickListener { view ->
            if (view != null) {
                // neues Fragment erstellen auf das weitergeleitet werden soll
                val frag = RaeumeFragment()
                // Fragment Manager aus Main Activity holen
                val fragMan = parentFragmentManager
                // Ftagment container aus content_main.xml muss ausgeählt werden,
                // dann mit neuen Fragment ersetzen, dass oben erstellt wurde
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).commit()
                // und anschließend noch ein commit()
            }
        }

        return root
    }

    private fun customTextListener(edit: EditText): EditText {
        edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (edit.text.toString() == "Sonstiges") {
                    savebutton.visibility = View.INVISIBLE
                    informationfield.text = raumnamesonstiges
                } else {
                    savebutton.visibility = View.VISIBLE
                    informationfield.text = ""
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        return edit
    }
}
