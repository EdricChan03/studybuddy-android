@file:Suppress("TestFunctionName")

package com.edricchan.studybuddy.ui.widgets.compose.textfield

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

internal const val boxTag = "Tag:OutlinedTextFieldBox"
internal const val textTag = "Tag:Text"
internal const val leadingIconTag = "Tag:LeadingIcon"
internal const val trailingIconTag = "Tag:TrailingIcon"

internal val boxModifier = Modifier.testTag(boxTag)
internal val textModifier = Modifier.testTag(textTag)
internal val leadingIconModifier = Modifier.testTag(leadingIconTag)
internal val trailingIconModifier = Modifier.testTag(trailingIconTag)

private const val itemText = "Content of text field"

@Composable
internal fun OutlinedTextFieldBoxText() {
    Text(modifier = textModifier, text = itemText)
}

@Composable
internal fun OutlinedTextFieldBoxLeadingIcon() {
    Icon(
        modifier = leadingIconModifier,
        imageVector = Icons.Outlined.CheckCircle,
        contentDescription = null
    )
}

@Composable
internal fun OutlinedTextFieldBoxTrailingIcon() {
    Icon(
        modifier = trailingIconModifier,
        imageVector = Icons.Outlined.Favorite,
        contentDescription = null
    )
}
