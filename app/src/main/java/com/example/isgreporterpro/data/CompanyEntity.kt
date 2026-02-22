package com.example.isgreporterpro.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "companies")
data class CompanyEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val address: String,
    val contactPerson: String,
    val phone: String,
    val sector: String,
    val dangerClass: String // Tehlike Sınıfı (Az Tehlikeli, Tehlikeli, Çok Tehlikeli)
)