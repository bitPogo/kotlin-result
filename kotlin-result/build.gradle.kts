import tech.antibytes.gradle.result.config.publishing.ProjectConfiguration
import tech.antibytes.gradle.configuration.apple.ensureAppleDeviceCompatibility
import tech.antibytes.gradle.configuration.sourcesets.nativeWithLegacy

description = "A Result monad for modelling success or failure operations."

plugins {
    `maven-publish`
    alias(antibytesCatalog.plugins.gradle.antibytes.kmpConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.publishing)
}

val publishingConfiguration = ProjectConfiguration(project)

antibytesPublishing {
    packaging.set(publishingConfiguration.publishing.packageConfiguration)
    repositories.set(publishingConfiguration.publishing.repositories)
    versioning.set(publishingConfiguration.publishing.versioning)
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
        browser()
    }

    jvm()

    nativeWithLegacy()
    ensureAppleDeviceCompatibility()

    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.contracts.ExperimentalContracts")
            }
        }

        val commonMain by getting {

        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(kotlin("test"))
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

publishing {
    publications.withType<MavenPublication> {
        pom {
            description.set(project.description)
        }
    }
}
