package com.example.stromtracker.ui.urlaub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.R
import com.example.stromtracker.database.Geraete
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.database.Urlaub
import com.example.stromtracker.ui.urlaub.urlaub_new.UrlaubNewFragment
import com.getbase.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class UrlaubFragment : Fragment(), View.OnClickListener {

    private lateinit var urlaubViewModel: UrlaubViewModel

    private lateinit var urlaubList: ArrayList<Urlaub>
    private lateinit var verbraucherList: ArrayList<Geraete>
    private lateinit var currHaushalt: Haushalt

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var addUrlaub: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //TODO auf Livedata umsteigen (Siehe branch current_haushalt)
        val navView = requireActivity().findViewById<NavigationView>(R.id.nav_view)

        val sp: Spinner = navView.menu.findItem(R.id.nav_haushalt).actionView as Spinner
        currHaushalt = sp.selectedItem as Haushalt

        urlaubList = ArrayList()
        verbraucherList = ArrayList()

        urlaubViewModel =
            ViewModelProviders.of(this).get(UrlaubViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_urlaub, container, false)

        //RecyclerView mit geholten Daten aus DB initialisieren
        viewAdapter = UrlaubListAdapter(urlaubList, currHaushalt)

        viewManager = LinearLayoutManager(this.context)
        recyclerView = root.findViewById<RecyclerView>(R.id.urlaub_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        addUrlaub = root.findViewById(R.id.urlaub_button_add)
        addUrlaub.setOnClickListener(this)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        urlaubViewModel.getAllVerbraucher().observe(
            viewLifecycleOwner,
            Observer { geraete ->
                if (geraete != null) {
                    verbraucherList.clear()
                    verbraucherList.addAll(geraete)
                    viewAdapter.notifyDataSetChanged()
                }
            })
        urlaubViewModel.getAllUrlaub().observe(
            viewLifecycleOwner,
            Observer { urlaub ->
                if (urlaub != null) {
                    urlaubList.clear()
                    urlaubList.addAll(urlaub.sortedWith(compareByDescending { it.getDateVon() }))
                    viewAdapter.notifyDataSetChanged()
                }
            }
        )
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.urlaub_button_add -> {
                val frag = UrlaubNewFragment(verbraucherList, currHaushalt)
                val fragMan = parentFragmentManager
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                    .addToBackStack(null).commit()
            }
        }
    }
}
