plugins {
    java
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
}

group = "org.braid.society.secret"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-web:3.1.4")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("com.google.guava:guava:32.1.3-jre")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    implementation("org.apache.commons:commons-csv:1.10.0")

    compileOnly("org.projectlombok:lombok:1.18.30")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:3.1.5")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api:2.1.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.4")
    testImplementation("com.google.truth:truth:1.1.5") {
        isTransitive = false
    }
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.modulith:spring-modulith-bom:1.0.1")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

extensions.findByName("buildScan")?.withGroovyBuilder {
    setProperty("termsOfServiceUrl", "https://gradle.com/terms-of-service")
    setProperty("termsOfServiceAgree", "yes")
}

tasks.bootJar {
    this.archiveFileName.set("${project.name}.jar")
}
