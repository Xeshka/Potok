plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.android.library.jacoco)
    alias(libs.plugins.app.hilt)
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
    //api(projects.core.datastore)
    api(projects.core.network)
    implementation(libs.androidx.work.ktx)

    //testImplementation(libs.kotlinx.coroutines.test)
    //testImplementation(libs.kotlinx.serialization.json)
    //testImplementation(projects.core.datastoreTest)
    //testImplementation(projects.core.testing)
}
