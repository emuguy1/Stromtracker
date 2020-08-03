package com.example.stromtracker.ui.kategorien.edit_kategorie

import android.app.AlertDialog
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

class KategorienEditFragment(
    private var currKategorie: Kategorie,
    private var katList: ArrayList<Kategorie>,
    private val iconArray: Array<Int>
) : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var currNameEdit: EditText
    private lateinit var infoFeld: TextView
    private var selectedIcon: Int = 0
    private var sonstigesKatID = 0
    private lateinit var abbrBtn: Button
    private lateinit var delBtn: Button
    private lateinit var saveBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedViewModel =
            ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val root = inflater.inflate(
            R.layout.fragment_kategorien_edit,
            container,
            false)
        currNameEdit = root.findViewById<EditText>(R.id.kategorie_edit_edit_name)
        currNameEdit.setText(currKategorie.getName())
        customTextListener(currNameEdit)
        infoFeld = root.findViewById(R.id.kategorie_edit_info)

        val spinner: Spinner = root.findViewById(R.id.kategorie_edit_icon_spinner)

        val adapter =
            SimpleImageArrayAdapter(
                requireContext(),
                iconArray
            )
        adapter.setDropDownViewResource(R.layout.fragment_kategorien_spinner_row)
        spinner.adapter = adapter
        selectedIcon = currKategorie.getIcon()
        spinner.setSelection(selectedIcon)
        spinner.onItemSelectedListener = this

        abbrBtn = root.findViewById<Button>(R.id.kategorie_edit_button_abbrechen)
        abbrBtn.setOnClickListener(this)
        delBtn = root.findViewById<Button>(R.id.kategorie_edit_button_loeschen)
        delBtn.setOnClickListener(this)
        saveBtn = root.findViewById<Button>(R.id.kategorie_edit_button_speichern)
        saveBtn.setOnClickListener(this)

        for (kategorie in katList) {
            if (kategorie.getName() == "Sonstiges") {
                sonstigesKatID = kategorie.getKategorieID()
                break
            }
        }

        // Wenn Raum Sonstiges ist,
        // soll der Name nicht geändert werden können under Raum auch nicht gelöscht werden können.
        if (currKategorie.getName() == "Sonstiges") {
            saveBtn.visibility = View.INVISIBLE
            delBtn.visibility = View.INVISIBLE
        }

        return root
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
            R.id.kategorie_edit_button_abbrechen -> {
                // neues Fragment erstellen,
                // Beim Klick soll ja auf die Seite der Kategorien weitergeleitet werden
                val frag = KategorienFragment()
                // Wichtig: Hier bei R.id. die Fragment View aus dem content_main.xml auswählen!
                // mit dem neuen Fragment ersetzen und dann committen.
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                    .addToBackStack(null).commit()
            }
            R.id.kategorie_edit_button_loeschen -> {
                val confirmDeleteBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
                confirmDeleteBuilder.setMessage(R.string.kategorie_edit_loeschen_confirm)
                confirmDeleteBuilder.setPositiveButton(
                    R.string.ja
                ) { dialog, _ ->
                    // Alle Geräte die der aktuellen Kategorie hinzugefügt sind,
                    // werden dem SonstigeKategorie zugeordnet
                    sharedViewModel.updateGeraeteByKategorieID(
                        currKategorie.getKategorieID(),
                        sonstigesKatID
                    )
                    // Daten werden aus der Datenbank gelöscht
                    sharedViewModel.deleteKategorie(currKategorie)
                    // Man wir nur weitergeleitet, wenn man wirkllich löschen will.
                    // Deswegen nur bei positiv der Fragmentwechsel.
                    // neues Fragment erstellen auf das weitergeleitet werden soll
                    val frag = KategorienFragment()
                    // Fragment container aus content_main.xml muss ausgeählt werden,
                    // dann mit neuen Fragment ersetzen, dass oben erstellt wurde
                    fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                        .addToBackStack(null).commit()
                    // und anschließend noch ein commit()
                    dialog.cancel()
                }

                confirmDeleteBuilder.setNegativeButton(
                    R.string.nein
                ) { dialog, _ -> dialog.cancel() }

                val confirmDeleteDialog: AlertDialog = confirmDeleteBuilder.create()
                confirmDeleteDialog.show()
            }
            R.id.kategorie_edit_button_speichern -> {
                if (currNameEdit.text.isNotEmpty()) {
                    currKategorie.setName(currNameEdit.text.toString())
                    currKategorie.setIcon(selectedIcon)
                    // Daten werden in der DB gespeichert
                    sharedViewModel.updateKategorie(currKategorie)
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
