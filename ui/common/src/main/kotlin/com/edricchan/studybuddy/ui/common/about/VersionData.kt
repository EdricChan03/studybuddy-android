package com.edricchan.studybuddy.ui.common.about

import android.content.Context
import com.edricchan.studybuddy.exts.android.metadata.versionCode
import com.edricchan.studybuddy.exts.android.metadata.versionName
import java.time.Instant

fun Context.getAppVersionData(
    packageName: String = this.packageName,
    buildType: String,
    versionName: String? = this.versionName,
    versionCode: Long = this.versionCode,
    buildTime: Instant?,
    gitCommitSha: String?
): String = buildString {
    appendLine("Package name: $packageName ($buildType)")
    versionName?.takeUnless(String::isEmpty)
        ?.let { appendLine("Version name: $it") }
    appendLine("Version code: $versionCode")
    buildTime?.let { appendLine("Build time: $it") }
    gitCommitSha?.takeUnless(String::isEmpty)
        ?.let { appendLine("Git commit SHA: $it") }
}

fun Context.getMarkdownAppVersionData(
    packageName: String = this.packageName,
    buildType: String,
    versionName: String? = this.versionName,
    versionCode: Long = this.versionCode,
    buildTime: Instant?,
    gitCommitSha: String?
): String = buildString {
    appendLine("* **Package name:** `$packageName` (`$buildType`)")
    versionName?.takeUnless(String::isEmpty)
        ?.let { appendLine("* **Version name:** $it") }
    appendLine("* **Version code:** $versionCode")
    buildTime?.let { appendLine("* **Build time:** `$it`") }
    gitCommitSha?.takeUnless(String::isEmpty)
        ?.let { appendLine("* **Git commit SHA:** `$it`") }
}
