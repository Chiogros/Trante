val appName = "Trante"
val packageName = "chiogros." + appName.lowercase()
val providerClass = ".ui.saf.CustomDocumentsProvider"

android {
    namespace = packageName
    compileSdk = 36

    defaultConfig {
        applicationId = android.namespace
        minSdk = 26
        targetSdk = android.compileSdk
        versionCode = 4
        versionName = "1.2.0"

        // Values to be used from manifest file
        manifestPlaceholders["appName"] = appName

        // Values to be used from code
        buildConfigField("String", "APP_NAME", "\"$appName\"")
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

            val providerName = "$packageName$providerClass"
            manifestPlaceholders["providerName"] = providerName
            manifestPlaceholders["appLogo"] = "@mipmap/ic_launcher"
            buildConfigField("String", "PROVIDER_NAME", "\"$providerName\"")
        }
        debug {
            isDebuggable = true
            applicationIdSuffix = ".debug"

            val debugProviderName: String = packageName + applicationIdSuffix + providerClass
            manifestPlaceholders["providerName"] = debugProviderName
            manifestPlaceholders["appLogo"] = "@mipmap/ic_launcher_debug"
            buildConfigField("String", "PROVIDER_NAME", "\"$debugProviderName\"")
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

    packaging {
        resources {
            pickFirsts += "META-INF/DEPENDENCIES"
            pickFirsts += "META-INF/LICENSE.md"
            pickFirsts += "META-INF/NOTICE.md"
            pickFirsts += "META-INF/jandex.idx"
            pickFirsts += "pom.xml"
            pickFirsts += "component.properties"
            pickFirsts += "bean.properties"
            pickFirsts += "other.properties"
            pickFirsts += "dev-consoles.properties"
        }
    }

    androidResources {
        @Suppress("UnstableApiUsage")
        generateLocaleConfig = true
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
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)

    implementation(libs.sshd.sftp)
    // To avoid logging error
    runtimeOnly(libs.slf4j.api)
    runtimeOnly(libs.slf4j.nop)

    implementation(libs.camel.ftp)
}

// Plugins are used to parse Gradle configuration.
plugins {
    alias(libs.plugins.aboutlibraries.plugin.android)
    alias(libs.plugins.android.application)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.com.google.devtools.ksp)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

// Required for Room DB schemas migration
// https://developer.android.com/training/data-storage/room/migrating-db-versions#test
room {
    schemaDirectory("$projectDir/schemas")
}
