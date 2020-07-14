package com.example.stromtracker.ui.geraete.geraet_new

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R
import com.example.stromtracker.database.Geraete
import com.example.stromtracker.database.Kategorie
import com.example.stromtracker.database.Raum
import com.example.stromtracker.ui.geraete.GeraeteFragment
import com.example.stromtracker.ui.geraete.GeraeteViewModel
import kotlin.collections.ArrayList


class GeraeteNewVerbraucherFragment(
    private val katList: ArrayList<Kategorie>,
    private val raumList: ArrayList<Raum>
) : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {
    private lateinit var geraeteViewModel: GeraeteViewModel
    private lateinit var inputName: EditText
    private lateinit var inputVolllast: EditText
    private lateinit var inputStandBy: EditText
    private lateinit var inputZeitVolllast: EditText
    private lateinit var inputZeitStandBy: EditText
    private lateinit var inputNotiz: EditText
    private lateinit var spinnerRaum: Spinner
    private lateinit var spinnerKat: Spinner
    private lateinit var checkUrlaub: CheckBox
    private var selectedRoom: Int = 0
    private var selectedKat: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_geraete_new_verbraucher, container, false)

        spinnerKat = root.findViewById(R.id.geraete_new_kategorie_spinner)
        val katAdapter: ArrayAdapter<Kategorie> =
            ArrayAdapter<Kategorie>(root.context, android.R.layout.simple_spinner_item, katList)
        spinnerKat.adapter = katAdapter
        spinnerKat.onItemSelectedListener = this

        spinnerRaum = root.findViewById(R.id.geraete_new_raum_spinner)
        val raumAdapter: ArrayAdapter<Raum> =
            ArrayAdapter<Raum>(root.context, android.R.layout.simple_spinner_item, raumList)
        spinnerRaum.adapter = raumAdapter
        spinnerRaum.onItemSelectedListener = this

        val abbrBtn = root.findViewById<Button>(R.id.geraete_new_button_abbrechen)
        abbrBtn.setOnClickListener(this)
        val saveBtn = root.findViewById<Button>(R.id.geraete_new_save)
        saveBtn.setOnClickListener(this)

        inputName = root.findViewById(R.id.geraete_new_edit_name)
        inputVolllast = root.findViewById(R.id.geraete_new_edit_volllast)
        inputStandBy = root.findViewById(R.id.geraete_new_edit_standBy)
        inputZeitVolllast = root.findViewById(R.id.geraete_new_edit_zeit_volllast)
        inputZeitStandBy = root.findViewById(R.id.geraete_new_edit_zeit_standBy)
        inputNotiz = root.findViewById(R.id.geraete_new_edit_notiz)

        checkUrlaub = root.findViewById(R.id.geraete_new_checkbox)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        geraeteViewModel = ViewModelProviders.of(this).get(GeraeteViewModel::class.java)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }


    override fun onItemSelected(parent: AdapterView<*>, v: View, pos: Int, id: Long) {
        when (parent.id) {
            R.id.geraete_new_raum_spinner -> {
                selectedRoom = pos
            }
            R.id.geraete_new_kategorie_spinner -> {
                selectedKat = pos
            }
            else -> {
            }

        }
    }

    override fun onClick(v: View) {
        val fragMan = parentFragmentManager
        when (v.id) {
            R.id.geraete_new_save -> {
                if (inputName.text.isNotEmpty() && inputStandBy.text.isNotEmpty() && inputVolllast.text.isNotEmpty() && inputZeitVolllast.text.isNotEmpty() && inputZeitStandBy.text.isNotEmpty()) {

                    val volllast: Double? = inputVolllast.text.toString().toDoubleOrNull()
                    val standby: Double? = inputStandBy.text.toString().toDoubleOrNull()
                    val zeitVolllast: Double? = inputZeitVolllast.text.toString().toDoubleOrNull()
                    val zeitStandBy: Double? = inputZeitStandBy.text.toString().toDoubleOrNull()
                    var notiz: String? = inputNotiz.text.toString()


                    if (volllast != null && standby != null && zeitVolllast != null && zeitStandBy != null && notiz != null) {
                        //TODO magic numbers
                        if(notiz.isEmpty()) {
                            notiz = null
                        }

                        if (zeitStandBy <= 24.0 && zeitVolllast <= 24.0 && (zeitStandBy + zeitVolllast) <= 24.0) {
                            val jahresverbrauch =
                                (((volllast * zeitVolllast) + (zeitStandBy * standby)) / 1000.0) * 365.0

                            val geraet = Geraete(
                                inputName.text.toString(),
                                katList[selectedKat].getKategorieID(),
                                raumList[selectedRoom].getRaumID(),
                                raumList[selectedRoom].getHaushaltID(),
                                volllast,
                                standby,
                                zeitVolllast,
                                zeitStandBy,
                                checkUrlaub.isChecked,
                                jahresverbrauch,
                                null,
                                notiz
                            )
                            geraeteViewModel.insertGeraet(geraet)
                            val frag = GeraeteFragment()
                            fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                                .addToBackStack(null).commit()
                        } else {
                            Toast.makeText(
                                this.context,
                                R.string.geraet_new_zeit_error,
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    } else {
                        Toast.makeText(
                            this.context,
                            R.string.geraet_new_nullValue,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } else {
                    Toast.makeText(this.context, R.string.geraet_new_nullValue, Toast.LENGTH_SHORT)
                        .show()
                }
            }
            R.id.geraete_new_button_abbrechen -> {
                val frag = GeraeteFragment()
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                    .addToBackStack(null).commit()
            }

        }

    }
}
