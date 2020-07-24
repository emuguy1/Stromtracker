package com.example.stromtracker.database

import androidx.room.*
import java.util.*

@Entity(
    indices = [Index("haushaltID")], foreignKeys = [
        ForeignKey(
            entity = Haushalt::class,
            parentColumns = arrayOf("haushaltID"),
            childColumns = arrayOf("haushaltID"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Urlaub(
    @ColumnInfo(name = "name") private var name: String,
    @ColumnInfo(name = "DateVon") private var dateVon: Date,
    @ColumnInfo(name = "DateBis") private var dateBis: Date,
    @ColumnInfo(name = "ersparnisProTag") private var ersparnisProTag: Double,
    @ColumnInfo(name = "haushaltID") private var haushaltID: Int
) {
    @PrimaryKey(autoGenerate = true)
    private var urlaubID: Int = 0

    fun getName(): String {
        return name
    }

    fun setName(n: String) {
        name = n
    }

    fun getDateVon(): Date {
        return dateVon
    }

    fun setDateVon(d: Date) {
        dateVon = d
    }

    fun getDateBis(): Date {
        return dateBis
    }

    fun setDateBis(d: Date) {
        dateBis = d
    }

    fun getUrlaubID(): Int {
        return urlaubID
    }

    fun setUrlaubID(id: Int) {
        urlaubID = id
    }

    fun getErsparnisProTag(): Double {
        return ersparnisProTag
    }

    fun setErsparnisProTag(g: Double) {
        ersparnisProTag = g
    }

    fun getHaushaltID(): Int {
        return haushaltID
    }

    fun setHaushaltID(id: Int) {
        haushaltID = id
    }


}
