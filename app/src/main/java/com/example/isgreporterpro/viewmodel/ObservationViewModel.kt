package com.example.isgreporterpro.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.isgreporterpro.data.AppDatabase
import com.example.isgreporterpro.data.ObservationEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ObservationViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).observationDao()

    val allObservations: StateFlow<List<ObservationEntity>> = dao.getAllObservations()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    fun insertObservation(observation: ObservationEntity) {
        viewModelScope.launch {
            dao.insertObservation(observation)
        }
    }
}