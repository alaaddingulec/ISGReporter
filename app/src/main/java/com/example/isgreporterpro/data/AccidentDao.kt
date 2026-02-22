package com.example.isgreporterpro.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AccidentDao {
    @Query("SELECT * FROM accidents ORDER BY id DESC")
    fun getAllAccidents(): Flow<List<AccidentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccident(accident: AccidentEntity)
}