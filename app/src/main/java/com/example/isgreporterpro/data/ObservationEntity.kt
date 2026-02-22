package com.example.isgreporterpro.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "observations")
data class ObservationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val location: String,
    val detail: String,
    val dateAndTime: String,
    val riskLevel: String,
    val imageUri: String? = null
)