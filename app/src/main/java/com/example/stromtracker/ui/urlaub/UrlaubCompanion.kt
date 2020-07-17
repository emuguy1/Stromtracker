package com.example.stromtracker.ui.urlaub

import android.util.Log
import android.widget.EditText
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class UrlaubCompanion {
    companion object {
        private const val dateLength = 10
        const val dateTimeToDays = 1000 * 60 * 60 * 24
        const val dayLen = 24
        const val wattToKW = 1.0 / 1000.0
        const val yearToDay = 1.0 / 365.0
        const val centToEuro = 1.0 / 100.0

        fun checkDates(st: EditText, end: EditText): Boolean {
            if (st.text.isNotEmpty() && end.text.isNotEmpty()) {
                if (st.text.length == this.dateLength && end.text.length == dateLength) {
                    //TODO String richtig überprüfen ob er wirklich ein gültiges Datum ist, ggf. try - catch
                    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN)
                    var tempSt: Date? = null
                    var tempEnd: Date? = null
                    try {
                        tempSt = dateFormat.parse(st.text.toString())
                        tempEnd = dateFormat.parse(end.text.toString())
                    } catch (e: ParseException) {
                        Log.d(
                            "ERROR_StringIsNoDate",
                            "Der eingegebene String ist kein Datum und lässt sich somit nicht konvertieren."
                        )
                        //e.printStackTrace() //um die Fehlermeldung auf dem StackTrace auszugeben
                        return false
                    }
                    if (tempSt != null && tempEnd != null) {
                        if (tempSt.before(tempEnd) || tempSt == tempEnd)
                            return true
                    }
                }
            }
            return false
        }
    }

}
