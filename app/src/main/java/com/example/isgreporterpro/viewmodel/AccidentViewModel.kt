package com.example.isgreporterpro.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
// Düzeltilmiş doğru import:
import com.example.isgreporterpro.data.AccidentEntity
import com.example.isgreporterpro.data.AppDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AccidentViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).accidentDao()

    val allAccidents: StateFlow<List<AccidentEntity>> = dao.getAllAccidents()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    fun insertAccident(accident: AccidentEntity) {
        viewModelScope.launch {
            dao.insertAccident(accident)
        }
    }
}