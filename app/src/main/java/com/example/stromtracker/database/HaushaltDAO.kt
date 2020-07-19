package com.example.stromtracker.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HaushaltDAO {
    @Query("SELECT * FROM haushalt")
    fun getAll(): LiveData<List<Haushalt>>

    @Query("SELECT * FROM haushalt WHERE haushaltID IN (:haushaltID)")
    fun loadAllByIds(haushaltID: IntArray): LiveData<List<Haushalt>>

    @Query("SELECT * FROM haushalt WHERE name LIKE :name ")
    fun findByName(name: String): Haushalt

    @Insert
    fun insertHaushalt(vararg haushalt: Haushalt)

    @Delete
    fun delete(haushalt: Haushalt)

    @Query("DELETE FROM haushalt WHERE haushaltID = :haushaltID")
    fun deleteByHaushaltId(haushaltID: Int)

    @Update
    fun updateHaushalt(haushalt: Haushalt)
}
