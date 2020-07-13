package com.example.stromtracker.ui.raeume.raeumeBearbeiten_Loeschen

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R
import com.example.stromtracker.database.Raum
import com.example.stromtracker.ui.raeume.RaeumeFragment
import com.example.stromtracker.ui.raeume.RaeumeViewModel


//deklariert Raeumefragment als Unterklasse von Fragment
class RaeumeBearbeitenLoeschenFragment(private var currRaum: Raum): Fragment() {
    private lateinit var raeumeViewModel: RaeumeViewModel
    private lateinit var savebutton:View
    private lateinit var abortbutton:View
    private lateinit var deletebutton:View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        raeumeViewModel =
            ViewModelProviders.of(this).get(RaeumeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_raeume_bearbeiten_loeschen, container, false)//false weil es nur teil des root ist, aber nicht selber die root
        //finde die buttons
        savebutton = root.findViewById(R.id.button_raeume_bearbeiten_speichern)
        abortbutton = root.findViewById(R.id.button_raeume_bearbeiten_abbrechen)
        deletebutton = root.findViewById(R.id.button_raeume_bearbeiten_loeschen)

        // Die Daten aus der RoomDatabse holen und in die Felder schreiben
        val raumnameneditfeld=root.findViewById<EditText>(R.id.edit_text_raum_bearbeiten_name)
        raumnameneditfeld.setText(currRaum.getName())

        if(currRaum.getName()=="Sonstiges"){
            savebutton.visibility=View.INVISIBLE
            deletebutton.visibility=View.INVISIBLE
        }
        //Speicher Button zum speichern der eingegebenen Daten

        //Click listener setzen
        savebutton.setOnClickListener { view ->
            if (view != null) {
                //Die Daten in die Roomdatabase speichern
                currRaum.setName(raumnameneditfeld.text.toString())
                raeumeViewModel.updateRaeume(currRaum)
                //neues Fragment erstellen auf das weitergeleitet werden soll
                val frag = RaeumeFragment()
                //Fragment Manager aus Main Activity holen
                val fragMan = parentFragmentManager
                //Ftagment container aus content_main.xml muss ausgeählt werden, dann mit neuen Fragment ersetzen, dass oben erstellt wurde
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).addToBackStack(null).commit();
                //und anschließend noch ein commit()

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
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).commit();
                //und anschließend noch ein commit()

            }

        }
        //Delete Button zum löschen des Raums
        //Click listener setzen
        deletebutton.setOnClickListener { view ->
            if (view != null) {
                //Bestätigungsdialog mithilfe von AlertDialog
                val builder1: AlertDialog.Builder = AlertDialog.Builder(context)
                builder1.setMessage(R.string.raumlöschenConfirm )
                builder1.setPositiveButton(
                    R.string.ja,
                    DialogInterface.OnClickListener { dialog, id ->
                        //Daten werden aus der Datenbank gelöscht
                        //Daten aus Datenbank löschen
                        raeumeViewModel.deleteRaeume(currRaum)
                        //Man wir nur weitergeleitet, wenn man wirkllich löschen will. Deswegen nur bei positiv der Fragmentwechsel.
                        //neues Fragment erstellen auf das weitergeleitet werden soll
                        val frag = RaeumeFragment()
                        //Fragment Manager aus Main Activity holen
                        val fragMan = parentFragmentManager
                        //Ftagment container aus content_main.xml muss ausgeählt werden, dann mit neuen Fragment ersetzen, dass oben erstellt wurde
                        fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).commit();
                        //und anschließend noch ein commit()
                        //TODO: get Geräte by Raumname und schiebe enthaltene Geräte in Raum Sonstiges des Haushaltes
                        dialog.cancel() })

                builder1.setNegativeButton(
                    R.string.nein,
                    DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })

                val alert11: AlertDialog = builder1.create()
                alert11.show()


            }

        }

        return root
    }

}