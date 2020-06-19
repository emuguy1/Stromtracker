package com.example.stromtracker.database


import androidx.room.*;

@Entity
data class Geraete(
    @PrimaryKey val geraeteID: Int,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?
)
