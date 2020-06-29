package com.example.stromtracker.ui.geraete.geraet_new

import android.os.Bundle
import android.util.Log
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
import java.util.*
import kotlin.collections.ArrayList


class GeraeteNewFragment(private val katList: ArrayList<Kategorie>, private val raumList: ArrayList<Raum>):Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {
    private lateinit var geraeteViewModel:GeraeteViewModel
    private lateinit var inputName:EditText
    private lateinit var inputVolllast:EditText
    private lateinit var inputStandBy:EditText
    private lateinit var inputZeit:EditText
    private lateinit var spinnerRaum:Spinner
    private lateinit var spinnerKat:Spinner
    private lateinit var checkUrlaub:CheckBox
    private var selectedRoom:Int = 0
    private var selectedKat:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_geraete_new, container, false)

        spinnerKat = root.findViewById(R.id.geraete_new_KategorieSpinner)
        val katAdapter: ArrayAdapter<Kategorie> =
            ArrayAdapter<Kategorie>(root.context, android.R.layout.simple_spinner_item, katList)
        spinnerKat.adapter = katAdapter
        spinnerKat.onItemSelectedListener = this

        spinnerRaum = root.findViewById(R.id.geraete_new_RaumSpinner)
        val raumAdapter: ArrayAdapter<Raum> =
            ArrayAdapter<Raum>(root.context, android.R.layout.simple_spinner_item, raumList)
        spinnerRaum.adapter = raumAdapter
        spinnerRaum.onItemSelectedListener = this

        val abbrBtn = root.findViewById<Button>(R.id.geraete_new_button_abbrechen)
        abbrBtn.setOnClickListener(this)
        val saveBtn = root.findViewById<Button>(R.id.geraete_new_save)
        saveBtn.setOnClickListener(this)
        inputName = root.findViewById(R.id.geraete_new_EditName)
        inputVolllast = root.findViewById(R.id.geraete_new_EditVolllast)
        inputStandBy = root.findViewById(R.id.geraete_new_EditStandBy)
        inputZeit = root.findViewById(R.id.geraete_new_EditZeit)

        checkUrlaub = root.findViewById(R.id.geraete_checkbox)


        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        geraeteViewModel = ViewModelProviders.of(this).get(GeraeteViewModel::class.java)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


    override fun onItemSelected(parent: AdapterView<*>, v: View, pos: Int, id: Long) {
        when (v.id) {
            R.id.geraete_new_RaumSpinner -> {
                selectedRoom = pos
            }
            R.id.geraete_new_KategorieSpinner -> {
                selectedKat = pos
            }
            else -> {
                Toast.makeText(
                    v.context,
                    String.format(Locale.GERMAN, "%d was pressed.", v.id),
                    Toast.LENGTH_SHORT
                ).show()

            }

        }
    }


     override fun onClick(v: View) {
        val fragMan = parentFragmentManager
        when(v.id) {
            R.id.geraete_new_save -> {
                Log.d("TAG", inputStandBy.text.toString().toDouble().toString())
                if(inputName.text.toString() != "" && inputStandBy.text.toString() != "" && inputVolllast.text.toString() != "" && inputZeit.toString() != "") {
                    val geraet = Geraete(inputName.text.toString(), katList[selectedKat].getKategorieID(), raumList[selectedRoom].getRaumID(), raumList[selectedRoom].getHaushaltID()
                        , inputVolllast.text.toString().toDouble(), inputStandBy.toString().toDouble(), inputZeit.toString().toDouble(), checkUrlaub.isChecked, null)
                    geraeteViewModel.insertGeraet(geraet)
                    val frag = GeraeteFragment()
                    fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).addToBackStack(null).commit()

                }
                else {
                    Toast.makeText(this.context, R.string.geraet_new_nullValue, Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(v.context, String.format(Locale.GERMAN,"%d was pressed.", v.id), Toast.LENGTH_SHORT).show()
            }


        }

        }
    }


