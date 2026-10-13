import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.navigation.safe.args)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.compose)
}

val enableChuckerDev = project.findProperty("enableChuckerDev") == "true"
val enableChuckerProd = project.findProperty("enableChuckerProd") == "true"

fun getStringProp(key: String, fallback: String? = null, message: String = ""): String =
    project.findProperty(key)?.toString() ?: fallback ?: throw GradleException("Missing property: $key. $message")

android {
    namespace = "com.taufik.themovieshow"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.taufik.themovieshow"
        minSdk = 30
        targetSdk = 37
        versionCode = 1320
        versionName = "1.32.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    signingConfigs {
        create("release") {
            storeFile = file("${rootDir}/${project.property("RELEASE_STORE_FILE")}")
            storePassword = project.property("RELEASE_STORE_PASSWORD") as String
            keyAlias = project.property("RELEASE_KEY_ALIAS") as String
            keyPassword = project.property("RELEASE_KEY_PASSWORD") as String
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs.getByName("debug")
        }
    }

    flavorDimensions += "version"
    productFlavors {
        create("dev") {
            dimension = "version"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"

            buildConfigField("String", "BASE_URL", "\"${getStringProp("BASE_URL", "https://api.themoviedb.org/3/", "ERROR: BASE_URL is missing")}\"")
            buildConfigField("String", "API_KEY", "\"${getStringProp("API_KEY", "YOUR_API_KEY", "ERROR: API_KEY is missing")}\"")
            buildConfigField("String", "IMAGE_URL", "\"${getStringProp("IMAGE_URL", "https://image.tmdb.org/t/p/w780/", "ERROR: IMAGE_URL is missing")}\"")
            buildConfigField("String", "THUMBNAIL_IMAGE_URL", "\"${getStringProp("THUMBNAIL_IMAGE_URL", "https://img.youtube.com/vi/", "ERROR: THUMBNAIL_IMAGE_URL is missing")}\"")
            buildConfigField("String", "ENCRYPTED_DB_PASSPHRASE", "\"${getStringProp("ENCRYPTED_DB_PASSPHRASE", "YOUR_ENCRYPTED_DB_PASSPHRASE", "ERROR: ENCRYPTED_DB_PASSPHRASE is missing")}\"")
            buildConfigField("boolean", "ENABLE_CHUCKER", "true")

            if (!enableChuckerDev) {
                logger.warn("ERROR: Chucker must be ENABLED in DEV flavor. Please set enableChuckerDev=true in gradle.properties.")
            }
        }

        create("prod") {
            dimension = "version"

            buildConfigField("String", "BASE_URL", "\"${getStringProp("BASE_URL", "https://api.themoviedb.org/3/", "ERROR: BASE_URL is missing")}\"")
            buildConfigField("String", "API_KEY", "\"${getStringProp("API_KEY", "YOUR_API_KEY", "ERROR: API_KEY is missing")}\"")
            buildConfigField("String", "IMAGE_URL", "\"${getStringProp("IMAGE_URL", "https://image.tmdb.org/t/p/w780/", "ERROR: IMAGE_URL is missing")}\"")
            buildConfigField("String", "THUMBNAIL_IMAGE_URL", "\"${getStringProp("THUMBNAIL_IMAGE_URL", "https://img.youtube.com/vi/", "ERROR: THUMBNAIL_IMAGE_URL is missing")}\"")
            buildConfigField("String", "ENCRYPTED_DB_PASSPHRASE", "\"${getStringProp("ENCRYPTED_DB_PASSPHRASE", "YOUR_ENCRYPTED_DB_PASSPHRASE", "ERROR: ENCRYPTED_DB_PASSPHRASE is missing")}\"")
            buildConfigField("boolean", "ENABLE_CHUCKER", "false")

            if (enableChuckerProd) {
                logger.warn("ERROR: Chucker must be disabled in PROD flavor. Please set enableChuckerProd=false in gradle.properties.")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
        compose = true
    }
}

// APK Name
base {
    val timestamp = SimpleDateFormat("yyyyMMddHHmm").format(Date())
    archivesName = "TheMovieShow-v${android.defaultConfig.versionName}(${android.defaultConfig.versionCode})-${timestamp}"
}

dependencies {

    // Kotlin
    implementation(libs.core.ktx)

    // UI
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)

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
    testImplementation(libs.androidx.core.testing)

    // Justified text view
    implementation(libs.justifiedtextview)

    // View Model
    implementation(libs.lifecycle.viewmodel.ktx)

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
    testImplementation(libs.kotlinx.coroutines.test)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

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

    // Shimmer
    implementation(libs.shimmerLayout)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.activity)
    debugImplementation(libs.compose.ui.tooling)
}