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
        targetSdk = 33
        versionCode = 126
        versionName = "1.26"

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
    implementation("androidx.core:core-ktx:1.9.0")

    // UI
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    // Glide
    val glideVersion = "4.16.0"
    implementation("com.github.bumptech.glide:glide:$glideVersion")

    // Navigation component
    val navKTXVersion = "2.7.5"
    implementation("androidx.navigation:navigation-fragment-ktx:$navKTXVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navKTXVersion")

    // Fragment KTX
    val fragmentKTXVersion = "1.6.2"
    implementation("androidx.fragment:fragment-ktx:$fragmentKTXVersion")

    // Testing
    val junitVersion = "4.13.2"
    val mockitoCoreVersion = "5.7.0"
    val espressoVersion = "3.5.1"
    val mockitoInlineVersion = "5.2.0"
    testImplementation("junit:junit:$junitVersion")
    androidTestImplementation("junit:junit:$junitVersion")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:$espressoVersion")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.mockito:mockito-core:$mockitoCoreVersion")
    testImplementation("org.mockito:mockito-inline:$mockitoInlineVersion")

    // Justified text view
    val justifiedTextVersion = "1.1.0"
    implementation("com.codesgood:justifiedtextview:$justifiedTextVersion")

    // View Model
    val lifeCycleVersion = "2.2.0"
    val lifeCycleKtxVersion = "2.6.2"
    implementation("androidx.lifecycle:lifecycle-extensions:$lifeCycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifeCycleKtxVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifeCycleKtxVersion")

    // Network
    val retrofitVersion = "2.9.0"
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
    val coroutineCoreVersion = "1.7.3"
    val coroutineAndroidVersion = "1.7.3"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineCoreVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineAndroidVersion")

    // Hilt
    val hiltVersion = "2.48.1"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")

    // Firebase
    val firebaseCrashlyticsVersion = "18.6.0"
    val firebaseCrashlyticsKtxVersion = "21.5.0"
    implementation("com.google.firebase:firebase-crashlytics-ktx:$firebaseCrashlyticsVersion")
    implementation("com.google.firebase:firebase-analytics-ktx:$firebaseCrashlyticsKtxVersion")

    // Lottie
    val lottieVersion = "6.1.0"
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