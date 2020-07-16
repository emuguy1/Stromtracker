package com.example.stromtracker.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RaumDAO {
    @Query("SELECT * FROM raum")
    fun getAll(): LiveData<List<Raum>>

    @Query("SELECT * FROM raum WHERE raumID IN (:raumID)")
    fun loadAllByIds(raumID: IntArray): LiveData<List<Raum>>

    @Query("SELECT * FROM raum WHERE haushaltID = :haushaltID")
    fun loadAllByHaushaltID(haushaltID: Int): LiveData<List<Raum>>

    @Query("SELECT * FROM raum WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Raum

    @Insert
    fun insertRaum(vararg raum: Raum)

    @Delete
    fun delete(raum: Raum)

    @Query("DELETE FROM raum WHERE raumID = :raumID")
    fun deleteByRaumId(raumID: Int)

    @Update
    fun updateRaum(raum: Raum)
}
