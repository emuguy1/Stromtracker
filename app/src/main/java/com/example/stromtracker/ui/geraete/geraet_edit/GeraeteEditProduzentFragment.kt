package com.example.stromtracker.ui.geraete.geraet_edit

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.stromtracker.R
import com.example.stromtracker.database.Geraete
import com.example.stromtracker.database.Kategorie
import com.example.stromtracker.database.Raum
import com.example.stromtracker.ui.SharedViewModel
import com.example.stromtracker.ui.geraete.GeraeteCompanion
import com.example.stromtracker.ui.geraete.GeraeteFragment

class GeraeteEditProduzentFragment(
    private val currGeraet: Geraete,
    private val katList: ArrayList<Kategorie>,
    private val raumList: ArrayList<Raum>
) :
    Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var inputName: EditText
    private lateinit var inputProdProJahr: EditText
    private lateinit var inputVerbrauch: EditText
    private lateinit var inputNotiz: EditText

    private lateinit var spinnerRaum: Spinner
    private lateinit var spinnerKat: Spinner

    private var selectedRoom: Int = 0
    private var selectedKat: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(
            R.layout.fragment_geraete_edit_produzent,
            container,
            false
        )

        spinnerKat = root.findViewById(R.id.geraete_edit_produzent_spinner_kategorie)
        val katAdapter: ArrayAdapter<Kategorie> =
            ArrayAdapter<Kategorie>(root.context, android.R.layout.simple_spinner_item, katList)
        spinnerKat.adapter = katAdapter
        spinnerKat.onItemSelectedListener = this
        spinnerKat.setSelection(currGeraet.getKategorieID() - 1)

        spinnerRaum = root.findViewById(R.id.geraete_edit_produzent_spinner_raum)
        val raumAdapter: ArrayAdapter<Raum> =
            ArrayAdapter<Raum>(root.context, android.R.layout.simple_spinner_item, raumList)
        spinnerRaum.adapter = raumAdapter
        spinnerRaum.onItemSelectedListener = this
        var count = 0
        for (raum in raumList) {
            if (raum.getRaumID() == currGeraet.getRaumID()) {
                spinnerRaum.setSelection(count)
                break
            } else {
                count++
            }
        }

        val abbrBtn = root.findViewById<Button>(R.id.geraete_edit_produzent_button_abbrechen)
        abbrBtn.setOnClickListener(this)
        val saveBtn = root.findViewById<Button>(R.id.geraete_edit_produzent_save)
        saveBtn.setOnClickListener(this)
        val delBtn = root.findViewById<Button>(R.id.geraete_edit_produzent_button_loeschen)
        delBtn.setOnClickListener(this)

        inputName = root.findViewById(R.id.geraete_edit_produzent_edit_name)
        inputName.setText(currGeraet.getName())
        inputProdProJahr = root.findViewById(R.id.geraete_edit_produzent_edit_prod)
        inputProdProJahr.setText((currGeraet.getJahresverbrauch() * (-1)).toString())
        inputVerbrauch = root.findViewById(R.id.geraete_edit_produzent_edit_verbrauch)
        inputVerbrauch.setText(currGeraet.getEigenverbrauch().toString())

        inputNotiz = root.findViewById(R.id.geraete_prod_edit_edit_notiz)
        if (currGeraet.getNotiz() != null) {
            inputNotiz.setText(currGeraet.getNotiz())
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>, v: View, pos: Int, id: Long) {
        when (parent.id) {
            R.id.geraete_edit_produzent_spinner_raum -> {
                selectedRoom = pos
            }
            R.id.geraete_edit_produzent_spinner_kategorie -> {
                selectedKat = pos
            }
            else -> {
            }
        }
    }

    override fun onClick(v: View) {
        val fragMan = parentFragmentManager
        when (v.id) {
            R.id.geraete_edit_produzent_save -> {
                if (inputName.text.isNotEmpty() && inputProdProJahr.text.isNotEmpty()) {

                    val prodProJahr: Double? = inputProdProJahr.text.toString().toDoubleOrNull()
                    val eigenverbrauch: Double? = inputVerbrauch.text.toString().toDoubleOrNull()
                    var notiz: String? = inputNotiz.text.toString()

                    if (prodProJahr != null &&
                        prodProJahr > 0.0 &&
                        eigenverbrauch != null &&
                        eigenverbrauch > 0.0 &&
                        notiz != null
                    ) {

                        if (notiz.isEmpty()) {
                            notiz = null
                        }

                        val jahresverbrauch: Double = prodProJahr * (-1)

                        currGeraet.setName(inputName.text.toString())
                        currGeraet.setKategorieID(katList[selectedKat].getKategorieID())
                        currGeraet.setRaumID(raumList[selectedRoom].getRaumID())
                        currGeraet.setJahresverbrauch(GeraeteCompanion.roundDouble(jahresverbrauch))
                        currGeraet.setEigenverbrauch(eigenverbrauch)
                        currGeraet.setNotiz(notiz)

                        sharedViewModel.updateGeraet(currGeraet)
                        val frag = GeraeteFragment()
                        fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                            .addToBackStack(null).commit()
                    } else {
                        GeraeteCompanion.validValues(this.context)
                    }
                } else {
                    GeraeteCompanion.validValues(this.context)
                }
            }
            R.id.geraete_edit_produzent_button_abbrechen -> {
                val frag = GeraeteFragment()
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                    .addToBackStack(null).commit()
            }
            R.id.geraete_edit_produzent_button_loeschen -> {
                alertDelete(fragMan)
            }
        }
    }

    private fun alertDelete(fragMan: FragmentManager) {
        val confirmDeleteBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        confirmDeleteBuilder.setMessage(R.string.geraete_edit_confirmDelete)
        confirmDeleteBuilder.setPositiveButton(
            R.string.ja
        ) { dialog, _ ->
            // Daten werden aus der Datenbank gelöscht
            sharedViewModel.deleteGeraet(currGeraet)
            // Man wir nur weitergeleitet, wenn man wirklich löschen will.
            // Deswegen nur bei positiv der Fragmentwechsel.
            // neues Fragment erstellen auf das weitergeleitet werden soll
            val frag = GeraeteFragment()
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
}
