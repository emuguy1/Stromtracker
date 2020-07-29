package com.example.stromtracker.ui.importexport.Import

import com.example.stromtracker.database.*
import java.util.*

class CompanionImport {
    private lateinit var haushaltaltlist: ArrayList<Haushalt>
    private lateinit var geraetealtlist: ArrayList<Geraete>
    private lateinit var kategoriealtlist: ArrayList<Kategorie>
    private lateinit var raeumealtlist: ArrayList<Raum>
    private lateinit var urlaubaltlist: ArrayList<Urlaub>

    fun setHaushaltaltlist(list: ArrayList<Haushalt>) {
        haushaltaltlist = list
    }

    fun getHaushaltaltlist(): ArrayList<Haushalt> {
        return haushaltaltlist
    }

    fun setraeumealtlist(list: ArrayList<Raum>) {
        raeumealtlist = list
    }

    fun setkategorienaltlist(list: ArrayList<Kategorie>) {
        kategoriealtlist = list
    }

    fun getkategoriealtlist(): ArrayList<Kategorie> {
        return kategoriealtlist
    }

    fun setgeraetealtlist(list: ArrayList<Geraete>) {
        geraetealtlist = list
    }

    fun getraeumealtlist(): ArrayList<Raum> {
        return raeumealtlist
    }

    fun seturlaubaltlist(list: ArrayList<Urlaub>) {
        urlaubaltlist = list
    }

}
