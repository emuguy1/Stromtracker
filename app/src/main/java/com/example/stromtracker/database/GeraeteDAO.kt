package com.example.stromtracker.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface GeraeteDAO {
    @Query("SELECT * FROM geraete")
    fun getAll(): LiveData<List<Geraete>>

    @Query("SELECT * FROM geraete WHERE geraeteID IN (:geraeteIDs)")
    fun loadAllByIds(geraeteIDs: IntArray): LiveData<List<Geraete>>

    @Query("SELECT * FROM geraete WHERE name LIKE :name")
    fun findByName(name: String): List<Geraete>

    /*@Insert
    fun insertGeraete(vararg geraete: Geraete)
    */
     /*@Query("INSERT INTO geraete(name, kategorieID, raumID, haushaltID, stromVollast, stromStandBy, betriebszeit, urlaubsmodus, notiz) VALUES " +
             "(':name, (SELECT kategorieID FROM kategorie WHERE kategorieID = :kategorieID, (SELECT raumID FROM raum WHERE raumID = :raumID, (SELECt haushaltID FROM haushalt WHERE haushaltID = :haushaltID" +
     "), :stromVollast, :stromStandby, :betriebszeit, :urlaubsmodus; :notiz")
     fun insertGeraet(name:String, kategorieID:Int, raumID:Int, stromVollast:Int, stromStandby:Int, betriebszeit:Int, urlaubsmodus:Boolean, notiz:String?)
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertGeraete(vararg geraete: Geraete)


    @Delete
    fun delete(geraete: Geraete)

    @Query("DELETE FROM geraete WHERE geraeteID = :geraeteId")
    fun deleteByGeraeteId(geraeteId: Int)

    @Update
    fun updateGeraet(geraete: Geraete)
}
