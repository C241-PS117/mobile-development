plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.example.essy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.essy"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //lotties
    val lottieVersion = "5.2.0"
    implementation ("com.airbnb.android:lottie-compose:$lottieVersion")

    implementation ("androidx.core:core-ktx:1.8.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    implementation ("androidx.activity:activity-compose:1.4.0")
    implementation ("androidx.compose.ui:ui:1.1.0")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.1.0")
    implementation ("androidx.compose.material:material:1.1.0")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:1.1.0")
    debugImplementation ("androidx.compose.ui:ui-tooling:1.1.0")
    debugImplementation ("androidx.compose.ui:ui-test-manifest:1.1.0")

    // Accompanist Pager for page view
    implementation ("com.google.accompanist:accompanist-pager:0.24.0-alpha")

}