package com.example.stromtracker.database

import androidx.room.*;

@Entity
data class Kategorie(
    @ColumnInfo(name = "name") private var name: String,
    @ColumnInfo(name = "icon") private var icon: Int?

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

    fun getIcon():Int? {
        return icon
    }

    fun setIcon(i:Int) {
        icon = i
    }

}