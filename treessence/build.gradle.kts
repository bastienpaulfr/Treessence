import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.androidKotlin)
    alias(libs.plugins.dokka)
    alias(libs.plugins.versioning)
    alias(libs.plugins.maven.publish)
    id("signing")
}

apply(rootProject.file("gradle/ktlint.gradle.kts"))

android {
    compileSdk = 35

    namespace = "fr.bipi.treessence"

    defaultConfig {
        minSdk = 17

        testInstrumentationRunner = "androidx.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        debug {
            ext.set("alwaysUpdateBuildId", false)
        }
    }

    testOptions {
        targetSdk = 35
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.coroutines)

    implementation(libs.timber)
    implementation(libs.annotations)

    compileOnly(libs.slf4j)

    testImplementation(libs.junit)
    testImplementation(libs.mockito)
    testImplementation(libs.roboelectric)
    testImplementation(libs.awaitility)
    testImplementation(libs.kluent)
    testImplementation(libs.hamcrest)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.coroutines.test)

    androidTestImplementation(libs.android.runner)
}

mavenPublishing {
    coordinates("uk.kulikov", "treesense", versioning.info.display)

    configure(
        AndroidSingleVariantLibrary(
            variant = "release",
            sourcesJar = true,
            publishJavadocJar = true,
        )
    )

    pom {
        name.set("Treesense")
        description.set("Some trees for Timber lib")
        inceptionYear.set("2025")
        url.set("https://github.com/LionZXY/Treessence")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("lionzxy")
                name.set("LionZXY")
                url.set("https://github.com/LionZXY/")
            }
            developer {
                id.set("bastienpaulfr")
                name.set("Bastien Paul")
                url.set("https://github.com/bastienpaulfr/")
            }
        }
        scm {
            url.set("https://github.com/LionZXY/Treessence")
            connection.set("scm:git:git://github.com/LionZXY/Treessence.git")
            developerConnection.set("scm:git:ssh://git@github.com/LionZXY/Treessence.git")
        }
    }

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
}