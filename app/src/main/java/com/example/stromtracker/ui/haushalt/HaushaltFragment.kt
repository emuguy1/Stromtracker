package com.example.stromtracker.ui.haushalt

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.R
import com.example.stromtracker.ui.haushalt.HaushaltViewModel
import com.example.stromtracker.ui.haushalteBearbeiten_Loeschen.HaushaltBearbeitenLoeschenFragment


class HaushaltFragment: Fragment() {
    private lateinit var haushaltViewModel: HaushaltViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        haushaltViewModel =
            ViewModelProviders.of(this).get(HaushaltViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_haushalt, container, false)

        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerViewHaushalt)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = ListAdapterHaushalt()



        val fab: View = root.findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            val intent = Intent(this@HaushaltFragment.context, HaushaltBearbeitenLoeschenFragment::class.java)
            intent.putExtra("hallo", "text")
            startActivity(intent)
        }
        return root
    }

}


