package com.example.stromtracker.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao interface UrlaubDAO {
    @Query("SELECT * FROM urlaub")
    fun getAll(): LiveData<List<Urlaub>>

    @Query("SELECT * FROM urlaub WHERE urlaubID IN (:urlaubID)")
    fun loadAllByIds(urlaubID: IntArray): LiveData<List<Urlaub>>

    @Insert
    fun insertUrlaub(vararg urlaub: Urlaub)

    @Delete
    fun delete(urlaub: Urlaub)

    @Query("DELETE FROM urlaub WHERE urlaubID = :urlaubID")
    fun deleteByUrlaubId(urlaubID: Int)

    @Update
    fun updateUrlaub(urlaub: Urlaub)
}
