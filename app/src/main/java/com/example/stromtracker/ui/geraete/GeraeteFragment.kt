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


class GeraeteFragment : Fragment() {

    private lateinit var geraeteViewModel: GeraeteViewModel


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

                        Log.d("TAG", geraete.toString())

                    if (geraete.isEmpty()) {
                        geraeteViewModel.insertGeraet(Geraete("test", 0, 0, 0, 0, 0, 0, false))
                        geraeteViewModel.insertGeraet(Geraete("test2", 0, 0, 0, 0, 0, 0, false))


                    }
                }
            })
    }
}