package com.example.isgreporterpro.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "near_misses")
data class NearMissEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val auditTime: String,
    val location: String,
    val witness: String,
    val isReported: Boolean = false,
    val imageUri: String? = null
)