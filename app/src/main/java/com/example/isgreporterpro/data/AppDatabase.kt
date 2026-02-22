package com.example.isgreporterpro.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        CompanyEntity::class,
        ObservationEntity::class,
        NearMissEntity::class,
        AccidentEntity::class
    ],
    version = 7, // İŞTE ÇÖKMEYİ ÖNLEYEN KRİTİK GÜNCELLEME
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun companyDao(): CompanyDao
    abstract fun observationDao(): ObservationDao
    abstract fun nearMissDao(): NearMissDao
    abstract fun accidentDao(): AccidentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "isg_reporter_database"
                )
                    .fallbackToDestructiveMigration() // Versiyon artınca eski veriyi silip çökmeden yenisini kurar
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}