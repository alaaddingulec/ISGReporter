package com.example.isgreporterpro.ui

import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

import com.example.isgreporterpro.data.NearMissEntity
import com.example.isgreporterpro.viewmodel.NearMissViewModel
import com.example.isgreporterpro.utils.PdfGenerator

@Composable
fun NearMissScreen(
    viewModel: NearMissViewModel = viewModel(),
    onAddClick: () -> Unit
) {
    val nearMissList by viewModel.allNearMisses.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick, containerColor = Color(0xFFE53935), contentColor = Color.White) {
                Text("Ekle", modifier = Modifier.padding(horizontal = 16.dp), fontWeight = FontWeight.Bold)
            }
        }
    ) { paddingValues ->
        if (nearMissList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Henüz Ramak Kala (Near Miss) raporu bulunmuyor.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color(0xFFF5F5F5)),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(nearMissList) { report ->
                    NearMissCard(report = report)
                }
            }
        }
    }
}

@Composable
fun NearMissCard(report: NearMissEntity) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Near Miss: ${report.title}", fontStyle = FontStyle.Italic, fontSize = 18.sp, color = Color.DarkGray)
                IconButton(onClick = { PdfGenerator.generateNearMissPdf(context, report) }) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = "PDF İndir", tint = Color(0xFFD32F2F))
                }
            }
            HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier.weight(0.4f).height(100.dp).clip(RoundedCornerShape(8.dp)).background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    if (report.imageUri != null) {
                        AsyncImage(model = Uri.parse(report.imageUri), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    } else {
                        Icon(Icons.Default.Image, contentDescription = null, tint = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(0.6f)) {
                    DetailRow(icon = Icons.Default.AccessTime, text = "Saat: ${report.auditTime}", tint = Color(0xFFD84315))
                    DetailRow(
                        icon = if(report.isReported) Icons.Default.CheckCircle else Icons.Default.RemoveCircleOutline,
                        text = if(report.isReported) "Bildirildi" else "Bildirilmedi",
                        tint = if(report.isReported) Color(0xFF4CAF50) else Color(0xFFE53935)
                    )
                    DetailRow(icon = Icons.Default.LocationOn, text = "Yer: ${report.location}", tint = Color(0xFF8E24AA))
                    DetailRow(icon = Icons.Default.Visibility, text = "Tanık: ${report.witness}", tint = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun DetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, tint: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, fontSize = 12.sp, color = Color.DarkGray)
    }
}