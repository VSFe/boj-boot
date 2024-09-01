plugins {
    kotlin("jvm") version "1.9.25"
    id("java-gradle-plugin")
    id("maven-publish")
}

group = "io.github.vsfe"
version = "1.0.1"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("io.github.vsfe:boj-commons:1.0.1")
    implementation("org.jsoup:jsoup:1.15.3")
}

gradlePlugin {
    plugins {
        create("bojBootPlugin") {
            id = "io.github.vsfe.boj-boot-plugin"
            implementationClass = "io.github.vsfe.BojBootPlugin"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}