plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt") // Room veritabanı kodlarını derlemek için eklendi
}

android {
    namespace = "com.example.isgreporterpro"
    compileSdk = 36// Standart ve kararlı sürüme çekildi

    defaultConfig {
        applicationId = "com.example.sgreporterpro"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // 1. Android ve Compose Temel Kütüphaneleri (Dışarıda kalanlar içeri alındı)
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // 2. Navigation Component (Compose ile uyumlu sayfa geçişleri için)
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation(libs.androidx.compose.ui.text)

    // 3. Room Database (Çevrimdışı veritabanı)
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:${room_version}")

    // 4. Resim Yükleme (Compose için standart olan Coil kullanıldı)
    implementation("io.coil-kt:coil-compose:2.6.0")

    // 5. Uygulama İçi Satın Alma
    implementation("com.android.billingclient:billing:6.2.0")

    // 6. Profesyonel PDF Oluşturma (iText)
    implementation("com.itextpdf:itext7-core:7.2.5")

    // Test Kütüphaneleri
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}