package com.example.isgreporterpro.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NearMissDao {
    @Query("SELECT * FROM near_misses ORDER BY id DESC")
    fun getAllNearMisses(): Flow<List<NearMissEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNearMiss(nearMiss: NearMissEntity)
}