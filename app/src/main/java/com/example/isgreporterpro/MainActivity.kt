package com.example.isgreporterpro

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import com.example.isgreporterpro.ui.theme.İSGReporterProTheme
import com.example.isgreporterpro.ui.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            İSGReporterProTheme {
                MainAppScreen()
            }
        }
    }
}

// YENİ: AÇILIŞ (SPLASH) EKRANI
@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    LaunchedEffect(key1 = true) {
        delay(2000) // 2 saniye ekranda kalır
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF616161), Color(0xFF9E9E9E)))),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Welcome to", fontSize = 24.sp, color = Color.White, modifier = Modifier.padding(bottom = 16.dp))

            Box(
                modifier = Modifier
                    .size(150.dp)
                    .background(Color.White, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Burada logonuz olacak, şimdilik ikon koyuyoruz
                Icon(Icons.Default.Engineering, contentDescription = "Logo", modifier = Modifier.size(80.dp), tint = Color(0xFFD32F2F))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("HSE Reporter Pro", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("Professional solutions...", fontSize = 16.sp, color = Color.LightGray, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)

    var menuName by remember { mutableStateOf(prefs.getString("name", "İsimsiz Uzman") ?: "İsimsiz Uzman") }
    var menuTitle by remember { mutableStateOf(prefs.getString("title", "İSG Uzmanı") ?: "İSG Uzmanı") }
    var menuEmail by remember { mutableStateOf(prefs.getString("email", "uzman@ornek.com") ?: "uzman@ornek.com") }
    var menuImageUri by remember { mutableStateOf(prefs.getString("profile_image", null)) }

    LaunchedEffect(navController.currentBackStackEntryAsState().value) {
        menuName = prefs.getString("name", "İsimsiz Uzman") ?: "İsimsiz Uzman"
        menuTitle = prefs.getString("title", "İSG Uzmanı") ?: "İSG Uzmanı"
        menuEmail = prefs.getString("email", "uzman@ornek.com") ?: "uzman@ornek.com"
        menuImageUri = prefs.getString("profile_image", null)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(300.dp), drawerContainerColor = Color.White) {
                Column(modifier = Modifier.fillMaxWidth().padding(top = 32.dp, bottom = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier.size(100.dp).clip(CircleShape).background(Color(0xFFE3F2FD)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (menuImageUri != null) {
                            AsyncImage(model = Uri.parse(menuImageUri), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                        } else {
                            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(60.dp), tint = Color(0xFF1976D2))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = menuName, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                    Text(text = menuTitle, fontSize = 14.sp, color = Color.Gray)
                    Text(text = menuEmail, fontSize = 12.sp, color = Color.Gray)
                }

                HorizontalDivider(color = Color.LightGray)

                Spacer(modifier = Modifier.height(8.dp))
                NavigationDrawerItem(label = { Text("Profilim") }, selected = false, icon = { Icon(Icons.Default.AccountBox, null, tint = Color.DarkGray) }, onClick = { scope.launch { drawerState.close() }; navController.navigate("profile") })
                NavigationDrawerItem(label = { Text("Saha Gözlem Raporları") }, selected = false, icon = { Icon(Icons.Default.Assignment, null, tint = Color.DarkGray) }, onClick = { scope.launch { drawerState.close() }; navController.navigate("observations") })
                NavigationDrawerItem(label = { Text("Ramak Kala Raporları") }, selected = false, icon = { Icon(Icons.Default.ReportProblem, null, tint = Color.DarkGray) }, onClick = { scope.launch { drawerState.close() }; navController.navigate("near_miss") })
                NavigationDrawerItem(label = { Text("Kaza Raporları") }, selected = false, icon = { Icon(Icons.Default.Warning, null, tint = Color.DarkGray) }, onClick = { scope.launch { drawerState.close() }; navController.navigate("accidents") })
                NavigationDrawerItem(label = { Text("Firmalarım") }, selected = false, icon = { Icon(Icons.Default.Business, null, tint = Color.DarkGray) }, onClick = { scope.launch { drawerState.close() }; navController.navigate("companies") })
                NavigationDrawerItem(label = { Text("Belgelerim / Mevzuat") }, selected = false, icon = { Icon(Icons.Default.Folder, null, tint = Color.DarkGray) }, onClick = { scope.launch { drawerState.close() }; navController.navigate("documents") })
            }
        }
    ) {
        // İÇERİK YÖNETİMİ
        NavHost(navController = navController, startDestination = "splash") {

            // Açılış Ekranı Rotası
            composable("splash") {
                SplashScreen(onSplashFinished = {
                    navController.navigate("observations") {
                        popUpTo("splash") { inclusive = true }
                    }
                })
            }

            // Ana Uygulama Rotası (Scaffold ile sarılı)
            composable("observations") {
                MainScaffoldLayout(drawerState, scope) {
                    ObservationsScreen(onAddClick = { navController.navigate("add_observation") })
                }
            }

            composable("profile") { MainScaffoldLayout(drawerState, scope) { ProfileScreen(onNavigateNext = { navController.navigate("observations") { popUpTo("profile") { inclusive = true } } }) } }
            composable("companies") { MainScaffoldLayout(drawerState, scope) { CompaniesScreen(onAddCompanyClick = { navController.navigate("add_company") }) } }
            composable("add_company") { MainScaffoldLayout(drawerState, scope) { AddCompanyScreen(onNavigateBack = { navController.popBackStack() }) } }
            composable("add_observation") { MainScaffoldLayout(drawerState, scope) { AddObservationScreen(onNavigateBack = { navController.popBackStack() }) } }
            composable("near_miss") { MainScaffoldLayout(drawerState, scope) { NearMissScreen(onAddClick = { navController.navigate("add_near_miss") }) } }
            composable("add_near_miss") { MainScaffoldLayout(drawerState, scope) { AddNearMissScreen(onNavigateBack = { navController.popBackStack() }) } }
            composable("accidents") { MainScaffoldLayout(drawerState, scope) { AccidentsScreen(onAddClick = { navController.navigate("add_accident") }) } }
            composable("add_accident") { MainScaffoldLayout(drawerState, scope) { AddAccidentScreen(onNavigateBack = { navController.popBackStack() }) } }
            composable("documents") { MainScaffoldLayout(drawerState, scope) { DocumentsScreen() } }
        }
    }
}

// Üst Barı (TopAppBar) her ekranda tekrar tekrar yazmamak için kalıp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffoldLayout(drawerState: DrawerState, scope: kotlinx.coroutines.CoroutineScope, content: @Composable () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HSE Reporter Pro", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { scope.launch { drawerState.open() } }) { Icon(Icons.Default.Menu, null) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            content()
        }
    }
}