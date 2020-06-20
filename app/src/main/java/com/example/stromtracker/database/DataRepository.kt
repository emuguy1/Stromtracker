package com.example.stromtracker.database

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

class DataRepository public constructor(application: Application) {

    private var database = AppDatabase.getInstance(application)
    private  var mGeraeteDao:GerateDAO = database!!.geraeteDao()
    private  var mAllGeraete:LiveData<List<Geraete>> = mGeraeteDao.getAll()

    fun getAllGeraete():LiveData<List<Geraete>> {
        return mAllGeraete
    }

    fun insert(geraet:Geraete) {
        insertAsyncTask(mGeraeteDao).execute(geraet)
    }

    companion object {
        class insertAsyncTask(dao: GerateDAO) : AsyncTask<Geraete, Void, Void>() {
            private  var mAsyncTaskDAO: GerateDAO = dao


            override fun doInBackground(vararg params: Geraete): Void? {
                mAsyncTaskDAO.insertAll(params[0])
                return null
            }



        }
    }








}