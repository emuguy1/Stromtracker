package com.example.stromtracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = arrayOf(Geraete::class, Haushalt::class, Kategorie::class, Raum::class), version = 1)
@TypeConverters(DateConverters::class)

abstract class AppDatabase : RoomDatabase() {

    abstract fun geraeteDao(): GeraeteDAO
    abstract fun haushaltDao(): HaushaltDAO
    abstract fun kategorieDao():KategorieDAO
    abstract fun raumDao():RaumDAO

    companion object {
        private  var INSTANCE: AppDatabase? = null

         fun getInstance(context: Context): AppDatabase {

            synchronized(this) {


                if (INSTANCE == null) {
                     buildDatabase(context)
                }
                return INSTANCE!!
            }
        }

        private fun buildDatabase(context: Context) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "AppDatabase"
            ).addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Thread(Runnable { prepopulateDb(context, getInstance(context)) }).start()
                }
            })
                .build()

        }
         fun destroyInstance() {
            INSTANCE = null
        }

        fun prepopulateDb(context: Context, db: AppDatabase) {
            db.haushaltDao().insertHaushalt(Haushalt("name", 0.0, 1, 0.0, null, false), Haushalt("test2", 0.0, 1, 0.0, null, false))
            db.raumDao().insertRaum(Raum("test", 1))
        }
    }



}
