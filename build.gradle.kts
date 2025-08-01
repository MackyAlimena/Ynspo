// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.dagger.hilt.android") version "2.56.1" apply false
    id("com.google.devtools.ksp") version "2.0.0-1.0.21" apply false
    alias(libs.plugins.compose.compiler) apply false

}
buildscript {
    dependencies {
        classpath(libs.google.services.v432)
    }
}


