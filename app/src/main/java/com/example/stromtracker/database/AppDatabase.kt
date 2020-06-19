package com.example.stromtracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = arrayOf(Geraete::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun geraeteDao(): GerateDAO
    private var instance: AppDatabase? = null

    companion object {
        private var INSTANCE: AppDatabase? = null

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
