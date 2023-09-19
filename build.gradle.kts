plugins {
    java
    id("org.springframework.boot") version "3.1.3"
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

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("ch.qos.logback:logback-classic")
    implementation("org.mongodb:mongodb-driver-reactivestreams:4.10.0")
    implementation("com.google.guava:guava:32.0.1-jre")
    implementation("jakarta.annotation:jakarta.annotation-api:1.3.5")

    compileOnly("org.projectlombok:lombok")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api:1.3.5")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.google.truth:truth:1.1.3")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.modulith:spring-modulith-bom:1.0.0")
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
