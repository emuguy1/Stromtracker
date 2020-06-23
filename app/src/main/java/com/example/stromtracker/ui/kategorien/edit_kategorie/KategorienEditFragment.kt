package com.example.stromtracker.ui.kategorien.edit_kategorie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R
import com.example.stromtracker.ui.kategorien.new_kategorie.KategorienNewViewModel

class KategorienEditFragment : Fragment(){
    private lateinit var katViewModel: KategorienNewViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        katViewModel =
            ViewModelProviders.of(this).get(KategorienNewViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_kategorien_edit, container, false)

        val textView: TextView = root.findViewById(R.id.kategorie_edit_text)

        return root
    }
}