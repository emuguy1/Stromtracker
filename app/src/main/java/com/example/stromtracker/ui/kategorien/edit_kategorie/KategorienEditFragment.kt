package com.example.stromtracker.ui.kategorien.edit_kategorie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R
import com.example.stromtracker.ui.kategorien.KategorienFragment
import com.example.stromtracker.ui.kategorien.SimpleImageArrayAdapter
import com.example.stromtracker.ui.kategorien.new_kategorie.KategorienNewFragment
import com.example.stromtracker.ui.kategorien.new_kategorie.KategorienNewViewModel
import java.util.*

class KategorienEditFragment : Fragment(), View.OnClickListener{
    private lateinit var katViewModel: KategorienNewViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        katViewModel =
            ViewModelProviders.of(this).get(KategorienNewViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_kategorien_edit, container, false)

        val icons = arrayOf<Int>(R.drawable.ic_monitor, R.drawable.ic_refrigerator)
        val spinner: Spinner = root.findViewById(R.id.kategorie_edit_icon_spinner)

        val adapter =
            SimpleImageArrayAdapter(
                requireContext(),
                icons
            )
        adapter.setDropDownViewResource(R.layout.fragment_kategorien_spinner_row)
        spinner.adapter=adapter

        val abbrBtn = root.findViewById<Button>(R.id.kategorie_edit_button_abbrechen)
        abbrBtn.setOnClickListener(this)

        return root
    }

    override fun onClick(v : View) {
        //switch-case in Kotlin: (Zur Unterscheidung der Buttons. Hier eigentlich nicht notwendig)
        when (v.id) {
            R.id.kategorie_edit_button_abbrechen -> {
                Toast.makeText(v.context, "Abbrechen Button clicked.", Toast.LENGTH_SHORT).show()
                //neues Fragment erstellen, Beim Klick soll ja auf die Seite der Kategorien weitergeleitet werden
                val frag = KategorienFragment()
                //Fragment Manager aus Main Activity holen
                val fragMan = parentFragmentManager
                //Wichtig: Hier bei R.id. die Fragment View aus dem content_main.xml auswählen! mit dem neuen Fragment ersetzen und dann committen.
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).commit()
            }
            else -> {
                Toast.makeText(v.context, String.format(Locale.GERMAN,"%d was pressed.", v.id), Toast.LENGTH_SHORT).show()
            }
        }
    }
}