plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.istjobsportal"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.istjobsportal"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    implementation (libs.material3)
    // In build.gradle (app)
    implementation (platform(libs.firebase.bom.v3120))
    implementation (libs.firebase.messaging)
    implementation(libs.androidx.material.icons.extended)
    implementation (libs.androidx.datastore.preferences)
    implementation(libs.androidx.activity.compose)
    implementation(libs.accompanist.coil)
    implementation(libs.coil.compose) // Check the latest version on the Coil GitHub repository
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.common.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.androidx.room.common)
    implementation(libs.support.annotations)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.ui.android)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.firebase.appcheck.debug)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}