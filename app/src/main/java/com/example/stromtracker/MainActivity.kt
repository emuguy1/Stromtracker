package com.example.stromtracker

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.example.stromtracker.database.DataRepository
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModelProviders
import com.example.stromtracker.database.Geraete
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.ui.SharedViewModel
import com.example.stromtracker.ui.geraete.GeraeteViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var sp:Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        var geraeteViewModel: GeraeteViewModel =  ViewModelProviders.of(this).get(GeraeteViewModel::class.java)
        var sharedViewModel: SharedViewModel =  ViewModelProviders.of(this).get(SharedViewModel::class.java)



        val haushaltItems:ArrayList<Haushalt> = ArrayList()





        setSupportActionBar(toolbar)



        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        sp= navView.menu.findItem(R.id.nav_haushalt).actionView as Spinner


            sharedViewModel.getAllHaushalt().observe(
                this,
                Observer { haushalte ->
                    if (haushalte != null) {

                        haushaltItems.clear()
                        haushaltItems.addAll(haushalte)
                        sp= navView.menu.findItem(R.id.nav_haushalt).actionView as Spinner
                        val adapter = ArrayAdapter<Haushalt>(this, android.R.layout.simple_spinner_dropdown_item, haushaltItems)
                        sp.adapter = adapter
                        sp.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                            }

                            override fun onItemSelected(parent: AdapterView<*>, v: View, pos: Int, id: Long) {
                                sharedViewModel.setHaushalt(haushaltItems[pos])
                            }



                        }

                    }
                })

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_geraete,
            R.id.nav_haushalt,R.id.nav_haushalt, R.id.nav_geraete,
            R.id.nav_kategorien,R.id.nav_raeume,R.id.nav_verbrauchsrechner,
            R.id.nav_amortisationsrechner,R.id.nav_amortisationsrechnerPV,
            R.id.nav_co2bilanz,R.id.nav_importexport), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}