package com.example.stromtracker.database

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import java.lang.Exception
import java.lang.RuntimeException

class DataRepository(application: Application) {

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

    fun getAllVerbraucherByHaushaltID(haushaltID: Int): LiveData<List<Geraete>> {
        return mGeraeteDao.getAllVerbraucherByHaushaltID(haushaltID)
    }

    fun getAllProduzentenByHaushaltID(haushaltID: Int): LiveData<List<Geraete>> {
        return mGeraeteDao.getAllProduzentenByHaushaltID(haushaltID)
    }

    fun getAllGeraeteByHaushaltID(id: Int): LiveData<List<Geraete>> {
        return mGeraeteDao.loadAllByHaushaltID(id)
    }


    fun getAllRaumByHaushaltID(id: Int): LiveData<List<Raum>> {
        return mRaumDAO.loadAllByHaushaltID(id)
    }

    fun getAllUrlaubByHaushaltID(id: Int): LiveData<List<Urlaub>> {
        return mUrlaubDAO.getAllUrlaubByHaushaltID(id)
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

    fun getHaushaltByID(id: Int): LiveData<Haushalt> {
        return mHaushaltDAO.getHaushaltByID(id)
    }

    fun getAllUrlaub(): LiveData<List<Urlaub>> {
        return mAllUrlaub
    }

    fun insertGeraete(geraet: Geraete) {
        insertAsyncTaskGeraet(mGeraeteDao).execute(geraet)
    }

    fun deleteGeraete(geraet: Geraete) {
        deleteAsyncTaskGeraet(mGeraeteDao).execute(geraet)
    }

    fun updateGeraetByRaumID(oldID: Int, newID: Int) {
        updateAsyncTaskGeraetByRaumID(mGeraeteDao).execute(oldID, newID)
    }

    fun updateGeraete(geraet: Geraete) {
        updateAsyncTaskGeraet(mGeraeteDao).execute(geraet)
    }

    fun updateGeraetByKategorieID(oldID: Int, newID: Int) {
        updateAsyncTaskGeraetByKategorieID(mGeraeteDao).execute(oldID, newID)
    }

    fun insertHaushalt(Haushalt: Haushalt) {
        insertAsyncTaskHaushalt(mHaushaltDAO).execute(Haushalt)
    }

    fun deleteHaushalt(Haushalt: Haushalt) {
        deleteAsyncTaskHaushalt(mHaushaltDAO).execute(Haushalt)
    }

    fun updateHaushalt(Haushalt: Haushalt) {
        updateAsyncTaskHaushalt(mHaushaltDAO).execute(Haushalt)
    }

    fun insertKategorie(Kategorie: Kategorie) {
        insertAsyncTaskKategorie(mKategorieDAO).execute(Kategorie)
    }

    fun deleteKategorie(Kategorie: Kategorie) {
        deleteAsyncTaskKategorie(mKategorieDAO).execute(Kategorie)
    }

    fun updateKategorie(Kategorie: Kategorie) {
        updateAsyncTaskKategorie(mKategorieDAO).execute(Kategorie)
    }

    fun insertRaum(Raum: Raum) {
        try {
            insertAsyncTaskRaum(mRaumDAO).execute(Raum)
        } catch (e: Exception) {
            Log.d("TAGError", e.toString())
        }
    }

    fun deleteRaum(Raum: Raum) {
        deleteAsyncTaskRaum(mRaumDAO).execute(Raum)
    }

    fun updateRaum(Raum: Raum) {
        updateAsyncTaskRaum(mRaumDAO).execute(Raum)
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

        class updateAsyncTaskGeraetByRaumID(dao: GeraeteDAO) : AsyncTask<Int, Void, Void>() {
            private var mAsyncTaskDAO: GeraeteDAO = dao


            override fun doInBackground(vararg params: Int?): Void? {
                //checks if params[0] and params[1] is null
                params[0]?.let {
                    params[1]?.let { it1 ->
                        mAsyncTaskDAO.updateGeraetByRaumID(
                            it,
                            it1
                        )
                    }
                }
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

        class updateAsyncTaskGeraetByKategorieID(dao: GeraeteDAO) : AsyncTask<Int, Void, Void>() {
            private var mAsyncTaskDAO: GeraeteDAO = dao

            override fun doInBackground(vararg params: Int?): Void? {
                //Checks if params[0] and params[1] is null
                params[0]?.let {
                    params[1]?.let { it1 ->
                        mAsyncTaskDAO.updateGeraetByKategorieID(
                            it,
                            it1
                        )
                    }
                }
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
                try {
                    mAsyncTaskDAO.insertRaum(params[0])
                } catch (e: RuntimeException) {
                    Log.d("TAGError", e.toString())
                }
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
