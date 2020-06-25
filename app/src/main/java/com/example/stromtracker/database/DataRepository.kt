package com.example.stromtracker.database

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

class DataRepository public constructor(application: Application) {

    private var database = AppDatabase.getInstance(application)

        private var mGeraeteDao: GeraeteDAO = database!!.geraeteDao()
        private var mKategorieDAO: KategorieDAO = database!!.kategorieDao()
        private var mHaushaltDAO: HaushaltDAO = database!!.haushaltDao()
        private var mRaumDAO: RaumDAO = database!!.raumDao()
        private var mAllGeraete: LiveData<List<Geraete>> = mGeraeteDao.getAll()
        private var mAllRaum: LiveData<List<Raum>> = mRaumDAO.getAll()
        private var mAllHaushalt: LiveData<List<Haushalt>> = mHaushaltDAO.getAll()
        private var mAllKategorie: LiveData<List<Kategorie>> = mKategorieDAO.getAll()



    fun getAllGeraete():LiveData<List<Geraete>> {
        return mAllGeraete
    }

    fun getAllRaeume():LiveData<List<Raum>> {
        return mAllRaum
    }

    fun getAllKategorie():LiveData<List<Kategorie>> {
        return mAllKategorie
    }

    fun getAllHaushalt():LiveData<List<Haushalt>> {
        return mAllHaushalt
    }

    fun insertGeraete(geraet:Geraete) {
        insertAsyncTaskGeraet(mGeraeteDao).execute(geraet)
    }

    fun deleteGeraete(geraet:Geraete) {
        deleteAsyncTaskGeraet(mGeraeteDao).execute(geraet)
    }

    fun updateGeraete(geraet:Geraete) {
        updateAsyncTaskGeraet(mGeraeteDao).execute(geraet)
    }

    fun insertHaushalt(Haushalt:Haushalt) {
        insertAsyncTaskHaushalt(mHaushaltDAO).execute(Haushalt)
    }

    fun deleteHaushalt(Haushalt:Haushalt) {
        deleteAsyncTaskHaushalt(mHaushaltDAO).execute(Haushalt)
    }

    fun updateHaushalt(Haushalt:Haushalt) {
        updateAsyncTaskHaushalt(mHaushaltDAO).execute(Haushalt)
    }

    fun insertKategorie(Kategorie:Kategorie) {
        insertAsyncTaskKategorie(mKategorieDAO).execute(Kategorie)
    }

    fun deleteKategorie(Kategorie:Kategorie) {
        deleteAsyncTaskKategorie(mKategorieDAO).execute(Kategorie)
    }

    fun updateKategorie(Kategorie:Kategorie) {
        updateAsyncTaskKategorie(mKategorieDAO).execute(Kategorie)
    }

    fun insertRaum(Raum:Raum) {
        insertAsyncTaskRaum(mRaumDAO).execute(Raum)
    }

    fun deleteRaum(Raum:Raum) {
        deleteAsyncTaskRaum(mRaumDAO).execute(Raum)
    }

    fun updateRaum(Raum:Raum) {
        updateAsyncTaskRaum(mRaumDAO).execute(Raum)
    }


    companion object {
        class insertAsyncTaskGeraet(dao: GeraeteDAO) : AsyncTask<Geraete, Void, Void>() {
            private  var mAsyncTaskDAO: GeraeteDAO = dao


            override fun doInBackground(vararg params: Geraete): Void? {
                mAsyncTaskDAO.insertGeraete(params[0])
                return null
            }



        }

        class deleteAsyncTaskGeraet(dao: GeraeteDAO) : AsyncTask<Geraete, Void, Void>() {
            private  var mAsyncTaskDAO: GeraeteDAO = dao


            override fun doInBackground(vararg params: Geraete): Void? {
                mAsyncTaskDAO.delete(params[0])
                return null
            }



        }

        class updateAsyncTaskGeraet(dao: GeraeteDAO) : AsyncTask<Geraete, Void, Void>() {
            private  var mAsyncTaskDAO: GeraeteDAO = dao


            override fun doInBackground(vararg params: Geraete): Void? {
                mAsyncTaskDAO.updateGeraet(params[0])
                return null
            }



        }

        class insertAsyncTaskHaushalt(dao: HaushaltDAO) : AsyncTask<Haushalt, Void, Void>() {
            private  var mAsyncTaskDAO: HaushaltDAO = dao


            override fun doInBackground(vararg params: Haushalt): Void? {
                mAsyncTaskDAO.insertHaushalt(params[0])
                return null
            }



        }

        class deleteAsyncTaskHaushalt(dao: HaushaltDAO) : AsyncTask<Haushalt, Void, Void>() {
            private  var mAsyncTaskDAO: HaushaltDAO = dao


            override fun doInBackground(vararg params: Haushalt): Void? {
                mAsyncTaskDAO.delete(params[0])
                return null
            }



        }

        class updateAsyncTaskHaushalt(dao: HaushaltDAO) : AsyncTask<Haushalt, Void, Void>() {
            private  var mAsyncTaskDAO: HaushaltDAO = dao


            override fun doInBackground(vararg params: Haushalt): Void? {
                mAsyncTaskDAO.updateHaushalt(params[0])
                return null
            }



        }

        class insertAsyncTaskKategorie(dao: KategorieDAO) : AsyncTask<Kategorie, Void, Void>() {
            private  var mAsyncTaskDAO: KategorieDAO = dao


            override fun doInBackground(vararg params: Kategorie): Void? {
                mAsyncTaskDAO.insertKategorie(params[0])
                return null
            }



        }

        class deleteAsyncTaskKategorie(dao: KategorieDAO) : AsyncTask<Kategorie, Void, Void>() {
            private  var mAsyncTaskDAO: KategorieDAO = dao


            override fun doInBackground(vararg params: Kategorie): Void? {
                mAsyncTaskDAO.delete(params[0])
                return null
            }



        }

        class updateAsyncTaskKategorie(dao: KategorieDAO) : AsyncTask<Kategorie, Void, Void>() {
            private  var mAsyncTaskDAO: KategorieDAO = dao


            override fun doInBackground(vararg params: Kategorie): Void? {
                mAsyncTaskDAO.updateKategorie(params[0])
                return null
            }



        }

        class insertAsyncTaskRaum(dao: RaumDAO) : AsyncTask<Raum, Void, Void>() {
            private  var mAsyncTaskDAO: RaumDAO = dao


            override fun doInBackground(vararg params: Raum): Void? {
                mAsyncTaskDAO.insertRaum(params[0])
                return null
            }



        }

        class deleteAsyncTaskRaum(dao: RaumDAO) : AsyncTask<Raum, Void, Void>() {
            private  var mAsyncTaskDAO: RaumDAO = dao


            override fun doInBackground(vararg params: Raum): Void? {
                mAsyncTaskDAO.delete(params[0])
                return null
            }



        }

        class updateAsyncTaskRaum(dao: RaumDAO) : AsyncTask<Raum, Void, Void>() {
            private  var mAsyncTaskDAO: RaumDAO = dao


            override fun doInBackground(vararg params: Raum): Void? {
                mAsyncTaskDAO.updateRaum(params[0])
                return null
            }



        }




    }
}













