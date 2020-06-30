package com.example.stromtracker.database


import androidx.room.*;
import java.util.*

@Entity
data class Haushalt(
    @ColumnInfo(name = "name") private var name: String,
    @ColumnInfo(name = "stromkosten") private var stromkosten: Double,
    @ColumnInfo(name = "bewohnerAnzahl") private var bewohnerAnzahl: Int,
    @ColumnInfo(name = "zaehlerstand") private var zaehlerstand: Double?,
    @ColumnInfo(name = "datum") private var datum: Date?,
    @ColumnInfo(name = "oekostrom") private var oekostrom: Boolean
) {
    @PrimaryKey(autoGenerate = true) private var haushaltID: Int = 0

    fun getHaushaltID():Int {
        return haushaltID
    }

    fun setHaushaltID(v:Int) {
        haushaltID = v
    }

    fun getStromkosten():Double {
        return stromkosten
    }

    fun setStromkosten(s:Double) {
        stromkosten = s
    }

    fun getBewohnerAnzahl():Int {
        return bewohnerAnzahl
    }

    fun setBewohnerAnzahl(b:Int) {
        bewohnerAnzahl = b
    }

    fun getZaehlerstand():Double? {
        return zaehlerstand
    }

    fun setZaehlerstand(z:Double) {
        zaehlerstand = z
    }

    fun getDatum():Date? {
        return datum
    }

    fun setDatum(d:Date) {
        datum = d
    }

    fun getOekostrom():Boolean {
        return oekostrom
    }

    fun setOekostrom(o:Boolean) {
        oekostrom = o
    }

    fun getName(): String {
        return name
    }

    fun setName(n: String) {
        name = n
    }
}



