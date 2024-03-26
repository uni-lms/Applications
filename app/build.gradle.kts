import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.io.ByteArrayOutputStream

fun Project.gitCommitCount(): Int {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine("git", "rev-list", "--count", "HEAD")
        standardOutput = stdout
    }
    return stdout.toString().trim().toInt()
}

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    kotlin("kapt")
    kotlin("plugin.serialization")
    id("com.google.dagger.hilt.android")
}

val majorVersion = 2
val minorVersion = 0
val patchVersion = 0

android {
    namespace = "ru.aip.intern"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.aip.intern"
        minSdk = 28
        targetSdk = 34
        versionCode = gitCommitCount()
        versionName = "$majorVersion.$minorVersion.$patchVersion"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("keystore/ru.unilms.jks")
            storePassword = System.getenv("SIGNING_STORE_PASSWORD")
            keyAlias = System.getenv("SIGNING_KEY_ALIAS")
            keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            applicationVariants.all {
                val variant = this
                variant.outputs
                    .map { it as BaseVariantOutputImpl }
                    .forEach { output ->
                        val outputFileName =
                            "${
                                rootProject.name.replace(
                                    " ",
                                    ""
                                )
                            }-${project.extensions.extraProperties["fullVersion"]}.apk"
                        output.outputFileName = outputFileName
                    }
            }
        }

        getByName("debug") {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("com.google.firebase:firebase-messaging-ktx:23.4.1")
    val ktorVersion = "2.3.8"

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material:material")

    implementation("androidx.navigation:navigation-compose:2.7.7")

    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
    implementation("com.google.firebase:firebase-analytics")

    implementation("com.google.dagger:hilt-android:2.51")
    kapt("com.google.dagger:hilt-android-compiler:2.51")

    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    implementation("androidx.compose.runtime:runtime-livedata")

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation("com.kizitonwose.calendar:compose:2.5.0")

    implementation("io.ktor:ktor-client-core:$ktorVersion") // Базовая клиентская библиотека
    implementation("io.ktor:ktor-client-android:$ktorVersion") // Плагин для работы в Android
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion") // Плагины для парсинга JSON в модели и обратно
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-client-serialization:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion") // Плагин для логгирования
    implementation("ch.qos.logback:logback-classic:1.2.11")  // Драйвер логгирования (последняя подддерживаемая в Android версия)

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

kapt {
    correctErrorTypes = true
}

task("printVersionName") {
    val versionName = "${
        android.defaultConfig.versionName!!.replace(
            ".",
            ""
        )
    }-${gitCommitCount()}"
    project.extensions.extraProperties["fullVersion"] = versionName
    println(versionName)
}