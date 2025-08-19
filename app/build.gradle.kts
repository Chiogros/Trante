plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.com.google.devtools.ksp)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin(libs.plugins.plugin.serialization.get().pluginId).version(libs.versions.serialization)
}


android {
    namespace = "chiogros.cost"
    compileSdk = 36

    defaultConfig {
        applicationId = android.namespace
        minSdk = 26
        targetSdk = android.compileSdk
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }
    packaging {
        resources {
            pickFirsts += "META-INF/DEPENDENCIES"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.room.runtime)
    implementation(platform(libs.androidx.compose.bom))
    ksp(libs.androidx.room.compiler)
    implementation(libs.sshd.sftp)
}