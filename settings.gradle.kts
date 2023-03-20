import tech.antibytes.gradle.dependency.settings.localGithub

pluginManagement {
    repositories {
        val antibytesPlugins = "^tech\\.antibytes\\.[\\.a-z\\-]+"
        gradlePluginPortal()
        google()
        mavenCentral()
        maven {
            setUrl("https://raw.github.com/bitPogo/maven-snapshots/main/snapshots")
            content {
                includeGroupByRegex(antibytesPlugins)
            }
        }
        maven {
            setUrl("https://raw.github.com/bitPogo/maven-rolling-releases/main/rolling")
            content {
                includeGroupByRegex(antibytesPlugins)
            }
        }
    }
}

plugins {
    id("tech.antibytes.gradle.dependency.settings") version "58ccb15"
}

includeBuild("setup")

include(
    // "benchmarks",
    // "example",
    "kotlin-result",
    // "kotlin-result-coroutines"
)

buildCache {
    localGithub()
}

rootProject.name = "kotlin-result"
