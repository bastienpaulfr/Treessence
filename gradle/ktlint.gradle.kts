import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the

configurations.register("ktlint")

tasks.register<JavaExec>("ktlint") {
    description = "Check Kotlin code style."
    mainClass = "com.pinterest.ktlint.Main"
    classpath = configurations.getByName("ktlint")
    args("src/**/*.kt", "--reporter=plain", "--reporter=checkstyle,output=${layout.buildDirectory}/reports/ktlint/ktlint.xml")

    //  --disabled_rules=filename ==> no check of filename
    // see https://github.com/pinterest/ktlint#usage for more
}

tasks.register<JavaExec>("ktlintFormat") {
    description = "Fix Kotlin code style deviations."
    mainClass = "com.pinterest.ktlint.Main"
    classpath = configurations.getByName("ktlint")
    args("-F", "src/**/*.kt")

    // Will try to auto-format what is possible
}

// Workaround for https://github.com/gradle/gradle/issues/15383
val Project.libs: LibrariesForLibs
    get() = the<LibrariesForLibs>()

dependencies {
    add("ktlint", libs.ktlint)
}