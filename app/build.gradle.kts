import java.text.SimpleDateFormat
import java.util.Date

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.devtools.ksp")
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
        versionCode = 127
        versionName = "1.27"

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
    val coreKtxVersion = "1.9.0"
    implementation("androidx.core:core-ktx:$coreKtxVersion")

    // UI
    val appCompatVersion = "1.6.1"
    val materialVersion = "1.11.0"
    val constraintLayoutVersion = "2.1.4"
    val legacySupportVersion = "1.0.0"
    implementation("androidx.appcompat:appcompat:$appCompatVersion")
    implementation("com.google.android.material:material:$materialVersion")
    implementation("androidx.constraintlayout:constraintlayout:$constraintLayoutVersion")
    implementation("androidx.legacy:legacy-support-v4:$legacySupportVersion")

    // Glide
    val glideVersion = "4.16.0"
    implementation("com.github.bumptech.glide:glide:$glideVersion")

    // Navigation component
    val navKTXVersion = "2.7.7"
    implementation("androidx.navigation:navigation-fragment-ktx:$navKTXVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navKTXVersion")

    // Fragment KTX
    val fragmentKTXVersion = "1.8.1"
    implementation("androidx.fragment:fragment-ktx:$fragmentKTXVersion")

    // Testing
    val junitVersion = "4.13.2"
    val mockitoCoreVersion = "5.12.0"
    val espressoVersion = "3.6.1"
    val mockitoInlineVersion = "5.2.0"
    val coreTestingVersion = "2.2.0"
    val testExtVersion = "1.2.1"
    val testRunnerVersion = "1.6.1"
    testImplementation("junit:junit:$junitVersion")
    androidTestImplementation("junit:junit:$junitVersion")
    androidTestImplementation("androidx.test.ext:junit:$testExtVersion")
    androidTestImplementation("androidx.test:runner:$testRunnerVersion")
    androidTestImplementation("androidx.test:rules:$testRunnerVersion")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:$espressoVersion")
    testImplementation("androidx.arch.core:core-testing:$coreTestingVersion")
    testImplementation("org.mockito:mockito-core:$mockitoCoreVersion")
    testImplementation("org.mockito:mockito-inline:$mockitoInlineVersion")

    // Justified text view
    val justifiedTextVersion = "1.1.0"
    implementation("com.codesgood:justifiedtextview:$justifiedTextVersion")

    // View Model
    val lifeCycleVersion = "2.2.0"
    val lifeCycleKtxVersion = "2.8.3"
    implementation("androidx.lifecycle:lifecycle-extensions:$lifeCycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifeCycleKtxVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifeCycleKtxVersion")

    // Network
    val retrofitVersion = "2.11.0"
    val okHttpVersion = "4.12.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okHttpVersion")

    // Toasty
    val toastVersion = "1.5.2"
    implementation("com.github.GrenderG:Toasty:$toastVersion")

    // Room DB
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    testImplementation("androidx.room:room-testing:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // Coroutine
    val coroutineCoreVersion = "1.8.0"
    val coroutineAndroidVersion = "1.8.0"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineCoreVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineAndroidVersion")

    // Hilt
    val hiltVersion = "2.51.1"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")

    // Firebase
    val firebaseCrashlyticsVersion = "19.0.2"
    val firebaseCrashlyticsKtxVersion = "22.0.2"
    implementation("com.google.firebase:firebase-crashlytics-ktx:$firebaseCrashlyticsVersion")
    implementation("com.google.firebase:firebase-analytics-ktx:$firebaseCrashlyticsKtxVersion")

    // Lottie
    val lottieVersion = "6.4.0"
    implementation("com.airbnb.android:lottie:$lottieVersion")

    // FlexboxLayout
    val flexboxVersion = "3.0.0"
    implementation("com.google.android.flexbox:flexbox:$flexboxVersion")

    // Encryption
    val sqlCipherVersion = "4.5.3"
    val sqliteVersion = "2.4.0"
    implementation("net.zetetic:android-database-sqlcipher:$sqlCipherVersion")
    implementation("androidx.sqlite:sqlite-ktx:$sqliteVersion")
}

kapt {
    correctErrorTypes = true
}