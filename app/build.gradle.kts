val appName = "Trante"
val packageName = "chiogros." + appName.lowercase()
val providerName = ".ui.saf.CustomDocumentsProvider"

android {
    namespace = packageName
    compileSdk = 36

    defaultConfig {
        applicationId = android.namespace
        minSdk = 26
        targetSdk = android.compileSdk
        versionCode = 1
        versionName = "1.0"

        // Values to be used from manifest file
        manifestPlaceholders["app_name"] = appName
        manifestPlaceholders["logo"] = ""
        manifestPlaceholders["package_name"] = packageName
        manifestPlaceholders["provider_name"] = providerName

        // Values to be used from code
        buildConfigField("String", "APP_NAME", "\"$appName\"")
        buildConfigField("String", "PACKAGE_NAME", "\"$packageName\"")
        buildConfigField("String", "PROVIDER_NAME", "\"$providerName\"")
        buildConfigField("String", "VERSION_NAME", "\"$versionName\"")
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
            applicationIdSuffix = ".debug"
            manifestPlaceholders["logo"] = "_debug"

            val debugPackageName: String = packageName + applicationIdSuffix
            manifestPlaceholders["package_name"] = debugPackageName
            buildConfigField("String", "PACKAGE_NAME", "\"$debugPackageName\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
        buildConfig = true
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
    implementation(libs.aboutlibraries.core)
    implementation(libs.aboutlibraries.compose.m3)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material.icons.extended)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.sshd.sftp)

    // Those following are for optimizations to work, although
    // I don't understand why they need to be referenced.
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("org.slf4j:slf4j-simple:2.0.17")
    //implementation("org.bouncycastle:bcpg-jdk18on:1.81")
    //implementation("org.bouncycastle:bcpkix-jdk18on:1.81")
    implementation("org.bouncycastle:bcprov-jdk18on:1.81")
    //implementation("org.bouncycastle:bcutil-jdk18on:1.81")
    //implementation("javax.naming:jndi:1.2.1")
    //implementation("javax.management:jmx:1.2.1")
}

// Plugins are only used to parse Gradle configuration.
plugins {
    alias(libs.plugins.aboutlibraries.plugin.android)
    alias(libs.plugins.android.application)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.com.google.devtools.ksp)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin(libs.plugins.plugin.serialization.get().pluginId).version(libs.versions.serialization)
}