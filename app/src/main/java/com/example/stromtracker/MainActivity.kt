package com.example.stromtracker

import android.os.Bundle
import android.view.Menu
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

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val iconArrayList:Array<Int> =
        arrayOf(R.drawable.ic_kategorien_monitor, R.drawable.ic_kategorien_joystick, R.drawable.ic_kategorien_speaker,
            R.drawable.ic_kategorien_refrigerator, R.drawable.ic_kategorie_oven, R.drawable.ic_kategorien_washing_machine,
            R.drawable.ic_kategorien_light, R.drawable.ic_kategorien_plug, R.drawable.ic_menu_amortrechnerpv)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)


        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val items = arrayOf("Haushalt1", "Haushalt2", "Haushalt3", "Haushalt4")

        val sp: Spinner = navView.menu.findItem(R.id.nav_haushalt).actionView as Spinner
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items)
        sp.adapter = adapter


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

    fun getIconArray () : Array<Int> {
        return iconArrayList
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