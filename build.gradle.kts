plugins {
    java
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "org.braid.society.secret"
version = "1.0.0"

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

    implementation("org.springframework.boot:spring-boot-starter-web:3.2.0")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("com.google.guava:guava:33.0.0-jre")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    implementation("org.apache.commons:commons-csv:1.10.0")

    compileOnly("org.projectlombok:lombok:1.18.30")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:3.2.0")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api:2.1.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.0")
    testImplementation("com.google.truth:truth:1.2.0") {
        isTransitive = false
    }
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.modulith:spring-modulith-bom:1.1.0")
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
