plugins {
    alias(libs.plugins.app.android.feature)
    alias(libs.plugins.app.android.library.compose)
    alias(libs.plugins.app.android.library.jacoco)
}

android {
    namespace = "ru.kolesnik.potok.feature.lifearea"
}

dependencies {
    implementation(projects.core.data)
    implementation(libs.androidx.media3.common.ktx)

    //testImplementation(projects.core.testing)

    //androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    //androidTestImplementation(projects.core.testing)
}
