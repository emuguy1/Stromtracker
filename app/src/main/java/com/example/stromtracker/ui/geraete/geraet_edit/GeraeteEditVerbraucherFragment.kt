package com.example.stromtracker.ui.geraete.geraet_edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.app.AlertDialog
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
import kotlin.collections.ArrayList

class GeraeteEditVerbraucherFragment(
    private val currGeraet: Geraete,
    private val katList: ArrayList<Kategorie>,
    private val raumList: ArrayList<Raum>
) : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var sharedViewModel: SharedViewModel
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

        val root = inflater.inflate(
            R.layout.fragment_geraete_edit_verbraucher,
            container,
            false)

        spinnerKat = root.findViewById(R.id.geraete_edit_kategorie_spinner)
        val katAdapter: ArrayAdapter<Kategorie> =
            ArrayAdapter(root.context, android.R.layout.simple_spinner_item, katList)
        spinnerKat.adapter = katAdapter
        spinnerKat.onItemSelectedListener = this
        spinnerKat.setSelection(currGeraet.getKategorieID() - 1)

        spinnerRaum = root.findViewById(R.id.geraete_edit_raum_spinner)
        val raumAdapter: ArrayAdapter<Raum> =
            ArrayAdapter(root.context, android.R.layout.simple_spinner_item, raumList)
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

        val abbrBtn = root.findViewById<Button>(R.id.geraete_edit_button_abbrechen)
        abbrBtn.setOnClickListener(this)
        val saveBtn = root.findViewById<Button>(R.id.geraete_edit_save)
        saveBtn.setOnClickListener(this)
        val delBtn = root.findViewById<Button>(R.id.geraete_edit_delete)
        delBtn.setOnClickListener(this)

        inputName = root.findViewById(R.id.geraete_edit_edit_name)
        inputName.setText(currGeraet.getName())
        inputVolllast = root.findViewById(R.id.geraete_edit_edit_volllast)
        inputVolllast.setText(currGeraet.getStromVollast().toString())
        inputStandBy = root.findViewById(R.id.geraete_edit_edit_standby)
        if (currGeraet.getStromStandBy() != null) {
            inputStandBy.setText(currGeraet.getStromStandBy().toString())
        }
        inputZeitVolllast = root.findViewById(R.id.geraete_edit_edit_zeit_volllast)
        inputZeitVolllast.setText(currGeraet.getBetriebszeit().toString())
        inputNotiz = root.findViewById(R.id.geraete_edit_edit_notiz)
        if (currGeraet.getNotiz() != null) {
            inputNotiz.setText(currGeraet.getNotiz())
        }

        inputZeitStandBy = root.findViewById(R.id.geraete_edit_edit_zeit_standby)
        if (currGeraet.getBetriebszeitStandBy() != null) {
            inputZeitStandBy.setText(currGeraet.getBetriebszeitStandBy().toString())
        }

        checkUrlaub = root.findViewById(R.id.geraete_edit_checkbox)
        checkUrlaub.isChecked = currGeraet.getUrlaubsmodus()

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
            R.id.geraete_edit_raum_spinner -> {
                selectedRoom = pos
            }
            R.id.geraete_edit_kategorie_spinner -> {
                selectedKat = pos
            }
            else -> {
            }
        }
    }

    override fun onClick(v: View) {
        val fragMan = parentFragmentManager
        when (v.id) {
            R.id.geraete_edit_save -> {
                if (inputName.text.isNotEmpty() && inputZeitVolllast.text.isNotEmpty() &&
                    inputVolllast.text.isNotEmpty()
                ) {

                    val volllast: Double? = inputVolllast.text.toString().toDoubleOrNull()
                    val standby: Double? = inputStandBy.text.toString().toDoubleOrNull()
                    val zeitVolllast: Double? = inputZeitVolllast.text.toString().toDoubleOrNull()
                    val zeitStandBy: Double? = inputZeitStandBy.text.toString().toDoubleOrNull()
                    var notiz: String? = inputNotiz.text.toString()
                    val jahresverbrauch: Double

                    if (volllast != null && zeitVolllast != null && notiz != null) {
                        if (zeitStandBy != null && standby != null) {
                            if (zeitStandBy <= GeraeteCompanion.maxHours &&
                                zeitVolllast <= GeraeteCompanion.maxHours &&
                                (zeitStandBy + zeitVolllast) <= GeraeteCompanion.maxHours
                            ) {
                                jahresverbrauch =
                                    GeraeteCompanion.calculateKWH(
                                        (volllast * zeitVolllast) + (zeitStandBy * standby)
                                    )
                            } else {
                                GeraeteCompanion.validTimes(this.context)
                                return
                            }
                        } else if (zeitStandBy == null && standby == null) {
                            if (zeitVolllast <= GeraeteCompanion.maxHours) {
                                jahresverbrauch =
                                    GeraeteCompanion.calculateKWH(volllast * zeitVolllast)
                            } else {
                                GeraeteCompanion.validTimes(this.context)
                                return
                            }
                        } else {
                            GeraeteCompanion.standByError(this.context)
                            return
                        }
                        if (notiz.isEmpty()) {
                            notiz = null
                        }

                        currGeraet.setBetriebszeit(zeitVolllast)
                        currGeraet.setBetriebszeitStandBy(zeitStandBy)
                        currGeraet.setJahresverbrauch(GeraeteCompanion.roundDouble(jahresverbrauch))
                        currGeraet.setKategorieID(katList[selectedKat].getKategorieID())
                        currGeraet.setRaumID(raumList[selectedRoom].getRaumID())
                        currGeraet.setStromVollast(volllast)
                        currGeraet.setStromStandBy(standby)
                        currGeraet.setUrlaubsmodus(checkUrlaub.isChecked)
                        currGeraet.setName(inputName.text.toString())
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
            R.id.geraete_edit_button_abbrechen -> {
                val frag = GeraeteFragment()
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                    .addToBackStack(null).commit()
            }

            R.id.geraete_edit_delete -> {
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
            val frag = GeraeteFragment()
            fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                .addToBackStack(null).commit()
            dialog.cancel()
        }

        confirmDeleteBuilder.setNegativeButton(
            R.string.nein
        ) { dialog, _ -> dialog.cancel() }

        val confirmDeleteDialog: AlertDialog = confirmDeleteBuilder.create()
        confirmDeleteDialog.show()
    }
}
