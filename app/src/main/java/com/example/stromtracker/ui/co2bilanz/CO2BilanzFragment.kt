package com.example.stromtracker.ui.co2bilanz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.stromtracker.R

class CO2BilanzFragment : Fragment() {

    private lateinit var co2bilanzViewModel: CO2BilanzViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        co2bilanzViewModel =
            ViewModelProvider(this).get(com.example.stromtracker.ui.co2bilanz.CO2BilanzViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_co2bilanz, container, false)
        val textView: TextView = root.findViewById(R.id.text_co2bilanz)
        co2bilanzViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
