package com.example.isgreporterpro.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.isgreporterpro.data.CompanyEntity
import com.example.isgreporterpro.viewmodel.CompanyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCompanyScreen(
    viewModel: CompanyViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var contactPerson by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var sector by remember { mutableStateOf("") }

    var dangerClass by remember { mutableStateOf("Az Tehlikeli") }
    var expanded by remember { mutableStateOf(false) }
    val dangerOptions = listOf("Az Tehlikeli", "Tehlikeli", "Çok Tehlikeli")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Yeni Firma Ekle") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Geri") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Firma Adı") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Açık Adres") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = contactPerson, onValueChange = { contactPerson = it }, label = { Text("İletişim Kurulacak Yetkili") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Telefon Numarası") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = sector, onValueChange = { sector = it }, label = { Text("Faaliyet Gösterdiği Sektör") }, modifier = Modifier.fillMaxWidth())

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = dangerClass,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tehlike Sınıfı") },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
                Box(modifier = Modifier.matchParentSize().background(Color.Transparent).clickable { expanded = true })
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    dangerOptions.forEach { selection ->
                        DropdownMenuItem(
                            text = { Text(selection) },
                            onClick = { dangerClass = selection; expanded = false }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val newCompany = CompanyEntity(
                            name = name,
                            address = address,
                            contactPerson = contactPerson,
                            phone = phone,
                            sector = sector,
                            dangerClass = dangerClass
                        )
                        viewModel.insertCompany(newCompany)
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Firmayı Kaydet", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}