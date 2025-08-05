import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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
    implementation("one.pkg:tiny-utils:1.5.0")
    implementation("net.java.dev.jna:jna:5.17.0")
    implementation("net.java.dev.jna:jna-platform:5.17.0")
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
        attributes["Implementation-Version"] = archiveVersion
        attributes["Implementation-Title"] = "FFMP"
        attributes["Implementation-Timestamp"] = Instant.now().atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_INSTANT)
        attributes["Specification-Vendor"] = "404Setup"
        attributes["Specification-Title"] = "ffmp"
        attributes["Specification-Github"] = "404Setup/FFMP"
        attributes["Automatic-Module-Name"] = "one.pkg.ffmp"
    }
}