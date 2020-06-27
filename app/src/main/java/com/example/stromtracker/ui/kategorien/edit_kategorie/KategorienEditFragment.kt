package com.example.stromtracker.ui.kategorien.edit_kategorie

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R
import com.example.stromtracker.ui.kategorien.KategorienFragment
import com.example.stromtracker.ui.kategorien.KategorienViewModel
import com.example.stromtracker.ui.kategorien.SimpleImageArrayAdapter
import java.util.*

class KategorienEditFragment(curr :TextView) : Fragment(), View.OnClickListener{
    private lateinit var katViewModel: KategorienViewModel
    var currText = curr.text
    lateinit var currName : EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        katViewModel =
            ViewModelProviders.of(this).get(KategorienViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_kategorien_edit, container, false)
        currName = root.findViewById<EditText>(R.id.kategorie_edit_editName)
        currName.setText(currText)

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
        val delBtn = root.findViewById<Button>(R.id.kategorie_edit_button_loeschen)
        delBtn.setOnClickListener(this)
        val saveBtn = root.findViewById<Button>(R.id.kategorie_edit_button_speichern)
        saveBtn.setOnClickListener(this)

        return root
    }

    override fun onClick(v : View) {
        //Fragment Manager aus Main Activity holen
        val fragMan = parentFragmentManager
        //switch-case in Kotlin: (Zur Unterscheidung der Buttons.)
        when (v.id) {
            R.id.kategorie_edit_button_abbrechen -> {
                //neues Fragment erstellen, Beim Klick soll ja auf die Seite der Kategorien weitergeleitet werden
                val frag = KategorienFragment()
                //Wichtig: Hier bei R.id. die Fragment View aus dem content_main.xml auswählen! mit dem neuen Fragment ersetzen und dann committen.
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).addToBackStack(null).commit()
            }
            R.id.kategorie_edit_button_loeschen -> {
                val confirmDeleteBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
                confirmDeleteBuilder.setMessage(R.string.kategorie_edit_LöschenConfirm)
                confirmDeleteBuilder.setPositiveButton(
                    R.string.ja,
                    DialogInterface.OnClickListener { dialog, id ->
                        //Daten werden aus der Datenbank gelöscht
                        //TODO: Daten aus Datenbank löschen
                        //Man wir nur weitergeleitet, wenn man wirkllich löschen will. Deswegen nur bei positiv der Fragmentwechsel.
                        //neues Fragment erstellen auf das weitergeleitet werden soll
                        val frag = KategorienFragment()
                        //Fragment container aus content_main.xml muss ausgeählt werden, dann mit neuen Fragment ersetzen, dass oben erstellt wurde
                        fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).addToBackStack(null).commit();
                        //und anschließend noch ein commit()
                        dialog.cancel() })

                confirmDeleteBuilder.setNegativeButton(
                    R.string.nein,
                    DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })

                val confirmDeleteDialog: AlertDialog = confirmDeleteBuilder.create()
                confirmDeleteDialog.show()
            }
            R.id.kategorie_edit_button_speichern -> {
                //neues Fragment erstellen, Beim Klick soll ja auf die Seite der Kategorien weitergeleitet werden
                val frag = KategorienFragment()
                //Wichtig: Hier bei R.id. die Fragment View aus dem content_main.xml auswählen! mit dem neuen Fragment ersetzen und dann committen.
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).addToBackStack(null).commit()
            }
            else -> {
                Toast.makeText(v.context, String.format(Locale.GERMAN,"%d was pressed.", v.id), Toast.LENGTH_SHORT).show()
            }
        }
    }
}