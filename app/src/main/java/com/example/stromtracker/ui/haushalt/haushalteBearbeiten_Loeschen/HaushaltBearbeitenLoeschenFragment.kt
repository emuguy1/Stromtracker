package com.example.stromtracker.ui.haushalt.haushalteBearbeiten_Loeschen

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.stromtracker.R
import com.example.stromtracker.database.Geraete
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.database.Urlaub
import com.example.stromtracker.ui.haushalt.HaushaltFragment
import com.example.stromtracker.ui.haushalt.HaushaltViewModel
import com.example.stromtracker.ui.urlaub.UrlaubCompanion
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.withSign

class HaushaltBearbeitenLoeschenFragment(
    private var currHaushalt: Haushalt
) : Fragment() {

    private lateinit var verbraucherInHaushalt: ArrayList<Geraete>
    private lateinit var produzentenInHaushalt: ArrayList<Geraete>
    private lateinit var urlaubeInHaushalt: ArrayList<Urlaub>

    private lateinit var haushaltViewModel: HaushaltViewModel

    private lateinit var zaehlerstandAkt: TextView
    private lateinit var datumAkt: TextView
    private lateinit var zaehlerstandNeu: EditText
    private lateinit var datumNeu: EditText
    private lateinit var auswertung: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        haushaltViewModel =
            ViewModelProvider(this).get(HaushaltViewModel::class.java)
        val root =
            inflater.inflate(R.layout.fragment_haushalt_bearbeiten_loeschen, container, false)

        verbraucherInHaushalt = ArrayList()
        produzentenInHaushalt = ArrayList()
        urlaubeInHaushalt = ArrayList()

        // Die einzelnen Felder finden:
        val haushaltsnameneditfeld =
            root.findViewById<EditText>(R.id.edit_text_haushalt_bearbeiten_name)
        val strompreiseditfeld =
            root.findViewById<EditText>(R.id.edit_text_haushalt_bearbeiten_strompreis)
        val personeneditfeld =
            root.findViewById<EditText>(R.id.edit_text_haushalt_bearbeiten_anzahl_personen)
        zaehlerstandAkt =
            root.findViewById(R.id.text_view_haushalt_bearbeiten_zaehlerstand_akt_value)
        datumAkt = root.findViewById(R.id.text_view_haushalt_bearbeiten_datum_akt_value)
        zaehlerstandNeu =
            root.findViewById(R.id.edit_text_haushalt_bearbeiten_zaehlerstand_neu_value)
        datumNeu = root.findViewById(R.id.edit_text_haushalt_bearbeiten_datum_neu_value)
        auswertung = root.findViewById(R.id.text_view_haushalt_bearbeiten_zaehlerstand_auswertung)
        addCustomTextChangedListener(datumNeu)
        addCustomTextChangedListener(zaehlerstandNeu)
        val oekomixeditfeld =
            root.findViewById<CheckBox>(R.id.check_box_haushalt_bearbeiten_oekostrom)

        // Die Daten aus der RoomDatabse holen und in die Felder schreiben
        haushaltsnameneditfeld.setText(currHaushalt.getName())
        strompreiseditfeld.setText(currHaushalt.getStromkosten().toString())
        personeneditfeld.setText(currHaushalt.getBewohnerAnzahl().toString())

        if (currHaushalt.getZaehlerstand() != null && currHaushalt.getDatum() != null) {
            var tempString = currHaushalt.getZaehlerstand().toString() + " kWh"
            zaehlerstandAkt.text = tempString
            // currHaushalt.getDatum() kann nicht Null sein, da ja in der if Schleife genau dies überprüpft wird
            tempString = SimpleDateFormat(
                "dd.MM.yyyy",
                Locale.GERMAN
            ).format(currHaushalt.getDatum())
            datumAkt.text = tempString
        }
        oekomixeditfeld.isChecked = currHaushalt.getOekostrom()

        // Speicher Button zum speichern der eingegebenen Daten
        // finde den save button
        val savebutton: View = root.findViewById(R.id.button_haushalt_bearbeiten_speichern)
        // Click listener setzen
        savebutton.setOnClickListener { view ->
            if (view != null) {
                // try catch Block um Parser Fehler beim Datum abzufangen
                try {
                    // Schauen, dass alle Werte die gesetzt sein müssen gesetzt wurden
                    if (haushaltsnameneditfeld.text.isNotEmpty() &&
                        personeneditfeld.text.isNotEmpty() &&
                        strompreiseditfeld.text.isNotEmpty()
                    ) {

                        // Die Daten in die RoomDatabase speichern
                        currHaushalt.setName(haushaltsnameneditfeld.text.toString())
                        currHaushalt.setBewohnerAnzahl(personeneditfeld.text.toString().toInt())
                        currHaushalt.setStromkosten(strompreiseditfeld.text.toString().toDouble())
                        currHaushalt.setOekostrom(oekomixeditfeld.isChecked)
                        if (datumNeu.text.isNotEmpty() && zaehlerstandNeu.text.isNotEmpty()) {
                            // Datum einfügen
                            val tempDate = SimpleDateFormat(
                                "dd.MM.yyyy",
                                Locale.GERMAN
                            ).parse(datumNeu.text.toString())
                            val zaehlerNeu = zaehlerstandNeu.text.toString().toDouble()

                            val currDate = currHaushalt.getDatum()
                            val currZaehler = currHaushalt.getZaehlerstand()
                            if (currDate != null && currZaehler != null) {
                                if (tempDate.after(currHaushalt.getDatum()) && zaehlerNeu > currZaehler) {
                                    currHaushalt.setZaehlerstand(zaehlerNeu)
                                    // Kann nicht null sein, da eventuelle Parser Fehler durch try catch abgefangen werden.
                                    currHaushalt.setDatum(tempDate)
                                }
                            } else {
                                currHaushalt.setZaehlerstand(zaehlerNeu)
                                // Kann nicht null sein, da eventuelle Parser Fehler durch try catch abgefangen werden.
                                currHaushalt.setDatum(tempDate)
                            }
                        }
                        haushaltViewModel.updateHaushalt(currHaushalt)
                        // neues Fragment erstellen auf das weitergeleitet werden soll
                        val frag = HaushaltFragment()
                        // Fragment Manager aus Main Activity holen
                        val fragMan = parentFragmentManager
                        // Ftagment container aus content_main.xml muss ausgeählt werden, dann mit neuen Fragment ersetzen, dass oben erstellt wurde
                        fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                            .addToBackStack(null).commit()
                        // und anschließend noch ein commit()
                    } else {
                        Toast.makeText(
                            this.context,
                            R.string.leere_felder_haushalt,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                } catch (e: ParseException) {
                    Toast.makeText(this.context, R.string.parse_error_datum, Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                }
            }
        }
        // Das gleiche noch für den Abbrechen Button, wobei hier einfach zurück gesprungen werden kann ohne etwas zu machen, da wir ja das ganze nicht speichern wollen
        // finde den abbrechen button
        val abortbutton: View = root.findViewById(R.id.button_haushalt_bearbeiten_abbrechen)
        // Click listener setzen
        abortbutton.setOnClickListener { view ->
            if (view != null) {
                // neues Fragment erstellen auf das weitergeleitet werden soll
                val frag = HaushaltFragment()
                // Fragment Manager aus Main Activity holen
                val fragMan = parentFragmentManager
                // Ftagment container aus content_main.xml muss ausgeählt werden, dann mit neuen Fragment ersetzen, dass oben erstellt wurde
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).commit()
                // und anschließend noch ein commit()
            }
        }
        // Delete Button zum löschen des ausgewählten Haushalts
        // finde den löschen button
        val deletebutton: View = root.findViewById(R.id.button_haushalt_bearbeiten_loeschen)
        // Click listener setzen
        deletebutton.setOnClickListener { view ->
            if (view != null) {
                // Bestätigungsdialog mithilfe von AlertDialog
                val builder1: AlertDialog.Builder = AlertDialog.Builder(context)
                builder1.setMessage(R.string.haushaltlöschen_confirm)
                builder1.setPositiveButton(
                    R.string.ja
                ) { dialog, _ ->
                    // Daten werden aus der Datenbank gelöscht
                    haushaltViewModel.deleteHaushalt(currHaushalt)
                    // Man wir nur weitergeleitet, wenn man wirkllich löschen will. Deswegen nur bei positiv der Fragmentwechsel.
                    // neues Fragment erstellen auf das weitergeleitet werden soll
                    val frag = HaushaltFragment()
                    // Fragment Manager aus Main Activity holen
                    val fragMan = parentFragmentManager
                    // Ftagment container aus content_main.xml muss ausgeählt werden, dann mit neuen Fragment ersetzen, dass oben erstellt wurde
                    fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).commit()
                    // und anschließend noch ein commit()
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        haushaltViewModel.getAllVerbraucherByHaushaltID(currHaushalt.getHaushaltID()).observe(
            viewLifecycleOwner,
            Observer { verbraucher ->
                verbraucherInHaushalt.clear()
                verbraucherInHaushalt.addAll(verbraucher)
            }
        )
        haushaltViewModel.getAllProduzentenByHaushaltID(currHaushalt.getHaushaltID()).observe(
            viewLifecycleOwner,
            Observer { produzent ->
                produzentenInHaushalt.clear()
                produzentenInHaushalt.addAll(produzent)
            }
        )
        haushaltViewModel.getAllUrlaubByHaushaltID(currHaushalt.getHaushaltID()).observe(
            viewLifecycleOwner,
            Observer { urlaub ->
                urlaubeInHaushalt.clear()
                urlaubeInHaushalt.addAll(urlaub)
            }
        )
    }

    private fun addCustomTextChangedListener(edit: EditText): EditText {
        edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                try {
                    if (datumNeu.text.isNotEmpty() && zaehlerstandNeu.text.isNotEmpty()) {
                        // Datum einfügen
                        val tempDate = SimpleDateFormat(
                            "dd.MM.yyyy",
                            Locale.GERMAN
                        ).parse(datumNeu.text.toString())
                        val zaehlerNeu = zaehlerstandNeu.text.toString().toDouble()

                        val currDate = currHaushalt.getDatum()
                        val currZaehler = currHaushalt.getZaehlerstand()
                        if (currDate != null && currZaehler != null) {
                            if (tempDate.after(currHaushalt.getDatum()) && zaehlerNeu > currZaehler) {
                                val tempStr: String
                                val days: Int =
                                    ((tempDate.time / UrlaubCompanion.dateTimeToDays) - (currDate.time / UrlaubCompanion.dateTimeToDays)).toInt()
                                val diff = zaehlerNeu - currZaehler
                                var perYear = diff / days / UrlaubCompanion.yearToDay
                                perYear = String.format("%.2f", perYear).toDouble()
                                val estimatedGesamt = calculateGesamtverbrauch()
                                tempStr =
                                    "Zwischen den beiden Zählerständen liegen $days Tage mit einer Differenz von $diff kWh.\n" +
                                            "Auf ein Jahr hochgerechnet, ergibt sich ein Verbrauch von $perYear kWh pro Jahr.\n" +
                                            "Durch die eingetragenen Verbraucher, Produzenten und Urlaube wird ein Verbrauch von $estimatedGesamt kWh pro Jahr geschätzt."
                                auswertung.text = tempStr
                            } else {
                                auswertung.text = null
                            }
                        }
                    }
                } catch (e: ParseException) {
                    // e.printStackTrace()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        return edit
    }

    fun calculateGesamtverbrauch(): Double {
        var tempSum: Double =
            verbraucherInHaushalt.sumByDouble { geraete -> geraete.getJahresverbrauch() }

        tempSum += produzentenInHaushalt.sumByDouble { geraete -> getProdVerbrauch(geraete) }
            .withSign(-1)

        val tempList: ArrayList<Urlaub> = ArrayList()
        for (currUrlaub in urlaubeInHaushalt) {
            if ((currUrlaub.getDateVon().year + UrlaubCompanion.dateTimeToYears).toInt() == (Calendar.getInstance()
                    .get(Calendar.YEAR))
            ) {
                tempList.add(currUrlaub)
            }
        }
        tempSum += tempList.sumByDouble { urlaub ->
            urlaub.getErsparnisProTag() * (urlaub.getDateBis().time / UrlaubCompanion.dateTimeToDays - urlaub.getDateVon().time / UrlaubCompanion.dateTimeToDays + 1)
        }.withSign(-1)

        tempSum = String.format("%.2f", tempSum).toDouble()
        return tempSum
    }

    fun getProdVerbrauch(geraet: Geraete): Double {
        return geraet.getJahresverbrauch().withSign(1) * geraet.getEigenverbrauch()!! / 100
    }
}
