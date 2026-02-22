package com.isgreporterpro.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "companies")
data class Company(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val registrationNumber: String,
    val naceCode: String,
    val hazardClass: String, // Az Tehlikeli, Tehlikeli, Çok Tehlikeli
    val companyExecutive: String,
    val dateOfContract: String,
    val logoUri: String? // Firmanın logo yolu
)