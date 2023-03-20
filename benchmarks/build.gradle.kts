description = "Benchmarks for kotlin-result."

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.kotlinx.benchmark")
    id("org.jetbrains.kotlin.plugin.allopen")
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
    annotation("org.openjdk.jmh.annotations.BenchmarkMode")
}

benchmark {
    targets {
        register("jvm")
        register("js")
    }
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    js(IR) {
        nodejs()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":kotlin-result-coroutines"))
                implementation(libs.kotlin.benchmark.runtime)
                implementation(libs.kotlin.coroutines.core)
            }
        }
    }
}
