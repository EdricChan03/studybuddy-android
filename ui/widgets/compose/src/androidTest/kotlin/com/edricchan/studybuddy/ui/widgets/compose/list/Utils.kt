package com.edricchan.studybuddy.ui.widgets.compose.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

internal const val listItemTag = "Tag:ListItem"
internal const val textTag = "Tag:Text"
internal const val checkboxTag = "Tag:Checkbox"
internal const val radioButtonTag = "Tag:Radio"
internal const val trailingContentTag = "Tag:TrailingContent"

internal val listItemModifier = Modifier.testTag(listItemTag)
internal val checkboxModifier = Modifier.testTag(checkboxTag)
internal val radioButtonModifier = Modifier.testTag(radioButtonTag)
internal val textModifier = Modifier.testTag(textTag)
internal val trailingContentModifier = Modifier.testTag(trailingContentTag)

private const val itemText = "Item 1"

@Suppress("TestFunctionName")
@Composable
internal fun ListItemText() = Text(modifier = textModifier, text = itemText)

@Suppress("TestFunctionName")
@Composable
internal fun ListItemTrailingContent() = Icon(
    modifier = trailingContentModifier,
    imageVector = Icons.Outlined.Favorite,
    contentDescription = null
)
