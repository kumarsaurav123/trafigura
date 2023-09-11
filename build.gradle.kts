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
    testImplementation("junit:junit:4.13.1")
    annotationProcessor("org.projectlombok:lombok:1.18.8")
    implementation("org.projectlombok:lombok:1.18.8")
    implementation("com.google.guava:guava:30.1-jre")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.4.4")


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