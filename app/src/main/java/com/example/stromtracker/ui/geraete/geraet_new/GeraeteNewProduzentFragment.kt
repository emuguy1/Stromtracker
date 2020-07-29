package com.example.stromtracker.ui.geraete.geraet_new

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stromtracker.R
import com.example.stromtracker.database.Geraete
import com.example.stromtracker.database.Kategorie
import com.example.stromtracker.database.Raum
import com.example.stromtracker.ui.geraete.GeraeteCompanion
import com.example.stromtracker.ui.geraete.GeraeteFragment
import com.example.stromtracker.ui.geraete.GeraeteViewModel

class GeraeteNewProduzentFragment(
    private val katList: ArrayList<Kategorie>,
    private val raumList: ArrayList<Raum>
) :
    Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var geraeteViewModel: GeraeteViewModel
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

        val root = inflater.inflate(R.layout.fragment_geraete_new_produzent, container, false)

        spinnerKat = root.findViewById(R.id.geraete_new_produzent_spinner_kategorie)
        val katAdapter: ArrayAdapter<Kategorie> =
            ArrayAdapter<Kategorie>(root.context, android.R.layout.simple_spinner_item, katList)
        spinnerKat.adapter = katAdapter
        spinnerKat.onItemSelectedListener = this

        spinnerRaum = root.findViewById(R.id.geraete_new_produzent_spinner_raum)
        val raumAdapter: ArrayAdapter<Raum> =
            ArrayAdapter<Raum>(root.context, android.R.layout.simple_spinner_item, raumList)
        spinnerRaum.adapter = raumAdapter
        spinnerRaum.onItemSelectedListener = this

        val abbrBtn = root.findViewById<Button>(R.id.geraete_new_produzent_button_abbrechen)
        abbrBtn.setOnClickListener(this)
        val saveBtn = root.findViewById<Button>(R.id.geraete_new_produzent_save)
        saveBtn.setOnClickListener(this)

        inputName = root.findViewById(R.id.geraete_new_produzent_edit_name)
        inputProdProJahr = root.findViewById(R.id.geraete_new_produzent_edit_prod)
        inputVerbrauch = root.findViewById(R.id.geraete_new_produzent_edit_verbrauch)

        inputNotiz = root.findViewById(R.id.geraete_prod_new_edit_notiz)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        geraeteViewModel = ViewModelProvider(this).get(GeraeteViewModel::class.java)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>, v: View, pos: Int, id: Long) {
        when (parent.id) {
            R.id.geraete_new_produzent_spinner_raum -> {
                selectedRoom = pos
            }
            R.id.geraete_new_produzent_spinner_kategorie -> {
                selectedKat = pos
            }
            else -> {
            }
        }
    }

    override fun onClick(v: View) {
        val fragMan = parentFragmentManager
        when (v.id) {
            R.id.geraete_new_produzent_save -> {
                if (inputName.text.isNotEmpty() && inputProdProJahr.text.isNotEmpty()) {

                    val prodProJahr: Double? = inputProdProJahr.text.toString().toDoubleOrNull()
                    val eigenverbrauch: Double? = inputVerbrauch.text.toString().toDoubleOrNull()
                    var notiz: String? = inputNotiz.text.toString()

                    if (prodProJahr != null && prodProJahr > 0.0 && eigenverbrauch != null && eigenverbrauch > 0.0 && notiz != null) {
                        // negativer Verbrauch = Produzent, kein Verbraucher

                        if (notiz.isEmpty()) {
                            notiz = null
                        }
                        val jahresverbrauch: Double = prodProJahr * (-1)
                        val geraet = Geraete(
                            inputName.text.toString(),
                            katList[selectedKat].getKategorieID(),
                            raumList[selectedRoom].getRaumID(),
                            raumList[selectedRoom].getHaushaltID(),
                            0.0,
                            null,
                            0.0,
                            null,
                            false,
                            GeraeteCompanion.roundDouble(jahresverbrauch),
                            eigenverbrauch,
                            notiz
                        )
                        geraeteViewModel.insertGeraet(geraet)
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
            R.id.geraete_new_produzent_button_abbrechen -> {
                val frag = GeraeteFragment()
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                    .addToBackStack(null).commit()
            }
        }
    }
}
