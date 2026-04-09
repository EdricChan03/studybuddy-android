package com.edricchan.studybuddy.plugin.app

import org.gradle.api.problems.ProblemGroup
import org.gradle.api.problems.ProblemId

object StudyBuddyAppProblemIds {
    val Group: ProblemGroup = ProblemGroup.create(
        "com.edricchan.studybuddy.application-problems",
        "Configuration problems for StudyBuddy"
    )

    const val missingSecretsConfigId = "missing-secrets-config-file"
    val MissingSecretsConfig: ProblemId = ProblemId.create(
        missingSecretsConfigId,
        "Secrets configuration file is missing or not readable",
        Group
    )
}
