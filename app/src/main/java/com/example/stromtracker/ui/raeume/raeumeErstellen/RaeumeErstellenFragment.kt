package com.example.stromtracker.ui.raeume.raeumeErstellen


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.database.Raum
import com.example.stromtracker.ui.raeume.RaeumeFragment
import com.example.stromtracker.ui.raeume.RaeumeViewModel
import com.google.android.material.navigation.NavigationView

class RaeumeErstellenFragment: Fragment() {
    private lateinit var raeumeViewModel: RaeumeViewModel



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        raeumeViewModel =
            ViewModelProviders.of(this).get(RaeumeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_raeumeerstellen, container, false)



        //Speicher Button zum speichern der eingegebenen Daten
        //finde den save button
        val savebutton: View = root.findViewById(R.id.button_raeume_erstellen_speichern)
        //Click listener setzen
        savebutton.setOnClickListener { view ->
            if (view != null) {
                //Die Daten in die RoomDatabase speichern
                val raumnameneditfeld=root.findViewById<EditText>(R.id.edit_text_raum_erstellen_name)

                val navView = requireActivity().findViewById<NavigationView>(R.id.nav_view)

                val sp: Spinner = navView.menu.findItem(R.id.nav_haushalt).actionView as Spinner
                val currHaushalt = sp.selectedItem as Haushalt

                val raum:Raum= Raum(raumnameneditfeld.text.toString(),currHaushalt.getHaushaltID())
                raeumeViewModel.insertRaeume(raum)
                //neues Fragment erstellen auf das weitergeleitet werden soll
                val frag = RaeumeFragment()
                //Fragment Manager aus Main Activity holen
                val fragMan = parentFragmentManager
                //Ftagment container aus content_main.xml muss ausgeählt werden, dann mit neuen Fragment ersetzen, dass oben erstellt wurde
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).commit();
                //und anschließend noch ein commit()

            }

        }

        //Das gleiche noch für den Abbrechen Button, wobei hier einfach zurück gesprungen werden kann ohne etwas zu machen, da wir ja das ganze nicht speichern wollen
        //finde den abbrechen button
        val abortbutton: View = root.findViewById(R.id.button_raeume_erstellen_abbrechen)
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

        return root
    }
}