plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.android.library.compose)
    alias(libs.plugins.app.android.library.jacoco)
}

android {
    namespace = "ru.kolesnik.potok.core.ui"
}

dependencies {
    api(libs.androidx.metrics)
    api(projects.core.analytics)
    api(projects.core.designsystem)
    api(projects.core.model)

    implementation(libs.androidx.browser)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)

    //androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    //androidTestImplementation(projects.core.testing)
}
