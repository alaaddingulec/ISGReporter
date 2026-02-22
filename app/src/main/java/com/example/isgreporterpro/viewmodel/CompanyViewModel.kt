package com.example.isgreporterpro.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.isgreporterpro.data.CompanyEntity
import com.example.isgreporterpro.data.AppDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CompanyViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).companyDao()

    val allCompanies: StateFlow<List<CompanyEntity>> = dao.getAllCompanies()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    fun insertCompany(company: CompanyEntity) {
        viewModelScope.launch {
            dao.insertCompany(company)
        }
    }
}