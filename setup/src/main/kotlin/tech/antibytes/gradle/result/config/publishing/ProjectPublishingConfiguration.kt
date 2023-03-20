/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.result.config.publishing

import org.gradle.api.Project
import tech.antibytes.gradle.configuration.BasePublishingConfiguration

open class ProjectPublishingConfiguration(
    project: Project,
) : BasePublishingConfiguration(project, "kotlin-result") {
    val description = "A Result monad for modelling success or failure operations."
    val url = "https://$gitHubRepositoryPath"
    val year = 2023
}
