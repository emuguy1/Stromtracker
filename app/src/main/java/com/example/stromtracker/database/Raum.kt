package com.example.stromtracker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Raum(
    @ColumnInfo(name = "name") private var name: String
) {
    @PrimaryKey(autoGenerate = true)
    private var raumID: Int = 0

    fun getRaumID(): Int {
        return raumID
    }

    fun setRaumID(k: Int) {
        raumID = k
    }

    fun getName(): String {
        return name
    }

    fun setName(n: String) {
        name = n
    }
}


