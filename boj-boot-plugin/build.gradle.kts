plugins {
    kotlin("jvm") version "1.9.25"
    id("java-gradle-plugin")
    id("maven-publish")
}

group = "org.vsfe"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("io.github.vsfe:boj-commons:1.0.0")
    implementation("org.jsoup:jsoup:1.15.3")
}

gradlePlugin {
    plugins {
        create("bojBootPlugin") {
            id = "org.vsfe.boj-boot-plugin"
            implementationClass = "org.vsfe.BojBootPlugin"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}