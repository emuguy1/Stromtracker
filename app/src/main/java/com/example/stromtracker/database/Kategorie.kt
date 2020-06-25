package com.example.stromtracker.database

import androidx.room.*;

@Entity
data class Kategorie(
    @ColumnInfo(name = "name") private var name: String
    )
{
    @PrimaryKey(autoGenerate = true) private var kategorieID: Int = 0


    fun getKategorieID():Int {
        return kategorieID
    }

    fun setKategorieID(k:Int) {
        kategorieID = k
    }

    fun getName():String {
        return name
    }

    fun setName(n:String) {
        name = n
    }

}