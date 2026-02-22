package com.example.isgreporterpro.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Mevzuat veri yapısı
data class MevzuatItem(val title: String, val url: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentsScreen() {
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("MevzuatPrefs", Context.MODE_PRIVATE)

    var searchQuery by remember { mutableStateOf("") }
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Mevzuat (Arama)", "Kendi Dökümanlarım")

    // Ekleme Paneli İçin Değişkenler
    var showAddDialog by remember { mutableStateOf(false) }
    var newMevzuatTitle by remember { mutableStateOf("") }
    var newMevzuatUrl by remember { mutableStateOf("") }

    // Varsayılan İSG Yönetmelikleri ve Resmi Linkleri
    val defaultMevzuat = listOf(
        MevzuatItem("6331 Sayılı İSG Kanunu", "https://www.mevzuat.gov.tr/mevzuat?MevzuatNo=6331&MevzuatTur=1&MevzuatTertip=5"),
        MevzuatItem("İş Ekipmanları Kullanımı Yönetmeliği", "https://www.mevzuat.gov.tr/mevzuat?MevzuatNo=17333&MevzuatTur=7&MevzuatTertip=5"),
        MevzuatItem("Yapı İşlerinde İSG Yönetmeliği", "https://www.mevzuat.gov.tr/mevzuat?MevzuatNo=18928&MevzuatTur=7&MevzuatTertip=5"),
        MevzuatItem("Yangın Korunma Yönetmeliği", "https://www.mevzuat.gov.tr/MevzuatMetin/3.5.200712937.pdf"),
        MevzuatItem("Kişisel Koruyucu Donanım Yönetmeliği", "https://www.mevzuat.gov.tr/mevzuat?MevzuatNo=25227&MevzuatTur=7&MevzuatTertip=5"),
        MevzuatItem("Risk Değerlendirmesi Yönetmeliği", "https://www.mevzuat.gov.tr/mevzuat?MevzuatNo=16921&MevzuatTur=7&MevzuatTertip=5")
    )

    // Hafızaya eklenen özel mevzuatları çekme fonksiyonu
    fun getCustomMevzuat(): List<MevzuatItem> {
        val savedSet = sharedPrefs.getStringSet("custom_mevzuat", emptySet()) ?: emptySet()
        return savedSet.mapNotNull {
            val parts = it.split("|||")
            if (parts.size == 2) MevzuatItem(parts[0], parts[1]) else null
        }
    }

    var customMevzuatList by remember { mutableStateOf(getCustomMevzuat()) }
    val allMevzuat = defaultMevzuat + customMevzuatList

    // Arama Çubuğu Filtresi (Artık çalışıyor)
    val filteredMevzuat = allMevzuat.filter { it.title.contains(searchQuery, ignoreCase = true) }

    // Kendi Dökümanlarım
    var myDocuments by remember { mutableStateOf(listOf<Uri>()) }
    val documentLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) { myDocuments = myDocuments + uri }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {

        TabRow(selectedTabIndex = selectedTabIndex, containerColor = Color.White) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp) }
                )
            }
        }

        if (selectedTabIndex == 0) {
            // ================= 1. SEKME: MEVZUAT =================
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Mevzuat Adı Ara...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Ara") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(filteredMevzuat) { mevzuat ->
                            Card(
                                modifier = Modifier.fillMaxWidth().clickable {
                                    // Tıklayınca İnternet Tarayıcısında Açar!
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mevzuat.url))
                                        context.startActivity(intent)
                                    } catch (e: Exception) { }
                                },
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Gavel, contentDescription = null, tint = Color(0xFFD32F2F))
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        Text(text = mevzuat.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                                        Text(text = "Okumak için dokunun", fontSize = 11.sp, color = Color.Gray)
                                    }
                                }
                            }
                        }
                        item { Spacer(modifier = Modifier.height(70.dp)) } // Butonun arkasında kalmaması için boşluk
                    }
                }

                // Kendi Mevzuatını Ekle Butonu
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                    containerColor = Color(0xFFD32F2F),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Ekle")
                }
            }
        } else {
            // ================= 2. SEKME: KENDİ DÖKÜMANLARIM =================
            Box(modifier = Modifier.fillMaxSize()) {
                if (myDocuments.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Henüz bir döküman eklemediniz.\nSağ alt köşeden PDF ekleyebilirsiniz.", color = Color.Gray, textAlign = TextAlign.Center)
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(myDocuments) { uri ->
                            Card(
                                modifier = Modifier.fillMaxWidth().clickable {
                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        setDataAndType(uri, "application/pdf")
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    }
                                    try { context.startActivity(intent) } catch (e: Exception) { }
                                },
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = Color(0xFF1976D2))
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(text = "Belge: ${uri.lastPathSegment ?: "Bilinmeyen Dosya"}", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }
                }

                FloatingActionButton(
                    onClick = { documentLauncher.launch(arrayOf("application/pdf")) },
                    modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                    containerColor = Color(0xFF1976D2),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Döküman Ekle")
                }
            }
        }
    }

    // YENİ MEVZUAT EKLEME PENCERESİ (Diyalog)
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Yeni Mevzuat Ekle") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = newMevzuatTitle, onValueChange = { newMevzuatTitle = it }, label = { Text("Mevzuat Adı") })
                    OutlinedTextField(value = newMevzuatUrl, onValueChange = { newMevzuatUrl = it }, label = { Text("Web Linki (URL)") })
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newMevzuatTitle.isNotBlank() && newMevzuatUrl.isNotBlank()) {
                            // Linki string formatına çevirip kaydet
                            val newEntry = "$newMevzuatTitle|||$newMevzuatUrl"
                            val currentSet = sharedPrefs.getStringSet("custom_mevzuat", emptySet()) ?: emptySet()
                            val updatedSet = currentSet.toMutableSet().apply { add(newEntry) }

                            sharedPrefs.edit().putStringSet("custom_mevzuat", updatedSet).apply()

                            // Listeyi anında güncelle
                            customMevzuatList = getCustomMevzuat()

                            showAddDialog = false
                            newMevzuatTitle = ""
                            newMevzuatUrl = ""
                        }
                    }
                ) { Text("Ekle") }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("İptal") }
            }
        )
    }
}