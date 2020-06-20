package com.example.stromtracker.database

import androidx.lifecycle.LiveData
import androidx.room.*;


@Dao
interface GerateDAO {
    @Query("SELECT * FROM geraete")
    fun getAll(): LiveData<List<Geraete>>

    @Query("SELECT * FROM geraete WHERE geraeteID IN (:geraeteIDs)")
    fun loadAllByIds(geraeteIDs: IntArray): LiveData<List<Geraete>>

    @Query("SELECT * FROM Geraete WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): Geraete

    @Insert
    fun insertAll(vararg geraete: Geraete)

    @Delete
    fun delete(geraete: Geraete)
}
