plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")

    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "net.ticherhaz.karangancemerlangspm"
    compileSdk = 36
    defaultConfig {
        applicationId = "net.ticherhaz.karangancemerlangspm"
        minSdk = 26
        targetSdk = 36

        versionCode = 520
        versionName = "5.20"

        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            keyAlias = "keyAlias"
            keyPassword = "112233"
            storeFile = file("../karangancemerlangspm.jks")
            storePassword = "112233"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            resValue("bool", "ENABLE_DEBUG_TOOLS", "false")
        }
        debug {
            isMinifyEnabled = false
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            resValue("bool", "ENABLE_DEBUG_TOOLS", "true")
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlin {
        jvmToolchain(21)
    }
}

dependencies {
    //Android
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("androidx.percentlayout:percentlayout:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.work:work-runtime:2.11.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.activity:activity-ktx:1.12.0")
    implementation("androidx.activity:activity:1.12.0")
    //------------------------------------------------------
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")

    //Kotlin
    val kotlinVersion = "2.2.21"
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.9.6")

    //Kotlin Coroutines

    //Lifecycle
    val lifecycleVersion = "2.10.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    //MultiDex
    implementation("androidx.multidex:multidex:2.0.1")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:34.6.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-functions")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-messaging")

    implementation("com.google.android.gms:play-services-ads:24.8.0")

    //FirebaseUI
    implementation("com.firebaseui:firebase-ui-database:9.1.1")

    //Image Glide
    implementation("com.github.bumptech.glide:glide:5.0.5")
    annotationProcessor("com.github.bumptech.glide:compiler:5.0.5")

    //For time
    implementation("com.github.ticherhaz:tarikhmasa:2.0.0")
    //Image Crop
    implementation("com.soundcloud.android:android-crop:1.0.1@aar")
    //App Billing
    implementation("com.android.billingclient:billing:8.1.0")
    implementation("com.google.guava:guava:33.5.0-android")

    // For permissions
    implementation("com.github.getActivity:XXPermissions:18.5")


    // -------------------------------------------------------------------------------------
    implementation("com.google.dagger:hilt-android:2.57.2")
    kapt("com.google.dagger:hilt-compiler:2.57.2")

    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("androidx.security:security-crypto:1.1.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.3.2")

    //For Logging
    implementation("com.github.ticherhaz:FireLog:1.2.1")

    // Huawei
    implementation("com.huawei.agconnect:agconnect-core:1.9.3.301")
    implementation("com.huawei.agconnect:agconnect-auth:1.9.3.300")
    //noinspection Aligned16KB
    implementation("com.huawei.agconnect:agconnect-cloud-database:1.9.3.300")
}
apply(plugin = "com.huawei.agconnect")
