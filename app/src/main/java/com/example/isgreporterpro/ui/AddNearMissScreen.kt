package com.example.isgreporterpro.ui

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

import com.example.isgreporterpro.data.NearMissEntity
import com.example.isgreporterpro.viewmodel.NearMissViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNearMissScreen(
    viewModel: NearMissViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var witness by remember { mutableStateOf("") }
    var auditTime by remember { mutableStateOf("") }
    var isReported by remember { mutableStateOf(false) }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri -> selectedImageUri = uri }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success -> if (success) { selectedImageUri = cameraImageUri } }

    fun createTempImageUri(): Uri {
        val file = java.io.File.createTempFile("JPEG_NEARMISS_", ".jpg", context.externalCacheDir)
        return androidx.core.content.FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Yeni Ramak Kala Raporu") },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Geri") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if (selectedImageUri != null) {
                AsyncImage(model = selectedImageUri, contentDescription = null, modifier = Modifier.fillMaxWidth().height(250.dp).clickable { selectedImageUri = null }, contentScale = ContentScale.Crop)
            } else {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }, modifier = Modifier.weight(1f).height(60.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null); Spacer(modifier = Modifier.width(8.dp)); Text("Galeri")
                    }
                    Button(onClick = {
                        val newUri = createTempImageUri()
                        cameraImageUri = newUri
                        cameraLauncher.launch(newUri)
                    }, modifier = Modifier.weight(1f).height(60.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null); Spacer(modifier = Modifier.width(8.dp)); Text("Kamera")
                    }
                }
            }

            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Olay Başlığı (Zorunlu)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Konum (Zorunlu)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = witness, onValueChange = { witness = it }, label = { Text("Gören / Tanık (Varsa)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = auditTime, onValueChange = { auditTime = it }, label = { Text("Olay Saati") }, modifier = Modifier.fillMaxWidth())

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Checkbox(checked = isReported, onCheckedChange = { isReported = it })
                Text("Üst Yönetime Bildirildi", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (title.isNotBlank() && location.isNotBlank()) {
                        val newNearMiss = NearMissEntity(
                            title = title, auditTime = auditTime, location = location, witness = witness, isReported = isReported, imageUri = selectedImageUri?.toString()
                        )
                        viewModel.insertNearMiss(newNearMiss)
                        onNavigateBack()
                    } else {
                        // KULLANICIYI UYARAN MESAJ
                        Toast.makeText(context, "Lütfen Başlık ve Konum alanlarını doldurun!", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
            ) {
                Text("Ramak Kala Raporunu Kaydet", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}