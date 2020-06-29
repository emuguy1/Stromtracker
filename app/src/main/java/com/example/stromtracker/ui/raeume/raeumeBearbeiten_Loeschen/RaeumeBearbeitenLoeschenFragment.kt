package com.example.stromtracker.ui.raeume.raeumeBearbeiten_Loeschen

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.R
import com.example.stromtracker.ui.raeume.RaeumeFragment
import com.example.stromtracker.ui.raeume.raeumeErstellen.RaeumeErstellenFragment


//deklariert Raeumefragment als Unterklasse von Fragment
class RaeumeBearbeitenLoeschenFragment: Fragment() {
    private lateinit var raeumeblViewModel: RaeumeBearbeitenLoeschenViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        raeumeblViewModel =
            ViewModelProviders.of(this).get(RaeumeBearbeitenLoeschenViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_raeume_bearbeiten_loeschen, container, false)//false weil es nur teil des root ist, aber nicht selber die root
        //TODO: Die Daten aus der RoomDatabse holen und in die Felder schreiben


        //TODO: Umschreiben auf Raeume
        //Speicher Button zum speichern der eingegebenen Daten
        //finde den save button
        val savebutton: View = root.findViewById(R.id.raeume_new_button_speichern)
        //Click listener setzen
        savebutton.setOnClickListener { view ->
            if (view != null) {
                //TODO: Die Daten in die RoomDatabase speichern

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
        //finde den abbrechen button
        val abortbutton: View = root.findViewById(R.id.raeume_new_button_abbrechen)
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
        // löschen button
        val deletebutton: View = root.findViewById(R.id.raeume_new_button_Loeschen)
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
                        //TODO: Daten aus Datenbank löschen
                        //Man wir nur weitergeleitet, wenn man wirkllich löschen will. Deswegen nur bei positiv der Fragmentwechsel.
                        //neues Fragment erstellen auf das weitergeleitet werden soll
                        val frag = RaeumeFragment()
                        //Fragment Manager aus Main Activity holen
                        val fragMan = parentFragmentManager
                        //Ftagment container aus content_main.xml muss ausgeählt werden, dann mit neuen Fragment ersetzen, dass oben erstellt wurde
                        fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).commit();
                        //und anschließend noch ein commit()
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