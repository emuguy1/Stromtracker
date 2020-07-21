package com.example.stromtracker.ui.geraete

import android.content.Context
import android.widget.Toast
import com.example.stromtracker.R
import kotlin.math.round

class GeraeteCompanion {
    //often used functions and values for Geraete
    companion object {
        private const val daysInYear: Double = 365.0
        private const val wattToKWatt: Double = 1000.0
        private const val roundingNumber: Int = 100 // each 0 is one decimal place
        const val maxHours: Double = 24.0

        fun validValues(context: Context?) {
            Toast.makeText(context, R.string.geraet_new_nullValue, Toast.LENGTH_SHORT)
                .show()
        }

        fun validTimes(context: Context?) {
            Toast.makeText(
                context,
                R.string.geraet_new_zeit_error,
                Toast.LENGTH_SHORT
            ).show()
        }

        fun standByError(context: Context?) {
            Toast.makeText(
                context,
                R.string.geraet_standby_error,
                Toast.LENGTH_SHORT
            ).show()
        }

        fun roundDouble(number: Double): Double {
            return round(number * roundingNumber) / roundingNumber
        }

        fun calculateKWH(verbrauch: Double): Double {
            return (verbrauch * wattToKWatt) / daysInYear
        }
    }
}