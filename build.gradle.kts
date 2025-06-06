

buildscript {

    dependencies {
        classpath("com.google.gms:google-services:4.3.8");

    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false

        // Add the dependency for the Google services Gradle plugin
        id("com.google.gms.google-services") version "4.4.1" apply false
        id("com.android.library") version "7.4.2" apply false

}
