import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.navigation.safe.args)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.ksp)
    id("kotlin-parcelize")
}

android {
    namespace = "com.taufik.themovieshow"
    compileSdk = 35

    // APK Name
    applicationVariants.all {
        val timestamp = SimpleDateFormat("yyyyMMddHHmm").format(Date())
        setProperty("archivesBaseName", "TheMovieShow-v${versionName}(${versionCode})-${timestamp}")
    }

    defaultConfig {
        applicationId = "com.taufik.themovieshow"
        minSdk = 30
        targetSdk = 35
        versionCode = 128
        versionName = "1.28"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs.getByName("debug")
        }
        debug {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    flavorDimensions += "version"
    productFlavors {
        create("dev") {
            dimension = "version"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"

            buildConfigField("String", "API_KEY", "\"YOUR_API_KEY_HERE\"")
            buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3/\"")
            buildConfigField("String", "IMAGE_URL", "\"https://image.tmdb.org/t/p/w780/\"")
            buildConfigField("String", "THUMBNAIL_IMAGE_URL", "\"https://img.youtube.com/vi/\"")
        }
        create("prod") {
            dimension = "version"

            buildConfigField("String", "API_KEY", "\"YOUR_API_KEY_HERE\"")
            buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3/\"")
            buildConfigField("String", "IMAGE_URL", "\"https://image.tmdb.org/t/p/w780/\"")
            buildConfigField("String", "THUMBNAIL_IMAGE_URL", "\"https://img.youtube.com/vi/\"")
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
        buildConfig = true
    }
}

dependencies {

    // Kotlin
    implementation(libs.core.ktx)

    // UI
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.legacy.support)

    // Glide
    implementation(libs.glide)

    // Navigation component
    implementation(libs.nav.fragment.ktx)
    implementation(libs.nav.ui.ktx)

    // Fragment KTX
    implementation(libs.fragment.ktx)
    implementation(libs.androidx.preference)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.espresso.contrib)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)

    // Justified text view
    implementation(libs.justifiedtextview)

    // View Model
    implementation(libs.lifecycle.extensions)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // Network
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Toasty
    implementation(libs.toasty)

    // Room DB
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    testImplementation(libs.androidx.room.testing)
    ksp(libs.androidx.room.compiler)

    // DataStore
    implementation(libs.datastore)
    implementation(libs.androidx.datastore.preferences)

    // Coroutine
    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Firebase
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.analytics.ktx)

    // Lottie
    implementation(libs.lottie)

    // FlexboxLayout
    implementation(libs.flexbox)

    // Encryption
    implementation(libs.android.database.sqlcipher)
    implementation(libs.androidx.sqlite.ktx)

    // Chucker
    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker.no.op)
}

kapt {
    correctErrorTypes = true
}