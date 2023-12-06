plugins {
    id("io.qameta.allure") version "2.11.2"
    id("java")
    id("org.gradle.test-retry") version "1.0.0"
    checkstyle
}

group = "inka.simbirsoft"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/io.rest-assured/rest-assured
    testImplementation("io.rest-assured:rest-assured:5.3.2")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
    implementation("com.fasterxml.jackson.core:jackson-databind:2.9.5")

    // https://mvnrepository.com/artifact/io.qameta.allure/allure-rest-assured
    implementation("io.qameta.allure:allure-rest-assured:2.24.0")
    // https://mvnrepository.com/artifact/org.testng/testng
    testImplementation("org.testng:testng:7.8.0")
    // https://mvnrepository.com/artifact/org.projectlombok/lombok
    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

}

tasks.getByName<Test>("test") {
    useTestNG() {
        outputDirectory = file("results")
        useDefaultListeners = true
    }
}