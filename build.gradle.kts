// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.navigation.safe.args) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
}

buildscript {
    dependencies {
        classpath(libs.navigation.safe.args)
        // AGP 9 built-in Kotlin defaults to KGP 2.2.10; pin it to the catalog's Kotlin version
        classpath(libs.kotlin.gradle.plugin)
    }
}
