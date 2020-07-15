package com.example.stromtracker.ui.urlaub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R
import com.example.stromtracker.ui.urlaub.urlaub_new.UrlaubNewFragment
import com.getbase.floatingactionbutton.FloatingActionButton

class UrlaubFragment : Fragment() , View.OnClickListener{

    private lateinit var urlaubViewModel: UrlaubViewModel

    private lateinit var addUrlaub : FloatingActionButton

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        urlaubViewModel =
                ViewModelProviders.of(this).get(UrlaubViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_urlaub, container, false)

        addUrlaub = root.findViewById(R.id.urlaub_button_add)
        addUrlaub.setOnClickListener(this)

        return root
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.urlaub_button_add -> {
                val frag = UrlaubNewFragment()
                val fragMan = parentFragmentManager
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).addToBackStack(null).commit()
            }
        }
    }
}