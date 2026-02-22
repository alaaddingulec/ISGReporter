package com.example.isgreporterpro.ui

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

import com.example.isgreporterpro.data.ObservationEntity
import com.example.isgreporterpro.viewmodel.ObservationViewModel
import com.example.isgreporterpro.utils.PdfGenerator

@Composable
fun ObservationsScreen(
    viewModel: ObservationViewModel = viewModel(),
    onAddClick: () -> Unit
) {
    val observationsList by viewModel.allObservations.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = Color(0xFFD32F2F),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(64.dp)
            ) {
                Text("Ekle", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    ) { paddingValues ->
        if (observationsList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color(0xFFF0F0F0)), contentAlignment = Alignment.Center) {
                Text("Henüz saha gözlem raporu bulunmuyor.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color(0xFFF0F0F0)),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(observationsList) { observation ->
                    ObservationDetailedCard(observation)
                }
            }
        }
    }
}

@Composable
fun ObservationDetailedCard(observation: ObservationEntity) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth().border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Rapor : ${observation.title}",
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Italic,
                fontSize = 18.sp,
                color = Color.DarkGray
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp, color = Color(0xFFE0E0E0))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier.width(130.dp).height(90.dp).clip(RoundedCornerShape(4.dp)).background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    if (observation.imageUri != null) {
                        AsyncImage(
                            model = Uri.parse(observation.imageUri),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.LightGray)
                    }
                    Icon(
                        Icons.Default.RemoveCircle,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.align(Alignment.BottomStart).padding(4.dp).size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    ObservationRowItem(Icons.Default.AccessTime, "Gözlem Saati: ${observation.dateAndTime.ifBlank { "Belirtilmedi" }}", Color(0xFFE64A19))
                    ObservationRowItem(Icons.Default.Warning, "Risk Seviyesi: ${observation.riskLevel}", Color(0xFFD32F2F))
                    ObservationRowItem(Icons.Default.LocationOn, "Konum: ${observation.location}", Color(0xFF8E24AA))
                    ObservationRowItem(Icons.Default.Person, "Firma: Belirtilmedi", Color.Gray)
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 1.dp, color = Color(0xFFE0E0E0))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    IconButton(
                        onClick = { PdfGenerator.generateObservationPdf(context, observation) },
                        modifier = Modifier.size(36.dp).border(1.dp, Color.LightGray, CircleShape)
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = "PDF", tint = Color(0xFFD32F2F), modifier = Modifier.size(20.dp))
                    }
                    IconButton(onClick = { }, modifier = Modifier.size(36.dp).border(1.dp, Color.LightGray, CircleShape)) {
                        Icon(Icons.Default.Share, contentDescription = "Paylaş", tint = Color.Gray, modifier = Modifier.size(20.dp))
                    }
                }

                Text(text = "Kayıt: ${observation.dateAndTime.take(10).ifBlank { "Belirtilmedi" }}", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun ObservationRowItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, tint: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 4.dp)) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = text, fontSize = 13.sp, color = Color.DarkGray, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}