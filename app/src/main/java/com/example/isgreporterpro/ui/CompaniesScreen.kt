package com.example.isgreporterpro.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.isgreporterpro.data.CompanyEntity
import com.example.isgreporterpro.viewmodel.CompanyViewModel

@Composable
fun CompaniesScreen(
    viewModel: CompanyViewModel = viewModel(),
    onAddCompanyClick: () -> Unit
) {
    val companiesList by viewModel.allCompanies.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddCompanyClick, containerColor = Color(0xFF1976D2), contentColor = Color.White) {
                Icon(Icons.Default.Add, contentDescription = "Firma Ekle")
            }
        }
    ) { paddingValues ->
        if (companiesList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Henüz kaydedilmiş bir firma bulunmuyor.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color(0xFFF5F5F5)),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(companiesList) { company ->
                    CompanyCard(company)
                }
            }
        }
    }
}

@Composable
fun CompanyCard(company: CompanyEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Business, contentDescription = null, tint = Color(0xFF1976D2), modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = company.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.DarkGray)
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            CompanyDetailRow(icon = Icons.Default.LocationOn, text = company.address, tint = Color.Gray)
            CompanyDetailRow(icon = Icons.Default.Person, text = "Yetkili: ${company.contactPerson}", tint = Color.DarkGray)
            CompanyDetailRow(icon = Icons.Default.Phone, text = company.phone, tint = Color(0xFF388E3C))
            CompanyDetailRow(icon = Icons.Default.Factory, text = "Sektör: ${company.sector}", tint = Color(0xFFF57C00))

            val dangerColor = when(company.dangerClass) {
                "Az Tehlikeli" -> Color(0xFF388E3C)
                "Tehlikeli" -> Color(0xFFF57C00)
                "Çok Tehlikeli" -> Color(0xFFD32F2F)
                else -> Color.Gray
            }
            CompanyDetailRow(icon = Icons.Default.Warning, text = "Tehlike Sınıfı: ${company.dangerClass}", tint = dangerColor)
        }
    }
}

@Composable
fun CompanyDetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, tint: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 14.sp, color = Color.DarkGray)
    }
}