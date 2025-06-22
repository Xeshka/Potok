plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.android.library.jacoco)
    alias(libs.plugins.app.android.room)
    alias(libs.plugins.app.hilt)
}

android {
    namespace = "ru.kolesnik.potok.core.database"
}

dependencies {
    api(projects.core.model)
    api(projects.core.network)

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}
