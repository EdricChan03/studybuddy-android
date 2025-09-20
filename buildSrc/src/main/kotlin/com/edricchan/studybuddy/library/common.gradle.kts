/** Convention script for all modules. */
package com.edricchan.studybuddy.library

private val commonMarkVersion =
    versionCatalogs.named("libs")
        .findVersion("commonmark")
        .map { it.requiredVersion }.get()

// Fix for https://github.com/noties/Markwon/issues/341
configurations.asMap.values
    .filter { it.dependencies.any { dependency -> dependency.group == "com.atlassian.commonmark" } }
    .forEach {
        it.resolutionStrategy.dependencySubstitution.all {
            requested.let {
                if (it is ModuleComponentSelector && it.group == "com.atlassian.commonmark") {
                    useTarget(
                        "org.commonmark:${it.module}:$commonMarkVersion",
                        "CommonMark has moved its groupId from com.atlassian to org.commonmark"
                    )
                }
            }
        }
    }
