buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:3.4.0")
        classpath("com.google.gms:google-services:4.3.14")
        classpath("com.google.firebase:firebase-appdistribution-gradle:3.0.3")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id ("com.android.application") version "7.2.2" apply false
    id ("com.android.library") version "7.2.2" apply false
    id ("org.jetbrains.kotlin.android") version "1.7.10" apply false
    id ("com.google.dagger.hilt.android") version "2.42" apply false
    // kotlinx serialization used for retrofit json adapter
    id ("org.jetbrains.kotlin.plugin.serialization") version "1.7.10"
    // apply navigation here
    id ("androidx.navigation.safeargs") version "2.5.2" apply false
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1" apply false
    id("io.gitlab.arturbosch.detekt") version "1.19.0" apply false
}

task<Delete>("clean") {
    delete = setOf(rootProject.buildDir)
}
