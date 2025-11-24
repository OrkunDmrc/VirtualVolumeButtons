import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

val localProps = Properties().apply {
    val lp = rootProject.file("local.properties")
    if (lp.exists()) load(lp.inputStream())
}
val admobId = localProps.getProperty("ADMOB_ID", "")
val bannerId = localProps.getProperty("BANNER_ID", "")
val appOpenId = localProps.getProperty("APP_OPEN_ID", "")


android {
    namespace = "com.example.virtualvolumebuttons"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.virtualvolumebuttons"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["ADMOB_APP_ID"] = admobId
        resValue("string", "BANNER_ID", bannerId)
        resValue("string", "APP_OPEN_ID", appOpenId)
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation("androidx.glance:glance:1.0.0-alpha05")
    implementation("androidx.glance:glance-appwidget:1.0.0-alpha05")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.datastore:datastore-preferences:1.1.0")
    implementation("com.google.android.gms:play-services-ads:23.6.0")
    implementation("androidx.lifecycle:lifecycle-process:2.9.0-alpha08")
    implementation(libs.androidx.ui.graphics)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}