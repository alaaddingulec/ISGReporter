package com.example.isgreporterpro.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accidents")
data class AccidentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val reportTitle: String,
    val dateAndTime: String,
    val location: String,
    val injuredStaffName: String,
    val staffStatus: String,
    val accidentType: String,
    val imageUri: String? = null
)