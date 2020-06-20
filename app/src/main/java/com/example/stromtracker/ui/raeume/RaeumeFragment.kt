package com.example.stromtracker.ui.raeume

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R
import com.example.stromtracker.ui.raeume.RaeumeViewModel

class RaeumeFragment : Fragment() {

    private lateinit var raeumeViewModel: RaeumeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        raeumeViewModel =
            ViewModelProviders.of(this).get(com.example.stromtracker.ui.raeume.RaeumeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_raeume, container, false)
        val textView: TextView = root.findViewById(R.id.text_raeume)
        raeumeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}