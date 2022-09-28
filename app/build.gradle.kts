@file:Suppress("MagicNumber", "SpellCheckingInspection", "ComplexMethod")

import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

val envReleaseNote: String = System.getenv("RELEASE_NOTE") ?: "LOCAL_BUILD"

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlinx-serialization")
    id("androidx.navigation.safeargs.kotlin")
    id("org.jlleitschuh.gradle.ktlint")
    id("io.gitlab.arturbosch.detekt")
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.zgsbrgr.demo.fiba"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }

        val environmentFlavorDimension = "environment"
        flavorDimensions.add(environmentFlavorDimension)
        productFlavors {
            create("production") {
                isDefault = true
                dimension = environmentFlavorDimension
            }

            create("staging") {
                dimension = environmentFlavorDimension

                applicationIdSuffix = ".stg"
                versionNameSuffix = "-stg"
            }

            create("development") {
                dimension = environmentFlavorDimension

                applicationIdSuffix = ".dev"
                versionNameSuffix = "-dev"
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    packagingOptions {
        resources {
            excludes += "/META-INFinstabug_comment_hint_bug/{AL2.0,LGPL2.1}"
            excludes += "META-INF/*"
        }
    }

    kotlinOptions {
        allWarningsAsErrors = true
    }

    lint {
        ignoreWarnings = false
        warningsAsErrors = true
    }
}

detekt {
    allRules = true
    config = files("detekt-config.yml")
    buildUponDefaultConfig = true
}

ktlint {
    reporters {
        reporter(ReporterType.HTML)
        reporter(ReporterType.CHECKSTYLE)
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    // hilt
    implementation("com.google.dagger:hilt-android:2.42")
    kapt("com.google.dagger:hilt-android-compiler:2.42")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // kotlinx serialization used for retrofit json adapter
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")

    // by viewmodels
    implementation("androidx.fragment:fragment-ktx:1.5.3")

    implementation("com.github.bumptech.glide:glide:4.13.2")
    kapt("com.github.bumptech.glide:compiler:4.13.2")

    // recyclerview, to set state restoration policy
    implementation("androidx.recyclerview:recyclerview:1.2.1")

    // navigation component
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.2")
}
