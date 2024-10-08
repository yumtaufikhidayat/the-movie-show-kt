import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.navigation.safe.args)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.taufik.themovieshow"
    compileSdk = 34

    // APK Name
    applicationVariants.all {
        val timestamp = SimpleDateFormat("yyyyMMddHHmm").format(Date())
        setProperty("archivesBaseName", "TheMovieShow-v${versionName}(${versionCode})-${timestamp}")
    }

    defaultConfig {
        applicationId = "com.taufik.themovieshow"
        minSdk = 28
        targetSdk = 34
        versionCode = 128
        versionName = "1.28"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "API_KEY", "\"API_KEY\"")
        buildConfigField("String", "BASE_URL", "\"BASE_URL\"")
        buildConfigField("String", "IMAGE_URL", "\"IMAGE_URL\"")
        buildConfigField("String", "THUMBNAIL_IMAGE_URL", "\"THUMBNAIL_IMAGE_URL\"")
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // Uncomment code below to run in release variant
//            signingConfig = signingConfigs.debug
        }
        debug {
            isMinifyEnabled = true
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
}

kapt {
    correctErrorTypes = true
}