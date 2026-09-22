plugins {
    id("java")
    kotlin("jvm") version "2.3.0"
}

group = "org.saintqd"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven(url = "https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven(url = "https://jitpack.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.2+")
    compileOnly("me.clip:placeholderapi:2.11.6") // repo.extendedclip.com
    compileOnly("com.github.SaintQd:AsureLib:v1.0")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
}

tasks.test {
    useJUnitPlatform()
}
tasks.withType<Jar> {

    // To avoid the duplicate handling strategy error
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // To add all the dependencies otherwise a "NoClassDefFoundError" error
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })

}
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}
kotlin {
    jvmToolchain(25)
}