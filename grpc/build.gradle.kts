plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.protobuf)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.kotlinx.rpc.plugin)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/krpc/grpc")
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    implementation(libs.krpc.grpc.core)
    implementation(libs.grpc.netty)
}