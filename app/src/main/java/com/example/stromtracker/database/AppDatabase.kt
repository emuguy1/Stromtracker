package com.example.stromtracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(
    entities = arrayOf(Geraete::class, Haushalt::class, Kategorie::class, Raum::class),
    version = 1
)
@TypeConverters(DateConverters::class)

abstract class AppDatabase : RoomDatabase() {

    abstract fun geraeteDao(): GeraeteDAO
    abstract fun haushaltDao(): HaushaltDAO
    abstract fun kategorieDao(): KategorieDAO
    abstract fun raumDao(): RaumDAO

    companion object {
        private var INSTANCE: AppDatabase? = null

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
                    Thread(Runnable { prepopulateDb(context, getInstance(context)) }
                    ).start()

                }
            })
                .build()

        }

        fun destroyInstance() {
            INSTANCE = null
        }

        fun prepopulateDb(context: Context, db: AppDatabase) {
            db.haushaltDao().insertHaushalt(
                Haushalt("Haushalt1", 26.5, 1, null, null, false)
            )
            db.raumDao().insertRaum(Raum("Sonstiges", 1))
            db.raumDao().insertRaum(Raum("Wohnzimmer", 1))
            db.raumDao().insertRaum(Raum("Schlafzimmer", 1))
            db.kategorieDao().insertKategorie(
                Kategorie("Sonstiges", 7),
                Kategorie("Fernseher", 0),
                Kategorie("Gaming", 1),
                Kategorie("Unterhaltung", 2),
                Kategorie("Kühlung", 3),
                Kategorie("Kochen", 4),
                Kategorie("Waschen", 5),
                Kategorie("Lampen", 6),
                Kategorie("Stromerzeugung", 8)
            )
            return

        }
    }


}
