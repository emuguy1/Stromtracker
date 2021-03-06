package com.example.stromtracker

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.ui.SharedViewModel
import com.example.stromtracker.ui.haushalt.HaushaltFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var sp: Spinner

    private val iconArrayList: Array<Int> =
        arrayOf(
            R.drawable.ic_kategorien_monitor,
            R.drawable.ic_kategorien_joystick,
            R.drawable.ic_kategorien_speaker,
            R.drawable.ic_kategorien_refrigerator,
            R.drawable.ic_kategorie_oven,
            R.drawable.ic_kategorien_washing_machine,
            R.drawable.ic_kategorien_light,
            R.drawable.ic_kategorien_plug,
            R.drawable.ic_menu_amortrechnerpv
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val sharedViewModel: SharedViewModel =
            ViewModelProvider(this).get(SharedViewModel::class.java)
        val haushaltItems: ArrayList<Haushalt> = ArrayList()
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        sp = navView.menu.findItem(R.id.nav_haushalt).actionView as Spinner

        sharedViewModel.getAllHaushalt().observe(
            this,
            Observer { haushalte ->
                if (haushalte != null) {

                    haushaltItems.clear()
                    haushaltItems.addAll(haushalte)
                    sp = navView.menu.findItem(R.id.nav_haushalt).actionView as Spinner
                    val adapter = ArrayAdapter<Haushalt>(
                        this,
                        R.layout.nav_spinner_row,
                        haushaltItems
                    )
                    sp.adapter = adapter

                    sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            v: View?,
                            pos: Int,
                            id: Long
                        ) {
                            try {
                                sharedViewModel.setHaushalt(haushaltItems[pos])
                            } catch (e: Exception) {
                                Log.d("Error", "sharedViewModelException")
                            }
                        }
                    }
                }
            })

        // Alte Haushaltliste initialisieren um damit auf neue Haushalte überprüfen
        HaushaltFragment.setOldHaushaltList(haushaltItems)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_urlaub, R.id.nav_geraete, R.id.nav_home,
                R.id.nav_haushalt, R.id.nav_haushalt, R.id.nav_geraete,
                R.id.nav_kategorien, R.id.nav_raeume, R.id.nav_verbrauchsrechner,
                R.id.nav_amortisationsrechner, R.id.nav_amortisationsrechnerPV,
                R.id.nav_co2bilanz, R.id.nav_importexport
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun getIconArray(): Array<Int> {
        return iconArrayList
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
