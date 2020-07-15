package com.example.stromtracker.ui.urlaub.urlaub_new

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.stromtracker.R
import com.example.stromtracker.ui.urlaub.UrlaubFragment

class UrlaubNewFragment : Fragment(), View.OnClickListener {

    private lateinit var btnAbbrechen : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_urlaub_new, container, false)

        btnAbbrechen = root.findViewById(R.id.urlaub_new_button_abbrechen)
        btnAbbrechen.setOnClickListener(this)

        return root
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.urlaub_new_button_abbrechen -> {
                val frag = UrlaubFragment()
                val fragMan = parentFragmentManager
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).addToBackStack(null).commit()
            }
        }
    }
}