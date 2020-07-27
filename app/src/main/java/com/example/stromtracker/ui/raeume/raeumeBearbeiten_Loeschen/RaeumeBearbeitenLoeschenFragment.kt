package com.example.stromtracker.ui.raeume.raeumeBearbeiten_Loeschen

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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stromtracker.R
import com.example.stromtracker.database.Raum
import com.example.stromtracker.ui.raeume.RaeumeFragment
import com.example.stromtracker.ui.raeume.RaeumeViewModel


//deklariert Raeumefragment als Unterklasse von Fragment
class RaeumeBearbeitenLoeschenFragment(
    private var currRaum: Raum,
    private var raumliste : ArrayList<Raum>
) : Fragment() {
    private lateinit var raeumeViewModel: RaeumeViewModel
    private lateinit var savebutton: Button
    private lateinit var abortbutton: Button
    private lateinit var deletebutton: Button
    private lateinit var informationfield: TextView
    private lateinit var raumnameneditfeld: EditText
    private var raumnameleer: String =
        "Raum kann nicht gespeichert werden, da Kein Name eingegeben wurde."
    private var raumnamesonstiges: String = "Raum - Sonstiges - darf nicht erstellt werden!"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        raeumeViewModel =
            ViewModelProvider(this).get(RaeumeViewModel::class.java)
        val root = inflater.inflate(
            R.layout.fragment_raeume_bearbeiten_loeschen,
            container,
            false
        )//false weil es nur teil des root ist, aber nicht selber die root
        //finde die buttons
        savebutton = root.findViewById(R.id.button_raeume_bearbeiten_speichern)
        abortbutton = root.findViewById(R.id.button_raeume_bearbeiten_abbrechen)
        deletebutton = root.findViewById(R.id.button_raeume_bearbeiten_loeschen)

        //Sachen holen, um die Überprüfung des Textes zu machen
        raumnameneditfeld = root.findViewById(R.id.edit_text_raum_bearbeiten_name)
        customTextListener(raumnameneditfeld)
        informationfield=root.findViewById(R.id.text_view_raum_bearbeiten_info)

        //Holen der Raumliste, um den Raum Sonstiges zu finden, um alle Geräte,
        // die im aktuellen Raum entalten sind, der gelöscht werden soll und deren Raum ID auf den
        // Sonstigen Raum zu ändern
        var sonstigesraumid = 0
        for (raeume in raumliste) {
            if (raeume.getName() == "Sonstiges") {
                sonstigesraumid = raeume.getRaumID()
                break
            }
        }

        // Die Daten aus der RoomDatabse holen und in das Feld befüllen
        val raumnameneditfeld = root.findViewById<EditText>(R.id.edit_text_raum_bearbeiten_name)
        raumnameneditfeld.setText(currRaum.getName())

        //Wenn Raum Sonstiges ist, soll der Name nicht geändert werden können under Raum auch nicht gelöscht werden können.
        if (currRaum.getName() == "Sonstiges") {
            savebutton.visibility = View.INVISIBLE
            deletebutton.visibility = View.INVISIBLE
        }

        //Buttons
        //Speicher Button zum speichern der eingegebenen Daten

        //Click listener setzen
        savebutton.setOnClickListener { view ->
            if (view != null) {
                //Die Daten in die Roomdatabase speichern
                if(!raumnameneditfeld.text.toString().isBlank()){
                    currRaum.setName(raumnameneditfeld.text.toString())
                    raeumeViewModel.updateRaeume(currRaum)
                    //neues Fragment erstellen auf das weitergeleitet werden soll
                    val frag = RaeumeFragment()
                    //Fragment Manager aus Main Activity holen
                    val fragMan = parentFragmentManager
                    //Ftagment container aus content_main.xml muss ausgeählt werden, dann mit neuen Fragment ersetzen, dass oben erstellt wurde
                    fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                        .addToBackStack(null).commit()
                    //und anschließend noch ein commit()
                }
                else{
                    informationfield.text=raumnameleer
                }
            }
        }
        //Das gleiche noch für den Abbrechen Button, wobei hier einfach zurück gesprungen werden kann ohne etwas zu machen, da wir ja das ganze nicht speichern wollen
        //Click listener setzen
        abortbutton.setOnClickListener { view ->
            if (view != null) {
                //neues Fragment erstellen auf das weitergeleitet werden soll
                val frag = RaeumeFragment()
                //Fragment Manager aus Main Activity holen
                val fragMan = parentFragmentManager
                //Ftagment container aus content_main.xml muss ausgeählt werden, dann mit neuen Fragment ersetzen, dass oben erstellt wurde
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).commit()
                //und anschließend noch ein commit()

            }
        }
        //Delete Button zum löschen des Raums
        //Click listener setzen
        deletebutton.setOnClickListener { view ->
            if (view != null) {
                //Bestätigungsdialog mithilfe von AlertDialog
                val builder1: AlertDialog.Builder = AlertDialog.Builder(context)
                builder1.setMessage(R.string.raumlöschen_confirm)
                builder1.setPositiveButton(
                    R.string.ja
                ) { dialog, _ ->
                    //Alle Geräte die dem aktuellen Raum hinzugefügt sind, werden dem Sonstigeraum zugeordnet
                    raeumeViewModel.updateGeraeteByRaumId(currRaum.getRaumID(),sonstigesraumid)
                    //Daten werden aus der Datenbank gelöscht
                    //Daten aus Datenbank löschen
                    raeumeViewModel.deleteRaeume(currRaum)
                    //Man wir nur weitergeleitet, wenn man wirkllich löschen will. Deswegen nur bei positiv der Fragmentwechsel.
                    //neues Fragment erstellen auf das weitergeleitet werden soll
                    val frag = RaeumeFragment()
                    //Fragment Manager aus Main Activity holen
                    val fragMan = parentFragmentManager
                    //Ftagment container aus content_main.xml muss ausgeählt werden, dann mit neuen Fragment ersetzen, dass oben erstellt wurde
                    fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).commit()
                    //und anschließend noch ein commit()
                    dialog.cancel()
                }

                builder1.setNegativeButton(
                    R.string.nein
                ) { dialog, _ -> dialog.cancel() }

                val alert11: AlertDialog = builder1.create()
                alert11.show()
            }

        }
        return root
    }

    private fun customTextListener(edit: EditText): EditText {
        edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (edit.text.toString() == "Sonstiges") {
                    savebutton.visibility = View.INVISIBLE
                    informationfield.text=raumnamesonstiges
                }
                else {
                    savebutton.visibility = View.VISIBLE
                    informationfield.text=""
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        return edit
    }

}
