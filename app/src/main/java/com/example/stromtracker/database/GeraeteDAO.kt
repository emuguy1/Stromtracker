package com.example.stromtracker.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface GerateDAO {
    @Query("SELECT * FROM geraete")
    fun getAll(): LiveData<List<Geraete>>

    @Query("SELECT * FROM geraete WHERE geraeteID IN (:geraeteIDs)")
    fun loadAllByIds(geraeteIDs: IntArray): LiveData<List<Geraete>>

    @Query("SELECT * FROM geraete WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Geraete

    @Insert
    fun insertAll(vararg geraete: Geraete)

    @Delete
    fun delete(geraete: Geraete)

    @Query("DELETE FROM geraete WHERE geraeteID = :geraeteId")
    fun deleteByGeraeteId(geraeteId: Int)

    @Update
    fun updateGeraet(geraete: Geraete)
}