plugins {
    alias(libs.plugins.app.jvm.library)
    alias(libs.plugins.app.hilt)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
}