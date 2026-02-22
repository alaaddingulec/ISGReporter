package com.example.isgreporterpro.ui

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

import com.example.isgreporterpro.data.ObservationEntity
import com.example.isgreporterpro.viewmodel.ObservationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddObservationScreen(
    viewModel: ObservationViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var detail by remember { mutableStateOf("") }
    var dateAndTime by remember { mutableStateOf("") }

    var riskLevel by remember { mutableStateOf("Düşük Risk") }
    var expanded by remember { mutableStateOf(false) }
    val riskOptions = listOf("Düşük Risk", "Orta Risk", "Yüksek Risk", "Kritik Risk")

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? -> selectedImageUri = uri }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success -> if (success) { selectedImageUri = cameraImageUri } }

    fun createTempImageUri(): Uri {
        val file = java.io.File.createTempFile("OBS_", ".jpg", context.externalCacheDir)
        return androidx.core.content.FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Yeni Saha Gözlemi") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if (selectedImageUri != null) {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "Gözlem Fotoğrafı",
                    modifier = Modifier.fillMaxWidth().height(250.dp).clip(RoundedCornerShape(12.dp)).clickable { selectedImageUri = null },
                    contentScale = ContentScale.Crop
                )
            } else {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }, modifier = Modifier.weight(1f).height(60.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF546E7A))) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = "Galeri")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Galeri")
                    }
                    Button(onClick = {
                        val newUri = createTempImageUri()
                        cameraImageUri = newUri
                        cameraLauncher.launch(newUri)
                    }, modifier = Modifier.weight(1f).height(60.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))) {
                        Icon(Icons.Default.CameraAlt, contentDescription = "Kamera")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Kamera")
                    }
                }
            }

            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Gözlem Başlığı (Zorunlu)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Konum (Zorunlu)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = dateAndTime, onValueChange = { dateAndTime = it }, label = { Text("Tarih ve Saat") }, modifier = Modifier.fillMaxWidth())

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = riskLevel, onValueChange = {}, readOnly = true, label = { Text("Risk Seviyesi") },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) }, modifier = Modifier.fillMaxWidth()
                )
                Box(modifier = Modifier.matchParentSize().background(Color.Transparent).clickable { expanded = true })
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    riskOptions.forEach { selection ->
                        DropdownMenuItem(text = { Text(selection) }, onClick = { riskLevel = selection; expanded = false })
                    }
                }
            }

            OutlinedTextField(value = detail, onValueChange = { detail = it }, label = { Text("Gözlem Detayı") }, modifier = Modifier.fillMaxWidth().height(120.dp), maxLines = 4)

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (title.isNotBlank() && location.isNotBlank()) {
                        val newObservation = ObservationEntity(
                            title = title, location = location, detail = detail, dateAndTime = dateAndTime, riskLevel = riskLevel, imageUri = selectedImageUri?.toString()
                        )
                        viewModel.insertObservation(newObservation)
                        onNavigateBack()
                    } else {
                        // KULLANICIYI UYARAN MESAJ
                        Toast.makeText(context, "Lütfen Başlık ve Konum alanlarını doldurun!", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(55.dp)
            ) {
                Text("Gözlem Raporunu Kaydet", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}