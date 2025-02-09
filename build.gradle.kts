import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

val ktorVersion: String by project
val moshiVersion: String by project
val kotlinxCoroutinesVersion: String by project
val jakartaMailVersion: String by project
val exposedVersion: String by project
val logbackVersion: String by project
val slf4jVersion: String by project

plugins {
    java
    idea
    `java-library`
    `maven-publish`
    id("org.jetbrains.kotlin.jvm") version "2.1.0" apply false
    id("org.jetbrains.dokka") version "1.9.20"
    id("com.gradleup.shadow") version "8.3.5"
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "maven-publish")
    apply(plugin = "java")
    apply(plugin = "com.gradleup.shadow")
    if (name != "platform") {
        apply(plugin = "org.jetbrains.dokka")
    }


    repositories {
        mavenLocal()
        maven("https://maven.aliyun.com/repository/central")
        maven("https://maven.wcpe.top/repository/maven-public/")
        maven("https://repo1.maven.org/maven2")
        maven("https://jitpack.io")
        maven("https://libraries.minecraft.net")
        maven("https://repo.codemc.io/repository/nms/")
        mavenCentral()
    }

    dependencies {
        compileOnly(kotlin("stdlib"))

        implementation("org.slf4j:slf4j-api:$slf4jVersion")
        implementation("ch.qos.logback:logback-core:$logbackVersion")
        implementation("ch.qos.logback:logback-classic:$logbackVersion")

        implementation("org.msgpack:msgpack-core:0.9.3")

        implementation("mysql:mysql-connector-java:8.0.29")
        implementation("com.alibaba:druid:1.2.11")
        implementation("org.mybatis:mybatis:3.5.10")

        implementation("redis.clients:jedis:5.1.2")
        implementation("org.redisson:redisson:3.38.1")


        implementation("org.quartz-scheduler:quartz:2.3.2")
        implementation("com.google.code.gson:gson:2.9.0")
        implementation("com.squareup.moshi:moshi:$moshiVersion")
        implementation("com.squareup.moshi:moshi-kotlin:$moshiVersion")
        implementation("dev.zacsweers.moshix:moshi-metadata-reflect:0.18.3")
        implementation("jakarta.mail:jakarta.mail-api:$jakartaMailVersion")
        implementation("org.eclipse.angus:jakarta.mail:2.0.3")

        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")

        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.2")

        implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
        implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
        implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
        implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")

        implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
        implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
        implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")

        implementation("io.ktor:ktor-client-core:$ktorVersion")
        implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
        implementation("io.ktor:ktor-client-gson:$ktorVersion")
        implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
        implementation("io.ktor:ktor-serialization-gson:$ktorVersion")
        implementation("org.jetbrains:annotations:13.0")

        compileOnly("org.projectlombok:lombok:1.18.34")
        annotationProcessor("org.projectlombok:lombok:1.18.34")
        testCompileOnly("org.projectlombok:lombok:1.18.34")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.34")

        testImplementation(kotlin("test"))
        testImplementation(platform("org.junit:junit-bom:5.9.1"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    java {
        withSourcesJar()
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks {
        test {
            useJUnitPlatform()
        }

        withType<JavaCompile> {
            options.encoding = "UTF-8"
            options.compilerArgs.addAll(listOf("-XDenableSunApiLintControl"))
        }
        named<KotlinJvmCompile>("compileKotlin") {
            compilerOptions {
                jvmTarget = JvmTarget.JVM_1_8
                apiVersion = KotlinVersion.KOTLIN_2_1
                languageVersion = KotlinVersion.KOTLIN_2_1
                freeCompilerArgs = listOf("-Xjvm-default=all", "-Xskip-metadata-version-check")
            }
        }
        named<ShadowJar>("shadowJar") {
            archiveFileName.set("${rootProject.name}-${project.name}-${project.version}.jar")
            relocate("io.netty", "top.wcpe.lib.io.netty")
            enabled = false
        }
    }
    publishing {
        repositories {
            maven {
                credentials {
                    username = project.findProperty("username").toString()
                    password = project.findProperty("password").toString()
                }
                authentication {
                    create<BasicAuthentication>("basic")
                }
                val releasesRepoUrl = uri("https://maven.wcpe.top/repository/maven-releases/")
                val snapshotsRepoUrl = uri("https://maven.wcpe.top/repository/maven-snapshots/")
                url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            }
            mavenLocal()
        }


        publications {
            create<MavenPublication>("maven") {
                if (project.name == "platform") {
                    return@create
                }
                artifactId = "${rootProject.name}-${project.name}".lowercase()
                groupId = project.group.toString()
                version = "${project.version}"
                from(components["java"])
                println("> Apply \"$groupId:$artifactId:$version\"")
            }
        }
    }

}