package com.example.stromtracker.ui.urlaub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R

class UrlaubFragment : Fragment() {

    private lateinit var urlaubViewModel: UrlaubViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        urlaubViewModel =
                ViewModelProviders.of(this).get(UrlaubViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_urlaub, container, false)
        val textView: TextView = root.findViewById(R.id.text_urlaub)
        urlaubViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}