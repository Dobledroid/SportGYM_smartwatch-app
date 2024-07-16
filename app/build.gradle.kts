plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.pry_gym"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pry_gym"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
        }

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.play.services.wearable)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.tooling.preview)
    implementation(libs.compose.material)
    implementation(libs.compose.foundation)
    implementation(libs.activity.compose)
    implementation(libs.core.splashscreen)
    implementation ("androidx.wear.compose:compose-navigation:1.3.1")
    implementation(libs.material3.android)

    // Añadir estas dependencias para asegurar que todas las funcionalidades de Jetpack Compose están disponibles
    implementation("androidx.compose.ui:ui:1.5.1")
    implementation ("androidx.compose.foundation:foundation:1.5.1")
    implementation("androidx.compose.material:material:1.5.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.1")
    implementation ("androidx.compose.material:material-icons-extended:1.0.5")
    implementation("androidx.activity:activity-compose:1.5.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.compose.ui:ui-text:1.5.1") // Dependencia adicional para el texto

    // Dependencia de OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    // Dependencia de Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")

    implementation ("androidx.wear.compose:compose-material:1.0.0")
    implementation ("androidx.wear.compose:compose-foundation:1.1.0")
    implementation ("androidx.navigation:navigation-compose:2.5.1")
    implementation ("androidx.wear.compose:compose-navigation:1.1.0")

    implementation ("com.google.code.gson:gson:2.8.8")

    implementation ("com.google.android.material:material:1.4.0")

    implementation ("com.google.android.gms:play-services-location:21.0.1")

    implementation ("com.google.accompanist:accompanist-flowlayout:0.21.2-beta")

    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)


    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}