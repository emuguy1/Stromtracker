package com.example.stromtracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = arrayOf(Geraete::class, Haushalt::class, Kategorie::class, Raum::class), version = 1)
@TypeConverters(DateConverters::class)

abstract class AppDatabase : RoomDatabase() {

    abstract fun geraeteDao(): GeraeteDAO
    abstract fun haushaltDao(): HaushaltDAO
    abstract fun kategorieDao():KategorieDAO
    abstract fun raumDao():RaumDAO

    companion object {
        private   var INSTANCE: AppDatabase? = null

        open fun getInstance(context: Context): AppDatabase? {


            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "AppDatabase"
                )
                    .build()
            }
            return INSTANCE
        }
    }

    open fun destroyInstance() {
        INSTANCE = null
    }

}