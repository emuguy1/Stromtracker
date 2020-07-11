package com.example.stromtracker.ui.haushalt.haushaltErstellen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.ui.haushalt.HaushaltFragment
import com.example.stromtracker.ui.haushalt.HaushaltViewModel
import java.text.SimpleDateFormat
import java.util.*

class HaushaltErstellenFragment: Fragment() {
    private lateinit var haushaltViewModel: HaushaltViewModel
    private lateinit var newHaushalt:Haushalt

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        haushaltViewModel =
            ViewModelProviders.of(this).get(HaushaltViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_haushalterstellen, container, false)

        //Die einzelnen Felder finden:
        val haushaltsnameneditfeld=root.findViewById<EditText>(R.id.editTextHaushaltsname)
        val strompreiseditfeld=root.findViewById<EditText>(R.id.haushalteditTextStrompreis)
        val personeneditfeld=root.findViewById<EditText>(R.id.haushalteditTextPersonen)
        val zaehlerstandeditfeld=root.findViewById<EditText>(R.id.haushalteditTextZählerstand)
        val datumeditfeld=root.findViewById<EditText>(R.id.haushalteditTextdatum)
        val oekomixeditfeld=root.findViewById<CheckBox>(R.id.haushalteditTextÖkostrommix)

        //Speicher Button zum speichern der eingegebenen Daten
        //finde den save button
        val savebutton: View = root.findViewById(R.id.haushalt_button_speichern)
        //Click listener setzen
        savebutton.setOnClickListener { view ->
            if (view != null) {
                //Überprüfen ob alle Wertte die gesetzt sein müssen gesetzt wurden
                if(haushaltsnameneditfeld.text.isNotEmpty() &&
                    personeneditfeld.text.isNotEmpty() &&
                    strompreiseditfeld.text.isNotEmpty()) {

                    //Haushaltdaten erstellen aus den Feldern und ein Haushalt erstellen
                    val name=haushaltsnameneditfeld.text.toString()
                    val bewohner=personeneditfeld.text.toString().toInt()
                    val stromkosten=strompreiseditfeld.text.toString().toDouble()
                    val oekostrom=oekomixeditfeld.isChecked

                    if (datumeditfeld.text.isNotEmpty() && zaehlerstandeditfeld.text.isNotEmpty()) {
                        val zaehlerstand=zaehlerstandeditfeld.text.toString().toDouble()
                        //Datum einfügen
                        val tempDateDate= SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).parse(datumeditfeld.text.toString())
                        newHaushalt= Haushalt(name,stromkosten,bewohner,zaehlerstand,tempDateDate,oekostrom)
                    }
                    else {
                        newHaushalt =
                            Haushalt(name, stromkosten, bewohner, null, null, oekostrom)
                    }
                    //Haushalt in Room Datenbank speichern
                    haushaltViewModel.insertHaushalt(newHaushalt)
                    //neues Fragment erstellen auf das weitergeleitet werden soll
                    val frag = HaushaltFragment()
                    //Fragment Manager aus Main Activity holen
                    val fragMan = parentFragmentManager
                    //Ftagment container aus content_main.xml muss ausgeählt werden, dann mit neuen Fragment ersetzen, dass oben erstellt wurde
                    fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                        .addToBackStack(null).commit();
                    //und anschließend noch ein commit()
                }
                else{
                    Toast.makeText(this.context, R.string.leereFelderHaushalt, Toast.LENGTH_SHORT).show()
                }
            }
        }

        //Das gleiche noch für den Abbrechen Button, wobei hier einfach zurück gesprungen werden kann ohne etwas zu machen, da wir ja das ganze nicht speichern wollen
        //finde den abbrechen button
        val abortbutton: View = root.findViewById(R.id.haushalt_button_abbrechen)
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
        return root
    }
}
