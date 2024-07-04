plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
}

android {
    namespace = "com.spirala1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.spirala1"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        buildConfig = true
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug{
            buildConfigField ("String", "TREFLE_API_KEY", project.properties["TREFLE_API_KEY"].toString())

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core)
    implementation(libs.androidx.rules)
    implementation(libs.androidx.espresso.contrib)
    implementation(libs.androidx.espresso.intents)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("androidx.camera:camera-core:1.2.2")
    implementation("androidx.camera:camera-camera2:1.2.2")
    implementation("androidx.camera:camera-lifecycle:1.2.2")
    implementation("androidx.camera:camera-video:1.2.2")
    implementation("androidx.camera:camera-view:1.2.2")
    implementation("androidx.camera:camera-extensions:1.2.2")
    implementation("com.google.guava:guava:30.1-android")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation("com.github.bumptech.glide:glide:5.0.0-rc01")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // Kotlin
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

// Feature module Support
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.7.7")

    implementation("androidx.navigation:navigation-compose:2.7.7")
    testImplementation ("org.assertj:assertj-core:3.22.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
    testImplementation ("org.mockito:mockito-core:4.3.1")
    testImplementation ("org.mockito:mockito-inline:4.3.1")
    androidTestImplementation ("org.mockito:mockito-android:3.11.2")

    implementation("androidx.room:room-runtime:+")
    annotationProcessor("androidx.room:room-compiler:+")
    implementation("androidx.room:room-ktx:+")
    kapt("androidx.room:room-compiler:+")

}