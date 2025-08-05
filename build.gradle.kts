plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-rc3"
}

group = "one.pkg"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.13.1")
    implementation("one.tranic:t-proxy:1.0.1")
}

val targetJavaVersion = 17
tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
        options.release.set(targetJavaVersion)
    }
}

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
    withSourcesJar()
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "one.pkg.ffmp.FFMPMain"
    }
}