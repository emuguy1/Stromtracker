package com.example.stromtracker.ui.geraete

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R
import com.example.stromtracker.database.Geraete
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.database.Kategorie
import com.example.stromtracker.database.Raum


class GeraeteFragment : Fragment() {

    private lateinit var geraeteViewModel: GeraeteViewModel
    private  lateinit var geraeteList:List<Geraete>
    private  lateinit var haushaltList:List<Haushalt>


    fun setGeraeteList(list:List<Geraete>) {

            geraeteList = list
        Log.d("TAG", geraeteList.toString())
        //Adapter setzen


    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {




        //Log.d("TAG", ViewModelProvider(this).get(GeraeteViewModel.javaClass).toString())



        //GeraeteViewModel =
         //     ViewModelProviders.of(this).get(com.example.stromtracker.ui.geraete.GeraeteViewModel::class.java)





        val root = inflater.inflate(R.layout.fragment_geraete, container, false)



    /*
        geraeteViewModel.setGereateList(Geraete(1, "test", "test2"))
        Log.d("TAG2", geraeteViewModel.getGereateList().toString())


     */







        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //geraeteViewModel= ViewModelProvider(this).get(GeraeteViewModel::class.java)

        geraeteViewModel = ViewModelProviders.of(this).get(GeraeteViewModel::class.java)

        geraeteViewModel.getAllGeraete().observe(
            viewLifecycleOwner,
            Observer { geraete ->
                if (geraete != null) {

                    setGeraeteList(geraete)

                    if (geraete.isEmpty()) {
                        geraeteViewModel.insertHaushalt(Haushalt("name", 0.0, 1, 0.0, null, false))
                        geraeteViewModel.insertKategorie(Kategorie("test"))
                        geraeteViewModel.insertRaum(Raum("test"))
                        geraeteViewModel.insertGeraet(Geraete("test", 0, 0, 0, 10, 0, 0, false, null))


                    }


                }
            })


    }
}