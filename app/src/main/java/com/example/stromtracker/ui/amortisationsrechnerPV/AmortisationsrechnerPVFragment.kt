package com.example.stromtracker.ui.amortisationsrechnerPV
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.R
import com.example.stromtracker.ui.amortisationsrechnerPV.AmortisationsrechnerPVViewModel

class AmortisationsrechnerPVFragment : Fragment() {

    private lateinit var amortisationsrechnerPVViewModel: AmortisationsrechnerPVViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        amortisationsrechnerPVViewModel =
            ViewModelProviders.of(this).get(com.example.stromtracker.ui.amortisationsrechnerPV.AmortisationsrechnerPVViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_geraete, container, false)
        val textView: TextView = root.findViewById(R.id.text_geraete)
        amortisationsrechnerPVViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}