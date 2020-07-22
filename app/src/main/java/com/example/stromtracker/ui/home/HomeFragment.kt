package com.example.stromtracker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.stromtracker.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.home_text_def)
        val text: String =
            "Die App soll den Stromverbrauch eines Haushaltes erfassen, visualisieren und Kosten und Nutzen einzelner Geräte und des gesamten Haushaltes errechnen. Hierzu muss der User selbst die Verbrauchsdaten seiner Geräte messen/ablesen und in die App eintragen. Außerdem beinhaltet die App Amortisationsrechner, um die Kostenvorteile von Solaranlagen oder effizienteren Geräten abzuschätzen.\n" +
                    "\n" +
                    "Es werden ein Standardhaushalt mit mehreren Standardräumen, sowie mehrere Standardkategorien erstellt. Falls ein Haushalt gelöscht wird, werden alle darin enthaltenen Räume und Geräte ebenfalls gelöscht.\n" +
                    "\n" +
                    "By Tobias Reithmaier Matteo Hoffmann Emanuel Erben"
        textView.text = text

        return root
    }
}