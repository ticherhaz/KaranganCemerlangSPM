// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.12.1" apply false
    id("org.jetbrains.kotlin.android") version "2.2.10" apply false
    id("com.google.dagger.hilt.android") version "2.57.1" apply false
    id("com.google.gms.google-services") version "4.4.3" apply false
}

// build.gradle.kts (project-level)
buildscript {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://developer.huawei.com/repo/") } // Huawei repo
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.12.1") // Android Gradle Plugin
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.0") // Kotlin
        classpath("com.google.gms:google-services:4.4.3") // Firebase
        classpath("com.huawei.agconnect:agcp:1.9.3.301") // ✅ Huawei AGConnect (OLD WAY)
    }
}