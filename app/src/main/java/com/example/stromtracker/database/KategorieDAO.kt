package com.example.stromtracker.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao interface KategorieDAO {
        @Query("SELECT * FROM kategorie ORDER BY name ASC")
        fun getAll(): LiveData<List<Kategorie>>

        @Query("SELECT * FROM kategorie WHERE kategorieID IN (:kategorieID)")
        fun loadAllByIds(kategorieID: IntArray): LiveData<List<Kategorie>>

        @Query("SELECT * FROM kategorie WHERE name LIKE :name LIMIT 1")
        fun findByName(name: String): Kategorie

        @Insert
        fun insertKategorie(vararg kategorie: Kategorie)

        @Delete
        fun delete(kategorie: Kategorie)

        @Query("DELETE FROM kategorie WHERE kategorieID = :kategorieID")
        fun deleteByKategorieId(kategorieID: Int)

        @Update
        fun updateKategorie(kategorie: Kategorie)
    }
