package com.example.isgreporterpro.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ObservationDao {

    // Gözlem raporlarını en son eklenen en üstte olacak şekilde (DESC) listeler
    @Query("SELECT * FROM observations ORDER BY id DESC")
    fun getAllObservations(): Flow<List<ObservationEntity>>

    // Yeni saha gözlem raporu kaydeder
    @Insert
    suspend fun insertObservation(observation: ObservationEntity)
}