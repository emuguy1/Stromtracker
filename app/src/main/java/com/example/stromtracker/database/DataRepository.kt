package com.example.stromtracker.database

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

class DataRepository public constructor(application: Application) {

    private var database = AppDatabase.getInstance(application)

    private var mGeraeteDao: GeraeteDAO = database.geraeteDao()
    private var mKategorieDAO: KategorieDAO = database.kategorieDao()
    private var mHaushaltDAO: HaushaltDAO = database.haushaltDao()
    private var mRaumDAO: RaumDAO = database.raumDao()
    private var mUrlaubDAO: UrlaubDAO = database.urlaubDAO()
    private var mAllGeraete: LiveData<List<Geraete>> = mGeraeteDao.getAll()
    private var mAllProduzenten: LiveData<List<Geraete>> = mGeraeteDao.getAllProduzenten()
    private var mAllVerbraucher: LiveData<List<Geraete>> = mGeraeteDao.getAllVerbraucher()
    private var mAllRaum: LiveData<List<Raum>> = mRaumDAO.getAll()
    private var mAllHaushalt: LiveData<List<Haushalt>> = mHaushaltDAO.getAll()
    private var mAllKategorie: LiveData<List<Kategorie>> = mKategorieDAO.getAll()
    private var mAllUrlaub: LiveData<List<Urlaub>> = mUrlaubDAO.getAll()


    fun getAllGeraete(): LiveData<List<Geraete>> {
        return mAllGeraete
    }

    fun getAllRaumByHaushaltID(id: Int): LiveData<List<Raum>> {
        return mRaumDAO.loadAllByHaushaltID(id)
    }

    fun getAllProduzenten(): LiveData<List<Geraete>> {
        return mAllProduzenten
    }

    fun getAllVerbraucher(): LiveData<List<Geraete>> {
        return mAllVerbraucher
    }

    fun getAllRaeume(): LiveData<List<Raum>> {
        return mAllRaum
    }

    fun getAllKategorie(): LiveData<List<Kategorie>> {
        return mAllKategorie
    }

    fun getAllHaushalt(): LiveData<List<Haushalt>> {
        return mAllHaushalt
    }

    fun getUrlaub():LiveData<List<Urlaub>> {
        return mAllUrlaub
    }

    fun insertGeraete(geraet: Geraete) {
        insertAsyncTaskGeraet(mGeraeteDao).execute(geraet)
    }

    fun deleteGeraete(geraet: Geraete) {
        deleteAsyncTaskGeraet(mGeraeteDao).execute(geraet)
    }

    fun updateGeraete(geraet: Geraete) {
        updateAsyncTaskGeraet(mGeraeteDao).execute(geraet)
    }

    fun insertHaushalt(haushalt: Haushalt) {
        insertAsyncTaskHaushalt(mHaushaltDAO).execute(haushalt)
    }

    fun deleteHaushalt(haushalt: Haushalt) {
        deleteAsyncTaskHaushalt(mHaushaltDAO).execute(haushalt)
    }

    fun updateHaushalt(haushalt: Haushalt) {
        updateAsyncTaskHaushalt(mHaushaltDAO).execute(haushalt)
    }

    fun insertKategorie(kategorie: Kategorie) {
        insertAsyncTaskKategorie(mKategorieDAO).execute(kategorie)
    }

    fun deleteKategorie(kategorie: Kategorie) {
        deleteAsyncTaskKategorie(mKategorieDAO).execute(kategorie)
    }

    fun updateKategorie(kategorie: Kategorie) {
        updateAsyncTaskKategorie(mKategorieDAO).execute(kategorie)
    }

    fun insertRaum(raum: Raum) {
        insertAsyncTaskRaum(mRaumDAO).execute(raum)
    }

    fun deleteRaum(raum: Raum) {
        deleteAsyncTaskRaum(mRaumDAO).execute(raum)
    }

    fun updateRaum(raum: Raum) {
        updateAsyncTaskRaum(mRaumDAO).execute(raum)
    }

    fun insertUrlaub(urlaub: Urlaub) {
        insertAsyncTaskUrlaub(mUrlaubDAO).execute(urlaub)
    }

    fun deleteUrlaub(urlaub: Urlaub) {
        deleteAsyncTaskUrlaub(mUrlaubDAO).execute(urlaub)
    }

    fun updateUrlaub(urlaub: Urlaub) {
        updateAsyncTaskUrlaub(mUrlaubDAO).execute(urlaub)
    }


    companion object {
        class insertAsyncTaskGeraet(dao: GeraeteDAO) : AsyncTask<Geraete, Void, Void>() {
            private var mAsyncTaskDAO: GeraeteDAO = dao


            override fun doInBackground(vararg params: Geraete): Void? {
                mAsyncTaskDAO.insertGeraete(params[0])
                return null
            }


        }

        class deleteAsyncTaskGeraet(dao: GeraeteDAO) : AsyncTask<Geraete, Void, Void>() {
            private var mAsyncTaskDAO: GeraeteDAO = dao


            override fun doInBackground(vararg params: Geraete): Void? {
                mAsyncTaskDAO.delete(params[0])
                return null
            }


        }

        class updateAsyncTaskGeraet(dao: GeraeteDAO) : AsyncTask<Geraete, Void, Void>() {
            private var mAsyncTaskDAO: GeraeteDAO = dao


            override fun doInBackground(vararg params: Geraete): Void? {
                mAsyncTaskDAO.updateGeraet(params[0])
                return null
            }


        }

        class insertAsyncTaskHaushalt(dao: HaushaltDAO) : AsyncTask<Haushalt, Void, Void>() {
            private var mAsyncTaskDAO: HaushaltDAO = dao


            override fun doInBackground(vararg params: Haushalt): Void? {
                mAsyncTaskDAO.insertHaushalt(params[0])
                return null
            }


        }

        class deleteAsyncTaskHaushalt(dao: HaushaltDAO) : AsyncTask<Haushalt, Void, Void>() {
            private var mAsyncTaskDAO: HaushaltDAO = dao


            override fun doInBackground(vararg params: Haushalt): Void? {
                mAsyncTaskDAO.delete(params[0])
                return null
            }


        }

        class updateAsyncTaskHaushalt(dao: HaushaltDAO) : AsyncTask<Haushalt, Void, Void>() {
            private var mAsyncTaskDAO: HaushaltDAO = dao


            override fun doInBackground(vararg params: Haushalt): Void? {
                mAsyncTaskDAO.updateHaushalt(params[0])
                return null
            }


        }

        class insertAsyncTaskKategorie(dao: KategorieDAO) : AsyncTask<Kategorie, Void, Void>() {
            private var mAsyncTaskDAO: KategorieDAO = dao


            override fun doInBackground(vararg params: Kategorie): Void? {
                mAsyncTaskDAO.insertKategorie(params[0])
                return null
            }


        }

        class deleteAsyncTaskKategorie(dao: KategorieDAO) : AsyncTask<Kategorie, Void, Void>() {
            private var mAsyncTaskDAO: KategorieDAO = dao


            override fun doInBackground(vararg params: Kategorie): Void? {
                mAsyncTaskDAO.delete(params[0])
                return null
            }


        }

        class updateAsyncTaskKategorie(dao: KategorieDAO) : AsyncTask<Kategorie, Void, Void>() {
            private var mAsyncTaskDAO: KategorieDAO = dao


            override fun doInBackground(vararg params: Kategorie): Void? {
                mAsyncTaskDAO.updateKategorie(params[0])
                return null
            }


        }

        class insertAsyncTaskRaum(dao: RaumDAO) : AsyncTask<Raum, Void, Void>() {
            private var mAsyncTaskDAO: RaumDAO = dao


            override fun doInBackground(vararg params: Raum): Void? {
                mAsyncTaskDAO.insertRaum(params[0])
                return null
            }


        }

        class deleteAsyncTaskRaum(dao: RaumDAO) : AsyncTask<Raum, Void, Void>() {
            private var mAsyncTaskDAO: RaumDAO = dao


            override fun doInBackground(vararg params: Raum): Void? {
                mAsyncTaskDAO.delete(params[0])
                return null
            }


        }

        class updateAsyncTaskRaum(dao: RaumDAO) : AsyncTask<Raum, Void, Void>() {
            private var mAsyncTaskDAO: RaumDAO = dao


            override fun doInBackground(vararg params: Raum): Void? {
                mAsyncTaskDAO.updateRaum(params[0])
                return null
            }


        }

        class insertAsyncTaskUrlaub(dao: UrlaubDAO) : AsyncTask<Urlaub, Void, Void>() {
            private var mAsyncTaskDAO: UrlaubDAO = dao


            override fun doInBackground(vararg params: Urlaub): Void? {
                mAsyncTaskDAO.insertUrlaub(params[0])
                return null
            }


        }

        class deleteAsyncTaskUrlaub(dao: UrlaubDAO) : AsyncTask<Urlaub, Void, Void>() {
            private var mAsyncTaskDAO: UrlaubDAO = dao


            override fun doInBackground(vararg params: Urlaub): Void? {
                mAsyncTaskDAO.delete(params[0])
                return null
            }


        }

        class updateAsyncTaskUrlaub(dao: UrlaubDAO) : AsyncTask<Urlaub, Void, Void>() {
            private var mAsyncTaskDAO: UrlaubDAO = dao


            override fun doInBackground(vararg params: Urlaub): Void? {
                mAsyncTaskDAO.updateUrlaub(params[0])
                return null
            }


        }

    }
}













