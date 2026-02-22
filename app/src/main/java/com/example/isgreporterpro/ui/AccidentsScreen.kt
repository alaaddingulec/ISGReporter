package com.example.isgreporterpro.ui

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

import com.example.isgreporterpro.data.AccidentEntity
import com.example.isgreporterpro.viewmodel.AccidentViewModel
import com.example.isgreporterpro.utils.PdfGenerator

// ==========================================
// 1. KAZA LİSTESİ EKRANI
// ==========================================
@Composable
fun AccidentsScreen(
    viewModel: AccidentViewModel = viewModel(),
    onAddClick: () -> Unit
) {
    val accidentsList by viewModel.allAccidents.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick, containerColor = Color(0xFFD32F2F), contentColor = Color.White) {
                Icon(Icons.Default.Add, contentDescription = "Ekle")
            }
        }
    ) { paddingValues ->
        if (accidentsList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Henüz iş kazası raporu bulunmuyor.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color(0xFFF5F5F5)),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(accidentsList) { accident ->
                    AccidentCard(accident)
                }
            }
        }
    }
}

// 2. KAZA KARTI TASARIMI
@Composable
fun AccidentCard(accident: AccidentEntity) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)).background(Color.LightGray), contentAlignment = Alignment.Center) {
                if (accident.imageUri != null) {
                    AsyncImage(model = Uri.parse(accident.imageUri), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                } else {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Red)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = accident.reportTitle, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "Tarih: ${accident.dateAndTime}", fontSize = 12.sp, color = Color.Gray)
                Text(text = "Yaralı: ${accident.injuredStaffName} (${accident.staffStatus})", fontSize = 12.sp, color = Color.DarkGray)
                Text(text = "Tip: ${accident.accidentType}", fontSize = 12.sp, color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold)
            }
            IconButton(onClick = { PdfGenerator.generateAccidentPdf(context, accident) }) {
                Icon(Icons.Default.PictureAsPdf, contentDescription = "PDF İndir", tint = Color(0xFFD32F2F))
            }
        }
    }
}

// ==========================================
// 3. KAZA EKLEME FORMU EKRANI
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccidentScreen(
    viewModel: AccidentViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var dateTime by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var injuredName by remember { mutableStateOf("") }

    var staffStatus by remember { mutableStateOf("Çalışan") }
    var staffStatusExpanded by remember { mutableStateOf(false) }
    val staffOptions = listOf("Çalışan", "Alt İşveren", "Ziyaretçi", "Stajyer")

    var accidentType by remember { mutableStateOf("İlk Yardım") }
    var accidentTypeExpanded by remember { mutableStateOf(false) }
    val typeOptions = listOf("İlk Yardım", "Tıbbi Müdahale", "Kayıp Zamanlı Kaza", "Uzuv Kayıplı", "Ölümlü Kaza")

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri -> selectedImageUri = uri }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success -> if (success) selectedImageUri = cameraImageUri }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Yeni İş Kazası Raporu") },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Geri") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.errorContainer, titleContentColor = MaterialTheme.colorScheme.error)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            if (selectedImageUri != null) {
                AsyncImage(model = selectedImageUri, contentDescription = null, modifier = Modifier.fillMaxWidth().height(200.dp).clickable { selectedImageUri = null }, contentScale = ContentScale.Crop)
            } else {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }, modifier = Modifier.weight(1f).height(55.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)) { Text("Galeri") }
                    Button(
                        onClick = {
                            val file = java.io.File.createTempFile("ACC_", ".jpg", context.externalCacheDir)
                            val uri = androidx.core.content.FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                            cameraImageUri = uri
                            cameraLauncher.launch(uri)
                        },
                        modifier = Modifier.weight(1f).height(55.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                    ) { Text("Kamera") }
                }
            }

            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Kaza Başlığı (Zorunlu)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = dateTime, onValueChange = { dateTime = it }, label = { Text("Tarih ve Saat") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Olay Yeri (Zorunlu)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = injuredName, onValueChange = { injuredName = it }, label = { Text("Yaralanan Personel Adı") }, modifier = Modifier.fillMaxWidth())

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = staffStatus, onValueChange = {}, readOnly = true, label = { Text("Personel Statüsü") },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) }, modifier = Modifier.fillMaxWidth()
                )
                Box(modifier = Modifier.matchParentSize().background(Color.Transparent).clickable { staffStatusExpanded = true })
                DropdownMenu(expanded = staffStatusExpanded, onDismissRequest = { staffStatusExpanded = false }) {
                    staffOptions.forEach { selection ->
                        DropdownMenuItem(text = { Text(selection) }, onClick = { staffStatus = selection; staffStatusExpanded = false })
                    }
                }
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = accidentType, onValueChange = {}, readOnly = true, label = { Text("Kaza Tipi") },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) }, modifier = Modifier.fillMaxWidth()
                )
                Box(modifier = Modifier.matchParentSize().background(Color.Transparent).clickable { accidentTypeExpanded = true })
                DropdownMenu(expanded = accidentTypeExpanded, onDismissRequest = { accidentTypeExpanded = false }) {
                    typeOptions.forEach { selection ->
                        DropdownMenuItem(text = { Text(selection) }, onClick = { accidentType = selection; accidentTypeExpanded = false })
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (title.isNotBlank() && location.isNotBlank()) {
                        val newAccident = AccidentEntity(
                            reportTitle = title, dateAndTime = dateTime, location = location,
                            injuredStaffName = injuredName, staffStatus = staffStatus,
                            accidentType = accidentType, imageUri = selectedImageUri?.toString()
                        )
                        viewModel.insertAccident(newAccident)
                        onNavigateBack()
                    } else {
                        Toast.makeText(context, "Lütfen Başlık ve Olay Yeri alanlarını doldurun!", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
            ) {
                Text("Kaza Raporunu Kaydet", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}