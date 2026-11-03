package com.edricchan.studybuddy.features.help.constants

import androidx.core.net.toUri
import com.edricchan.studybuddy.core.resources.metadata.StudyBuddyMetadata

val urlSrcCode = StudyBuddyMetadata.GitHubRepoUrl
val uriSrcCode = StudyBuddyMetadata.GitHubRepoUri

const val urlSendFeedback = "https://forms.gle/S6SY7aAgTyQUuKRw6"
val uriSendFeedback = urlSendFeedback.toUri()

const val urlHelpFeatured =
    "https://edricchan03.github.io/res/studybuddy/help-featured-articles.json"
val uriHelpFeatured = urlHelpFeatured.toUri()
