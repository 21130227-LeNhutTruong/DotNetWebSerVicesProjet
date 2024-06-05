buildscript {
    repositories{
        // Add the Google repository
        mavenCentral()
        // Add the Google Maven repository
        google()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
        // Add the dependency for the navigation Component
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
    // Add the dependency for the navigation Component
}


