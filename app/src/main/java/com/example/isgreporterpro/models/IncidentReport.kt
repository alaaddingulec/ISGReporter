package com.isgreporterpro.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incident_reports")
data class IncidentReport(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val companyId: Int,
    val type: String, // Near Miss (Ramak Kala), Site Audit (Saha Gözlem), Accident (İş Kazası)
    val title: String,
    val auditTime: String,
    val location: String,
    val witness: String?,
    val isReported: Boolean = false,
    val imagePath: String?, // Çekilen fotoğrafın cihazdaki konumu
    val riskLikelihood: Int = 0, // 5x5 matris için olasılık (1-5)
    val riskSeverity: Int = 0  // 5x5 matris için şiddet (1-5)
)