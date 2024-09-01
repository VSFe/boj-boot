plugins {
    kotlin("jvm") version "1.9.25"
    id("maven-publish")
}

group = "io.github.vsfe"
version = "1.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.junit:junit-bom:5.10.0"))
    implementation("org.junit.jupiter:junit-jupiter")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "io.github.vsfe"
            artifactId = "boj-commons"
            version = "1.0.1"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}
