package com.example.stromtracker.ui.importexport.Import

import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.database.Kategorie
import com.example.stromtracker.database.Raum
import java.util.*

class CompanionImport {
    companion object {
        private lateinit var haushaltaltlist: ArrayList<Haushalt>
        private lateinit var kategoriealtlist: ArrayList<Kategorie>
        private lateinit var raeumealtlist: ArrayList<Raum>

        fun setHaushaltaltlist(list: ArrayList<Haushalt>) {
            haushaltaltlist = list
        }

        fun getHaushaltaltlist(): ArrayList<Haushalt> {
            return haushaltaltlist
        }

        fun setraeumealtlist(list: ArrayList<Raum>) {
            raeumealtlist = list
        }

        fun getraeumealtlist(): ArrayList<Raum> {
            return raeumealtlist
        }

        fun setkategorienaltlist(list: ArrayList<Kategorie>) {
            kategoriealtlist = list
        }

        fun getkategoriealtlist(): ArrayList<Kategorie> {
            return kategoriealtlist
        }

    }
}
