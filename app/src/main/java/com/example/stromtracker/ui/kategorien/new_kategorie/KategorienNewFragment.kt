package com.example.stromtracker.ui.kategorien.new_kategorie

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
import com.example.stromtracker.database.Kategorie
import com.example.stromtracker.ui.SharedViewModel
import com.example.stromtracker.ui.kategorien.KategorienFragment
import com.example.stromtracker.ui.kategorien.SimpleImageArrayAdapter
import java.util.*

class KategorienNewFragment(private val iconArray: Array<Int>) : Fragment(), View.OnClickListener,
    AdapterView.OnItemSelectedListener {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var inputName: EditText
    private lateinit var infoFeld: TextView
    private var selectedIcon: Int = 0
    private lateinit var abbrBtn: Button
    private lateinit var saveBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_kategorien_new, container, false)

        val spinner: Spinner = root.findViewById(R.id.kategorie_new_icon_spinner)

        val adapter =
            SimpleImageArrayAdapter(
                requireContext(),
                iconArray
            )
        adapter.setDropDownViewResource(R.layout.fragment_kategorien_spinner_row)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this

        // Button Funktion zuweisen
        abbrBtn = root.findViewById<Button>(R.id.kategorie_new_button_abbrechen)
        abbrBtn.setOnClickListener(this)
        saveBtn = root.findViewById<Button>(R.id.kategorie_new_button_speichern)
        saveBtn.setOnClickListener(this)
        inputName = root.findViewById(R.id.kategorie_new_edit_name)
        customTextListener(inputName)
        infoFeld = root.findViewById(R.id.kategorie_new_info)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // View Model zuweisen, benötigt für DB Zugriff
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        selectedIcon = pos
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    override fun onClick(v: View) {
        // Fragment Manager aus Main Activity holen
        val fragMan = parentFragmentManager
        // switch-case in Kotlin: (Zur Unterscheidung der Buttons.)
        when (v.id) {
            R.id.kategorie_new_button_abbrechen -> {
                // neues Fragment erstellen,
                // Beim Klick soll ja auf die Seite der Kategorien weitergeleitet werden
                val frag = KategorienFragment()
                // Wichtig: Hier bei R.id. die Fragment View aus dem content_main.xml auswählen!
                // mit dem neuen Fragment ersetzen und dann committen.
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                    .addToBackStack(null).commit()
            }
            R.id.kategorie_new_button_speichern -> {

                if (inputName.text.isNotEmpty()) {
                    // Zuerst Daten in DB speichern
                    // Neue Kategorie anlegen, die gleich Insertet wird
                    val newKategorie = Kategorie(inputName.text.toString(), selectedIcon)
                    // In DB speichern
                    sharedViewModel.insertKategorie(newKategorie)
                    // neues Fragment erstellen,
                    // Beim Klick soll ja auf die Seite der Kategorien weitergeleitet werden
                    val frag = KategorienFragment()
                    // Wichtig: Hier bei R.id. die Fragment View aus dem content_main.xml auswählen!
                    // mit dem neuen Fragment ersetzen und dann committen.
                    fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                        .addToBackStack(null).commit()
                } else {
                    Toast.makeText(
                        this.context,
                        R.string.kategorie_new_invalid_value,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else -> {
                Toast.makeText(
                    v.context,
                    String.format(Locale.GERMAN, "%d was pressed.", v.id),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun customTextListener(edit: EditText): EditText {
        edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (edit.text.toString() == "Sonstiges") {
                    saveBtn.visibility = View.INVISIBLE
                    infoFeld.text = getString(R.string.kategorie_edit_info)
                } else {
                    saveBtn.visibility = View.VISIBLE
                    infoFeld.text = ""
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        return edit
    }
}
