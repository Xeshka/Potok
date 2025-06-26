plugins {
    alias(libs.plugins.app.android.application)
    alias(libs.plugins.app.android.application.compose)
    alias(libs.plugins.app.android.application.flavors)
    alias(libs.plugins.app.android.application.jacoco)
    alias(libs.plugins.app.hilt)
    id("jacoco")
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "ru.kolesnik.potok"

    defaultConfig {
        applicationId = "ru.kolesnik.potok"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "ru.kolesnik.potok.core.testing.AppTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = AppBuildType.DEBUG.applicationIdSuffix
        }
        release {
            isMinifyEnabled = true
            applicationIdSuffix = AppBuildType.RELEASE.applicationIdSuffix
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            // Ensure the release build type is debuggable for a staging environment.
            // This is only used to configure the Baseline Profile generation.
            // Note that this is a small bug in the Baseline Profiles plugin and will be fixed in an upcoming release.
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(projects.feature.lifearea)
    implementation(projects.feature.task)

    implementation(projects.core.analytics)
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.designsystem)
    implementation(projects.core.ui)
    implementation(projects.core.model)
    implementation(projects.core.notifications)

    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.layout)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.tracing.ktx)
    implementation(libs.androidx.window.core)
    implementation(libs.androidx.work.ktx)
    implementation(libs.kotlinx.coroutines.guava)
    implementation(libs.coil.kt)

    debugImplementation(libs.androidx.compose.ui.testManifest)
    debugImplementation(projects.core.testing)

    // Remove kaptTest and kaptAndroidTest - these are not valid configurations
    // Use testImplementation and androidTestImplementation instead
    testImplementation(projects.core.testing)
    testImplementation(libs.androidx.navigation.testing)
    testImplementation(libs.accompanist.permissions)
    testImplementation(libs.hilt.android.testing)

    androidTestImplementation(projects.core.testing)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.hilt.android.testing)

    // Remove screenshotTesting reference - use the correct project reference
    testImplementation(projects.core.testing)
    
    baselineProfile(projects.benchmarks)
}

baselineProfile {
    // Don't build on every iteration of a full assemble.
    // Instead enable generation directly for the release build variant.
    automaticGenerationDuringBuild = false
}

dependencyGuard {
    configuration("prodReleaseRuntimeClasspath")
}