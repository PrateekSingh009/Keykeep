plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //    id("kotlin-android-extensions")
//    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.credential"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.credential"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.security:security-crypto:1.1.0-alpha06") // For EncryptedSharedPreferences
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    //    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
    implementation("androidx.core:core-ktx:1.3.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    //Navigation, lifecycle
    implementation("androidx.navigation:navigation-fragment:2.3.0")
    implementation("androidx.navigation:navigation-ui:2.3.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")

    // Firebase
    implementation("com.google.firebase:firebase-database:19.3.1")
    implementation("com.google.firebase:firebase-auth:19.3.2")
    implementation("com.google.firebase:firebase-storage:19.1.1")

//    // Picasso
//    implementation("com.squareup.picasso:picasso:2.71828")
//    implementation("jp.wasabeef:picasso-transformations:2.2.1")

    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation( "androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.navigation:navigation-compose:2.7.7")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.firebaseui:firebase-ui-auth:7.2.0")

    //Lifecycle
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    //room
    implementation ("androidx.room:room-ktx:2.6.0")
    implementation ("androidx.room:room-common:2.6.0")
    implementation ("androidx.room:room-runtime:2.6.0")
    kapt("androidx.room:room-compiler:2.6.0")

    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.google.code.gson:gson:2.8.7")

    //paging
    implementation( "androidx.paging:paging-common-ktx:3.2.1")
    implementation ("androidx.paging:paging-runtime-ktx:3.2.1")

    //dagger-hilt
    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-android-compiler:2.48.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    kapt("com.google.dagger:dagger-android-processor:2.48.1")
    kapt("androidx.hilt:hilt-compiler:1.2.0")
}
