package com.example.stromtracker.ui.haushalt.haushaltErstellen
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R
import com.example.stromtracker.ui.haushalt.haushalteBearbeiten_Loeschen.HaushaltBearbeitenLoeschenViewModel

class HaushaltErstellenFragment: Fragment() {
    private lateinit var haushalterstellenViewModel: HaushaltErstellenViewModel



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        haushalterstellenViewModel =
            ViewModelProviders.of(this).get(HaushaltErstellenViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_haushalterstellen, container, false)
        return root
    }
}