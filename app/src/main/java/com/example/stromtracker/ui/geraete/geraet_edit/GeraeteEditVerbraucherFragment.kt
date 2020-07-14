package com.example.stromtracker.ui.geraete.geraet_edit

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R
import com.example.stromtracker.database.Geraete
import com.example.stromtracker.database.Kategorie
import com.example.stromtracker.database.Raum
import com.example.stromtracker.ui.geraete.GeraeteFragment
import com.example.stromtracker.ui.geraete.GeraeteViewModel
import kotlin.collections.ArrayList


class GeraeteEditVerbraucherFragment(
    private val currGeraet: Geraete,
    private val katList: ArrayList<Kategorie>,
    private val raumList: ArrayList<Raum>
) : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {
    private lateinit var geraeteViewModel: GeraeteViewModel
    private lateinit var inputName: EditText
    private lateinit var inputVolllast: EditText
    private lateinit var inputStandBy: EditText
    private lateinit var inputZeit: EditText
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

        val root = inflater.inflate(R.layout.fragment_geraete_edit, container, false)


        spinnerKat = root.findViewById(R.id.geraete_edit_kategorie_spinner)
        val katAdapter: ArrayAdapter<Kategorie> =
            ArrayAdapter<Kategorie>(root.context, android.R.layout.simple_spinner_item, katList)
        spinnerKat.adapter = katAdapter
        spinnerKat.onItemSelectedListener = this
        spinnerKat.setSelection(currGeraet.getKategorieID() - 1)

        spinnerRaum = root.findViewById(R.id.geraete_edit_raum_spinner)
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
        inputStandBy = root.findViewById(R.id.geraete_edit_edit_standBy)
        inputStandBy.setText(currGeraet.getStromStandBy().toString())
        inputZeit = root.findViewById(R.id.geraete_edit_edit_zeit)
        inputZeit.setText(currGeraet.getBetriebszeit().toString())

        checkUrlaub = root.findViewById(R.id.geraete_edit_checkbox)
        checkUrlaub.isChecked = currGeraet.getUrlaubsmodus()


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
                if (inputName.text.isNotEmpty() && inputStandBy.text.isNotEmpty() && inputVolllast.text.isNotEmpty() && inputZeit.text.isNotEmpty()) {

                    val volllast: Double? = inputVolllast.text.toString().toDoubleOrNull()
                    val standby: Double? = inputStandBy.text.toString().toDoubleOrNull()
                    val zeit: Double? = inputZeit.text.toString().toDoubleOrNull()

                    if (volllast != null && standby != null && zeit != null) {
                        val jahresverbrauch: Double =
                            ((volllast * zeit + standby * (24.0 - zeit)) / 1000.0) * 365.0

                        currGeraet.setBetriebszeit(zeit)
                        currGeraet.setHaushaltID(raumList[selectedRoom].getHaushaltID())
                        currGeraet.setJahresverbrauch(jahresverbrauch)
                        currGeraet.setKategorieID(katList[selectedKat].getKategorieID())
                        currGeraet.setRaumID(raumList[selectedRoom].getRaumID())
                        currGeraet.setStromVollast(volllast)
                        currGeraet.setStromStandBy(standby)
                        currGeraet.setUrlaubsmodus(checkUrlaub.isChecked)
                        currGeraet.setName(inputName.text.toString())

                        geraeteViewModel.updateGeraet(currGeraet)
                        val frag = GeraeteFragment()
                        fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                            .addToBackStack(null).commit()
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
            R.id.geraete_edit_button_abbrechen -> {
                val frag = GeraeteFragment()
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                    .addToBackStack(null).commit()
            }

            R.id.geraete_edit_delete -> {
                val confirmDeleteBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
                confirmDeleteBuilder.setMessage(R.string.geraete_edit_confirmDelete)
                confirmDeleteBuilder.setPositiveButton(
                    R.string.ja,
                    DialogInterface.OnClickListener { dialog, id ->
                        //Daten werden aus der Datenbank gelöscht
                        geraeteViewModel.deleteGeraet(currGeraet)
                        val frag = GeraeteFragment()
                        fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                            .addToBackStack(null).commit();
                        dialog.cancel()
                    })

                confirmDeleteBuilder.setNegativeButton(
                    R.string.nein,
                    DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })

                val confirmDeleteDialog: AlertDialog = confirmDeleteBuilder.create()
                confirmDeleteDialog.show()
            }

        }

    }
}
