plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.androidKotlin) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.versioning) apply false
    alias(libs.plugins.maven.publish) apply false
}