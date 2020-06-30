package com.example.stromtracker.database


import androidx.room.*;

@Entity (indices = [ Index("kategorieID"), Index("haushaltID"), Index("raumID")  ], foreignKeys = [
    ForeignKey(
        entity = Kategorie::class,
        parentColumns = arrayOf("kategorieID"),
        childColumns = arrayOf("kategorieID"),
        onDelete = ForeignKey.NO_ACTION),
    ForeignKey(
        entity = Haushalt::class,
    parentColumns = arrayOf("haushaltID"),
    childColumns = arrayOf("haushaltID"),
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = Raum::class,
        parentColumns = arrayOf("raumID"),
        childColumns = arrayOf("raumID"),
        onDelete = ForeignKey.NO_ACTION
)
])

    data class Geraete(
        @ColumnInfo(name = "name") private var name: String,
        @ColumnInfo(name = "kategorieID") private var kategorieID: Int,
        @ColumnInfo(name = "raumID") private var raumID: Int,
        @ColumnInfo(name = "haushaltID") private var haushaltID: Int,
        @ColumnInfo(name = "stromVollast") private var stromVollast: Double,

        @ColumnInfo(name = "stromStandBy") private var stromStandBy: Double,
        @ColumnInfo(name = "betriebszeit") private var betriebszeit: Double,
        @ColumnInfo(name = "urlaubsmodus") private var urlaubsmodus: Boolean,
        @ColumnInfo(name = "notiz") private var notiz: String?
)



    {
        @PrimaryKey(autoGenerate = true) private var geraeteID: Int = 0

        fun getGeraeteID():Int {
            return geraeteID
        }

        fun setGeraeteID(v:Int) {
            geraeteID = v
        }

        fun getName():String {
            return name
        }

        fun setName(v:String) {
            name = v
        }

        fun getStromVollast():Double {
            return stromVollast
        }

        fun setStromVollast(v:Double) {
            stromVollast = v
        }



        fun getKategorieID():Int {
            return kategorieID
        }

        fun setKategorieID(k:Int) {
            kategorieID = k
        }

        fun getRaumID():Int {
            return raumID
        }

        fun setRaumID(r:Int) {
            raumID = r
        }

        fun getHaushaltID():Int {
            return haushaltID
        }

        fun setHaushaltID(r:Int) {
            haushaltID = r
        }

        fun getStromStandBy():Double {
            return stromStandBy
        }

        fun setStromStandBy(r:Double) {
            stromStandBy = r
        }

        fun getBetriebszeit():Double {
            return betriebszeit
        }

        fun setBetriebszeit(r:Double) {
            betriebszeit = r
        }

        fun getUrlaubsmodus():Boolean {
            return urlaubsmodus
        }

        fun setUrlaubsmodus(b:Boolean) {
            urlaubsmodus = b
        }

        fun getNotiz():String? {
            return notiz
        }

        fun setNotiz(s:String) {
            notiz = s
        }








    }




