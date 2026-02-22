package com.example.isgreporterpro.ui

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Save
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
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateNext: () -> Unit // Doğrudan geçiş yetkisi
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)
    val coroutineScope = rememberCoroutineScope() // İşlemi arka plana atıp donmayı engeller

    var name by remember { mutableStateOf(sharedPreferences.getString("name", "") ?: "") }
    var title by remember { mutableStateOf(sharedPreferences.getString("title", "") ?: "") }
    var email by remember { mutableStateOf(sharedPreferences.getString("email", "") ?: "") }
    var imageUriString by remember { mutableStateOf(sharedPreferences.getString("profile_image", null)) }

    var selectedNewUri by remember { mutableStateOf<Uri?>(null) }
    var isSaving by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedNewUri = uri
            imageUriString = uri.toString()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFFE3F2FD))
                .clickable {
                    if (!isSaving) galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
            contentAlignment = Alignment.Center
        ) {
            if (imageUriString != null) {
                AsyncImage(
                    model = Uri.parse(imageUriString),
                    contentDescription = "Profil Resmi",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(Icons.Default.AccountCircle, contentDescription = "Profil Ekle", modifier = Modifier.size(100.dp), tint = Color(0xFF1976D2))
            }
        }
        Text("Resim eklemek için dokunun", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))

        Spacer(modifier = Modifier.height(16.dp))
        Text("Uzman Profil Bilgileri", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Ad Soyad") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("İSG Sınıfı / Ünvan") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("E-Posta Adresi") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                isSaving = true
                coroutineScope.launch {
                    try {
                        // Resmi güvenli hafızaya kopyalama işlemi (Arka Planda)
                        val finalImageUri = if (selectedNewUri != null) {
                            withContext(Dispatchers.IO) {
                                val inputStream = context.contentResolver.openInputStream(selectedNewUri!!)
                                // Eski resmi üst üste yazmasın diye ismine zaman damgası koyuyoruz
                                val file = File(context.filesDir, "profile_image_${System.currentTimeMillis()}.jpg")
                                val outputStream = FileOutputStream(file)
                                inputStream?.copyTo(outputStream)
                                inputStream?.close()
                                outputStream.close()
                                Uri.fromFile(file).toString()
                            }
                        } else {
                            imageUriString
                        }

                        // Verileri kaydet
                        sharedPreferences.edit()
                            .putString("name", name)
                            .putString("title", title)
                            .putString("email", email)
                            .putString("profile_image", finalImageUri)
                            .apply()

                        isSaving = false
                        onNavigateNext() // ANINDA DİĞER EKRANA GEÇ
                    } catch (e: Exception) {
                        e.printStackTrace()
                        isSaving = false
                        Toast.makeText(context, "Kayıt Hatası: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
            enabled = !isSaving
        ) {
            if (isSaving) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Kaydediliyor...", fontSize = 18.sp)
            } else {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Profili Kaydet ve Başla", fontSize = 18.sp)
            }
        }
    }
}