import org.gradle.kotlin.dsl.version

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version ("7.1.2")

}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    annotationProcessor("org.projectlombok:lombok:1.18.8")
    implementation("org.projectlombok:lombok:1.18.8")

}
tasks.test {
    useJUnitPlatform()
}
tasks.shadowJar { manifest { attributes
    "Main-Class : org.example.TestDriver"

} }
tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "org.example.TestDriver"
    }
}