import java.util.*


plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

val properties = Properties()
val localPropertiesFile: File = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { properties.load(it) }
}

android {
    compileSdkVersion(31)
    ndkVersion = "22.1.7171670"

    defaultConfig {
        applicationId = "com.pratt.tvwebbrowser"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 53
        versionName = "1.8.2"

        javaCompileOptions {
            annotationProcessorOptions {
                argument("room.incremental", "true")
            }
        }
    }
    signingConfigs {
        create("release") {
            storeFile = rootProject.file(properties.getProperty("storeFile", "\\"))
            storePassword = properties.getProperty("storePassword", "")
            keyAlias = properties.getProperty("keyAlias", "")
            keyPassword = properties.getProperty("keyPassword", "")
        }
    }
    buildTypes {
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig=signingConfigs.getByName("release")
        }
    }

    flavorDimensions(*(flavorDimensionList + listOf("appstore")).toTypedArray())
    productFlavors {
        create("generic") {
            setDimension("appstore")
        }
        create("google") {
            setDimension("appstore")
        }
        create("amazon") {
            setDimension("appstore")
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kapt {
        arguments {
            //used when AppDatabase @Database annotation exportSchema = true. Useful for migrations
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.webkit:webkit:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("androidx.recyclerview:recyclerview:1.2.1")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.6.20")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.20")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")

    val roomVersion = "2.4.2"
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    implementation("com.github.truefedex:segmented-button:v1.0.0")
    implementation("com.github.truefedex:ad-block:v0.0.1-ci")
    implementation("de.halfbit:pinned-section-listview:1.0.0")

    "debugImplementation"("com.squareup.leakcanary:leakcanary-android:2.7")

    //appstore-dependent dependencies
    "googleImplementation"("com.google.firebase:firebase-core:20.1.2")
    "googleImplementation"("com.google.firebase:firebase-crashlytics-ktx:18.2.9")
}

tasks.getByName("check").dependsOn("lint")
