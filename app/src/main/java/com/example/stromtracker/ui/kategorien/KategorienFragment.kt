package com.example.stromtracker.ui.kategorien

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.R

class KategorienFragment : Fragment() {

    private lateinit var kategorienViewModel: KategorienViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        kategorienViewModel =
            ViewModelProviders.of(this).get(com.example.stromtracker.ui.kategorien.KategorienViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_kategorien, container, false)


        //TODO Bestehende Kategorien holen und hier einfügen
        var myDataset = arrayOf("Kat1", "Kat2", "Kat3")
        viewAdapter = KategorienListAdapter(myDataset)
        viewManager = LinearLayoutManager(this.context)

        recyclerView = root.findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        return root
    }
}