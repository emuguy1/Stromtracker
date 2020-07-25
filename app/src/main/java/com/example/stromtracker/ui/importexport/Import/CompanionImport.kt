package com.example.stromtracker.ui.importexport.Import

import androidx.lifecycle.LiveData
import com.example.stromtracker.database.*
import com.example.stromtracker.ui.importexport.ImportExportViewModel

class CompanionImport(viewModel: ImportExportViewModel) {
    private var viewModel = viewModel
    private lateinit var haushaltaltlist: ArrayList<Haushalt>
    private lateinit var geraetealtlist: ArrayList<Geraete>
    private lateinit var geraeteneulist: ArrayList<Geraete>
    private lateinit var kategoriealtlist: ArrayList<Kategorie>
    private lateinit var kategorieneulist: ArrayList<Kategorie>
    private lateinit var raeumealtlist: ArrayList<Raum>
    private lateinit var raeumeneulist: ArrayList<Raum>
    private lateinit var urlaubaltlist: ArrayList<Urlaub>
    private lateinit var urlaubneulist: ArrayList<Urlaub>

    fun setHaushaltaltlist(list: ArrayList<Haushalt>) {
        haushaltaltlist = list
    }

    fun getHaushaltaltlist(): ArrayList<Haushalt> {
        return haushaltaltlist
    }

    fun getHaushaltneulist(): LiveData<List<Haushalt>> {
        return viewModel.getAllHaushalt()
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

    fun getKategorieneulist(): LiveData<List<Kategorie>> {
        return viewModel.getAllKategorie()
    }

    fun setgeraetealtlist(list: ArrayList<Geraete>) {
        geraetealtlist = list
    }

    fun getgeraetealtlist(): ArrayList<Geraete> {
        return geraetealtlist
    }

    fun getgeraetenulist(): LiveData<List<Geraete>> {
        return viewModel.getAllGeraete()
    }

    fun seturlaubaltlist(list: ArrayList<Urlaub>) {
        urlaubaltlist = list
    }

    fun geturlaubaltlist(): ArrayList<Urlaub> {
        return urlaubaltlist
    }

    fun geturlaubnulist(): LiveData<List<Urlaub>> {
        return viewModel.getAllUrlaub()
    }

}
