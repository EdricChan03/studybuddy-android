package com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.exts.androidx.compose.runtime.letComposable
import com.edricchan.studybuddy.ui.widgets.compose.list.TrailingCheckboxListItem
import com.edricchan.studybuddy.ui.widgets.compose.list.TrailingRadioButtonListItem
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.CheckableBehavior
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetGroup
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.select.model.OptionBottomSheetItem

// TODO: Move to compose module

@Composable
private fun <Id> CheckboxBottomSheetItem(
    modifier: Modifier = Modifier,
    item: OptionBottomSheetItem<Id>,
    isSelected: Boolean,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) = TrailingCheckboxListItem(
    modifier = modifier,
    text = { Text(text = item.title) },
    leadingContent = item.icon?.letComposable {
        Icon(
            painterResource(it),
            contentDescription = null
        )
    },
    checked = isSelected,
    onCheckedChange = onCheckedChange,
    enabled = enabled
)

@Composable
private fun <Id> RadioBottomSheetItem(
    modifier: Modifier = Modifier,
    item: OptionBottomSheetItem<Id>,
    isSelected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) = TrailingRadioButtonListItem(
    modifier = modifier,
    text = { Text(text = item.title) },
    leadingContent = item.icon?.letComposable {
        Icon(
            painterResource(it),
            contentDescription = null
        )
    },
    selected = isSelected,
    onSelected = onClick,
    enabled = enabled
)

@Composable
private fun SelectBottomSheetActions(
    modifier: Modifier = Modifier,
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
    confirmEnabled: Boolean
) = Row(
    modifier = modifier.padding(8.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
    verticalAlignment = Alignment.CenterVertically
) {
    OutlinedButton(onClick = onCancelClick) {
        Text(text = stringResource(R.string.cancel))
    }
    FilledTonalButton(
        onClick = onConfirmClick,
        enabled = confirmEnabled
    ) {
        Text(text = stringResource(R.string.ok))
    }
}

@Composable
private fun SelectBottomSheetHeaderText(
    modifier: Modifier = Modifier,
    text: String
) = Text(
    modifier = modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
    text = text,
    style = MaterialTheme.typography.titleMedium
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <Id : Any> SelectBottomSheetContent(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    headerTitle: String?,
    hideDragHandle: Boolean = false,
    itemsList: OptionBottomSheetGroup<Id>,
    onItemClick: (item: OptionBottomSheetItem<Id>, isSelected: Boolean) -> Unit,
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
    confirmEnabled: Boolean
) = Column(
    modifier = modifier
) {
    if (!hideDragHandle) {
        BottomSheetDefaults.DragHandle(modifier = Modifier.align(Alignment.CenterHorizontally))
    }

    headerTitle?.let {
        SelectBottomSheetHeaderText(
            text = it
        )
        HorizontalDivider()
    }

    LazyColumn(
        modifier = Modifier.weight(1f, fill = false),
        contentPadding = contentPadding
    ) {
        items(itemsList.items, key = { it.id }) { item ->
            when (itemsList.checkableBehavior) {
                CheckableBehavior.Single -> RadioBottomSheetItem(
                    item = item,
                    isSelected = itemsList.isChecked(item),
                    onClick = {
                        onItemClick(item, true)
                    },
                    enabled = item.enabled
                )

                CheckableBehavior.Multi -> CheckboxBottomSheetItem(
                    item = item,
                    isSelected = itemsList.isChecked(item),
                    onCheckedChange = {
                        onItemClick(item, it)
                    },
                    enabled = item.enabled
                )
            }
        }
    }

    HorizontalDivider()
    SelectBottomSheetActions(
        modifier = Modifier.fillMaxWidth(),
        onCancelClick = onCancelClick, onConfirmClick = onConfirmClick,
        confirmEnabled = confirmEnabled
    )
}
