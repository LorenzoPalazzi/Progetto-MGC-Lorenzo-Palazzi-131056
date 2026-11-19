plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "it.unicam.cs.mpgc.rpg131056"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

javafx {
    version = "25"
    modules = listOf("javafx.controls")
}

repositories {
    mavenCentral()
}

dependencies {
    
    implementation("com.google.code.gson:gson:2.10.1")  
    implementation("org.openjfx:javafx-controls:21")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


application {
    mainClass.set("it.unicam.cs.mpgc.rpg131056.view.AppLauncher")
}

tasks.test {
    useJUnitPlatform()
}