package com.example.isgreporterpro.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
// İŞTE EKSİK OLAN HAYATİ KOD:
import com.example.isgreporterpro.data.NearMissEntity
import com.example.isgreporterpro.data.AppDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NearMissViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).nearMissDao()

    val allNearMisses: StateFlow<List<NearMissEntity>> = dao.getAllNearMisses()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    fun insertNearMiss(nearMiss: NearMissEntity) {
        viewModelScope.launch {
            dao.insertNearMiss(nearMiss)
        }
    }
}