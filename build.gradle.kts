import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/*
 * Copyright Notice for CrossWise
 * Copyright (c) at Carina Sophie Schoppe 2022
 * File created on 8/11/22, 2:31 PM by Carina The Latest changes made by Carina on 8/11/22, 2:31 PM All contents of "build.gradle.kts" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at Carina Sophie Schoppe. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of Carina Sophie Schoppe.
 */

plugins {
    id("java")
    idea
    kotlin("jvm") version "+"
    id("com.github.johnrengelman.shadow") version "+"
    id("org.openjfx.javafxplugin") version "+"
    id("application")
}

group = "de.carina"
version = "1.0.1"
description = "Crosswise Pc-Game"


repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.google.code.gson:gson:+")
    testImplementation(kotlin("test"))
    implementation("org.jetbrains:annotations:+")
    implementation("org.projectlombok:lombok:+")
    annotationProcessor("org.projectlombok:lombok:+")
    testCompileOnly("org.projectlombok:lombok:+")
    testAnnotationProcessor("org.projectlombok:lombok:+")
}

javafx {
    version = "+"
    modules("javafx.controls", "javafx.fxml")

}

tasks {

    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
    test {
        useJUnitPlatform()
    }

    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf(
                "-Xuse-k2",
                "-Xjdk-release=17"
            )
            jvmTarget = "17"
            languageVersion = "1.7"
        }
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

application {
    mainClass.set("me.carinasophie.Main")
}