package com.example.stromtracker.ui.haushalt.haushalteBearbeiten_Loeschen

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stromtracker.R
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.ui.haushalt.HaushaltFragment
import com.example.stromtracker.ui.haushalt.HaushaltViewModel
import java.text.SimpleDateFormat
import java.util.*

class HaushaltBearbeitenLoeschenFragment(private var currHaushalt: Haushalt) : Fragment() {
    private lateinit var haushaltViewModel: HaushaltViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        haushaltViewModel =
            ViewModelProvider(this).get(HaushaltViewModel::class.java)
        val root =
            inflater.inflate(R.layout.fragment_haushalt_bearbeiten_loeschen, container, false)

        //Die einzelnen Felder finden:
        val haushaltsnameneditfeld =
            root.findViewById<EditText>(R.id.edit_text_haushalt_bearbeiten_name)
        val strompreiseditfeld =
            root.findViewById<EditText>(R.id.edit_text_haushalt_bearbeiten_strompreis)
        val personeneditfeld =
            root.findViewById<EditText>(R.id.edit_text_haushalt_bearbeiten_anzahl_personen)
        val zaehlerstandeditfeld =
            root.findViewById<EditText>(R.id.edit_text_haushalt_bearbeiten_zaehlerstand)
        val datumeditfeld = root.findViewById<EditText>(R.id.edit_text_haushalt_bearbeiten_datum)
        val oekomixeditfeld =
            root.findViewById<CheckBox>(R.id.check_box_haushalt_bearbeiten_oekostrom)

        //Die Daten aus der RoomDatabse holen und in die Felder schreiben
        haushaltsnameneditfeld.setText(currHaushalt.getName())
        strompreiseditfeld.setText(currHaushalt.getStromkosten().toString())
        personeneditfeld.setText(currHaushalt.getBewohnerAnzahl().toString())
        if (currHaushalt.getZaehlerstand() != null && currHaushalt.getDatum() != null) {
            zaehlerstandeditfeld.setText(currHaushalt.getZaehlerstand().toString())
            datumeditfeld.setText(
                SimpleDateFormat(
                    "dd.MM.yyyy",
                    Locale.GERMAN
                ).format(currHaushalt.getDatum())
            )
        }
        oekomixeditfeld.setChecked(currHaushalt.getOekostrom())

        //Speicher Button zum speichern der eingegebenen Daten
        //finde den save button
        val savebutton: View = root.findViewById(R.id.button_haushalt_bearbeiten_speichern)
        //Click listener setzen
        savebutton.setOnClickListener { view ->
            if (view != null) {
                //Schauen, dass alle Werte die gesetzt sein müssen gesetzt wurden
                if (haushaltsnameneditfeld.text.isNotEmpty() &&
                    personeneditfeld.text.isNotEmpty() &&
                    strompreiseditfeld.text.isNotEmpty()
                ) {

                    //Die Daten in die RoomDatabase speichern
                    currHaushalt.setName(haushaltsnameneditfeld.text.toString())
                    currHaushalt.setBewohnerAnzahl(personeneditfeld.text.toString().toInt())
                    currHaushalt.setStromkosten(strompreiseditfeld.text.toString().toDouble())
                    currHaushalt.setOekostrom(oekomixeditfeld.isChecked)
                    if (datumeditfeld.text.isNotEmpty() && zaehlerstandeditfeld.text.isNotEmpty()) {
                        currHaushalt.setZaehlerstand(
                            zaehlerstandeditfeld.text.toString().toDouble()
                        )
                        // Datum einfügen
                        val tempDate = SimpleDateFormat(
                            "dd.MM.yyyy",
                            Locale.GERMAN
                        ).parse(datumeditfeld.text.toString())
                        currHaushalt.setDatum(tempDate)
                    }
                    haushaltViewModel.updateHaushalt(currHaushalt)
                    //neues Fragment erstellen auf das weitergeleitet werden soll
                    val frag = HaushaltFragment()
                    //Fragment Manager aus Main Activity holen
                    val fragMan = parentFragmentManager
                    //Ftagment container aus content_main.xml muss ausgeählt werden, dann mit neuen Fragment ersetzen, dass oben erstellt wurde
                    fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                        .addToBackStack(null).commit();
                    //und anschließend noch ein commit()
                } else {
                    Toast.makeText(this.context, R.string.leereFelderHaushalt, Toast.LENGTH_SHORT)
                        .show()

                }
            }
        }
        //Das gleiche noch für den Abbrechen Button, wobei hier einfach zurück gesprungen werden kann ohne etwas zu machen, da wir ja das ganze nicht speichern wollen
        //finde den abbrechen button
        val abortbutton: View = root.findViewById(R.id.button_haushalt_bearbeiten_abbrechen)
        //Click listener setzen
        abortbutton.setOnClickListener { view ->
            if (view != null) {
                //neues Fragment erstellen auf das weitergeleitet werden soll
                val frag = HaushaltFragment()
                //Fragment Manager aus Main Activity holen
                val fragMan = parentFragmentManager
                //Ftagment container aus content_main.xml muss ausgeählt werden, dann mit neuen Fragment ersetzen, dass oben erstellt wurde
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).commit();
                //und anschließend noch ein commit()
            }
        }
        //Delete Button zum löschen des ausgewählten Haushalts
        //finde den löschen button
        val deletebutton: View = root.findViewById(R.id.button_haushalt_bearbeiten_loeschen)
        //Click listener setzen
        deletebutton.setOnClickListener { view ->
            if (view != null) {
                //Bestätigungsdialog mithilfe von AlertDialog
                val builder1: AlertDialog.Builder = AlertDialog.Builder(context)
                builder1.setMessage(R.string.haushaltlöschenConfirm)
                builder1.setPositiveButton(
                    R.string.ja,
                    DialogInterface.OnClickListener { dialog, _ ->
                        //Daten werden aus der Datenbank gelöscht
                        haushaltViewModel.deleteHaushalt(currHaushalt)
                        //Man wir nur weitergeleitet, wenn man wirkllich löschen will. Deswegen nur bei positiv der Fragmentwechsel.
                        //neues Fragment erstellen auf das weitergeleitet werden soll
                        val frag = HaushaltFragment()
                        //Fragment Manager aus Main Activity holen
                        val fragMan = parentFragmentManager
                        //Ftagment container aus content_main.xml muss ausgeählt werden, dann mit neuen Fragment ersetzen, dass oben erstellt wurde
                        fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).commit();
                        //und anschließend noch ein commit()
                        dialog.cancel()
                    })
                builder1.setNegativeButton(
                    R.string.nein,
                    DialogInterface.OnClickListener { dialog, _ -> dialog.cancel() })
                val alert11: AlertDialog = builder1.create()
                alert11.show()
            }
        }
        return root
    }
}
