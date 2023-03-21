import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.github.benmanes.gradle.versions.updates.gradle.GradleReleaseChannel
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import tech.antibytes.gradle.dependency.helper.ensureKotlinVersion
import tech.antibytes.gradle.result.config.publishing.ProjectPublishingConfiguration

val ossrhUsername: String? by project
val ossrhPassword: String? by project

val signingKeyId: String? by project // must be the last 8 digits of the key
val signingKey: String? by project
val signingPassword: String? by project

description = "A Result monad for modelling success or failure operations."

plugins {
    base
    id("tech.antibytes.gradle.setup")
    alias(antibytesCatalog.plugins.gradle.antibytes.dependencyHelper)
    alias(antibytesCatalog.plugins.gradle.antibytes.publishing)

    alias(libs.plugins.versions)

    alias(libs.plugins.multiplatform) apply false
    alias(libs.plugins.benchmark) apply false
    alias(libs.plugins.dokka) apply false
}

val publishing = ProjectPublishingConfiguration(project)

antibytesPublishing {
    additionalPublishingTasks.set(publishing.additionalPublishingTasks)
    versioning.set(publishing.versioning)
    repositories.set(publishing.repositories)
}

tasks.withType<DependencyUpdatesTask> {
    gradleReleaseChannel = GradleReleaseChannel.CURRENT.id

    rejectVersionIf {
        listOf("alpha", "beta", "rc", "cr", "m", "eap", "pr", "dev").any {
            candidate.version.contains(it, ignoreCase = true)
        }
    }
}

allprojects {
    repositories {
        mavenCentral()
    }
    ensureKotlinVersion("1.7.10")
}

subprojects {
    plugins.withType<MavenPublishPlugin> {
        apply(plugin = "org.gradle.signing")

        plugins.withType<KotlinMultiplatformPluginWrapper> {
            apply(plugin = "org.jetbrains.dokka")

            val dokkaHtml by tasks.existing(DokkaTask::class)

            val javadocJar by tasks.registering(Jar::class) {
                group = LifecycleBasePlugin.BUILD_GROUP
                description = "Assembles a jar archive containing the Javadoc API documentation."
                archiveClassifier.set("javadoc")
                from(dokkaHtml)
            }

            tasks.withType<Jar> {
                from(rootDir.resolve("LICENSE")) {
                    into("META-INF")
                }
            }

            configure<KotlinMultiplatformExtension> {
                explicitApi()
            }
        }
    }
}
