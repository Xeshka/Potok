plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.android.library.jacoco)
    alias(libs.plugins.app.hilt)
    alias(libs.plugins.app.android.room)
    id("kotlinx-serialization")
}

android {
    namespace = "ru.kolesnik.potok.core.data"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    api(projects.core.common)
    api(projects.core.database)
    api(projects.core.network)
    implementation(libs.androidx.work.ktx)
}
