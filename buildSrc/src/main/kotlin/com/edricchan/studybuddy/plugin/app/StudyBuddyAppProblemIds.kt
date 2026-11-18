package com.edricchan.studybuddy.plugin.app

import org.gradle.api.problems.ProblemGroups
import org.gradle.api.problems.ProblemId

class StudyBuddyAppProblemIds(private val problemGroups: ProblemGroups) {
    val missingSecretsConfig: ProblemId = problemGroups.verification.security.problemId(
        "Secrets configuration file is missing or not readable"
    )
}
