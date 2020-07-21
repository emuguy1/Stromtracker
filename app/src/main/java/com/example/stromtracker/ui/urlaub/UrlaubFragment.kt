package com.example.stromtracker.ui.urlaub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.R
import com.example.stromtracker.database.Geraete
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.database.Urlaub
import com.example.stromtracker.ui.SharedViewModel
import com.example.stromtracker.ui.urlaub.urlaub_new.UrlaubNewFragment
import com.getbase.floatingactionbutton.FloatingActionButton

class UrlaubFragment : Fragment(), View.OnClickListener {

    private lateinit var urlaubViewModel: UrlaubViewModel
    private lateinit var sharedViewModel: SharedViewModel

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

        urlaubViewModel = ViewModelProvider(this).get(UrlaubViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        urlaubList = ArrayList()
        verbraucherList = ArrayList()

        val root = inflater.inflate(R.layout.fragment_urlaub, container, false)

        //RecyclerView initialisieren
        viewAdapter = UrlaubListAdapter(urlaubList, sharedViewModel.getHaushalt().value!!)

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

        val verbraucherData: LiveData<List<Geraete>> =
            Transformations.switchMap(sharedViewModel.getHaushalt()) { haushalt ->
                urlaubViewModel.getAllVerbraucherByHaushaltID(haushalt.getHaushaltID())
            }
        verbraucherData.observe(
            viewLifecycleOwner,
            Observer { geraete ->
                if (geraete != null) {

                    verbraucherList.clear()
                    verbraucherList.addAll(geraete)
                    viewAdapter.notifyDataSetChanged();
                }
            })

        val urlaubData: LiveData<List<Urlaub>> =
            Transformations.switchMap(sharedViewModel.getHaushalt()) { haushalt ->
                urlaubViewModel.getAllUrlaubByHaushaltID(haushalt.getHaushaltID())
            }
        urlaubData.observe(
            viewLifecycleOwner,
            Observer { urlaub ->
                if (urlaub != null) {
                    urlaubList.clear()
                    urlaubList.addAll(urlaub.sortedWith(compareByDescending { it.getDateVon() }))
                    viewAdapter.notifyDataSetChanged()
                }
            })

        val currHaushaltData = Transformations.switchMap(sharedViewModel.getHaushalt()) { haushalt ->
            urlaubViewModel.getHaushaltByID(haushalt.getHaushaltID())
        }
        currHaushaltData.observe(
            viewLifecycleOwner,
            Observer { haushalt ->
                currHaushalt = haushalt
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
