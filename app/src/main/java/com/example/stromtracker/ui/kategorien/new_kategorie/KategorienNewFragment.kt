package com.example.stromtracker.ui.kategorien.new_kategorie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R

class KategorienNewFragment : Fragment() {
        private lateinit var katViewModel: KategorienNewViewModel

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            katViewModel =
                ViewModelProviders.of(this).get(KategorienNewViewModel::class.java)

            val root = inflater.inflate(R.layout.fragment_kategorien_new, container, false)

            val textView: TextView = root.findViewById(R.id.kategorie_new_text)

            return root
        }
}