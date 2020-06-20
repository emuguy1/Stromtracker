package com.example.stromtracker.ui.haushalteBearbeiten_Loeschen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R

class HaushaltBearbeitenLoeschenFragment: Fragment() {
    private lateinit var haushaltbearbeitenLoeschenViewModel: HaushaltBearbeitenLoeschenViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        haushaltbearbeitenLoeschenViewModel =
            ViewModelProviders.of(this).get(HaushaltBearbeitenLoeschenViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_haushalt_bearbeiten_loeschen, container, false)
        val textView: TextView = root.findViewById(R.id.text_haushaltBearbeitenLoeschen)
        haushaltbearbeitenLoeschenViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}


