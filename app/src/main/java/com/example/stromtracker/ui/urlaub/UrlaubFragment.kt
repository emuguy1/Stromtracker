package com.example.stromtracker.ui.urlaub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R
import com.example.stromtracker.database.Geraete
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.ui.geraete.GeraeteViewModel
import com.example.stromtracker.ui.urlaub.urlaub_new.UrlaubNewFragment
import com.getbase.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class UrlaubFragment : Fragment() , View.OnClickListener{

    private lateinit var urlaubViewModel: UrlaubViewModel

    private lateinit var verbraucherList:ArrayList<Geraete>
    private lateinit var currHaushalt : Haushalt

    private lateinit var addUrlaub : FloatingActionButton

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        //TODO auf Livedata umsteigen (Siehe branch current_haushalt)
        val navView = requireActivity().findViewById<NavigationView>(R.id.nav_view)

        val sp: Spinner = navView.menu.findItem(R.id.nav_haushalt).actionView as Spinner
        currHaushalt = sp.selectedItem as Haushalt

        verbraucherList = ArrayList()

        urlaubViewModel =
                ViewModelProviders.of(this).get(UrlaubViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_urlaub, container, false)

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
                    //viewAdapter.notifyDataSetChanged();
                }
            })
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.urlaub_button_add -> {
                val frag = UrlaubNewFragment(verbraucherList, currHaushalt)
                val fragMan = parentFragmentManager
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).addToBackStack(null).commit()
            }
        }
    }
}