package com.example.stromtracker.database

import androidx.room.*

@Entity(
    indices = [Index("haushaltID")], foreignKeys = [
        ForeignKey(
            entity = Haushalt::class,
            parentColumns = arrayOf("haushaltID"),
            childColumns = arrayOf("haushaltID"),
            onDelete = ForeignKey.CASCADE
        )]
)
data class Raum(
    @ColumnInfo(name = "name") private var name: String,
    @ColumnInfo(name = "haushaltID") private var haushaltID: Int

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

    fun getHaushaltID(): Int {
        return haushaltID
    }

    fun setHaushaltID(r: Int) {
        haushaltID = r
    }

    override fun toString(): String {
        return name
    }
}
