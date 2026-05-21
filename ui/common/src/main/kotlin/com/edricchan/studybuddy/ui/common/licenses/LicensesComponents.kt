package com.edricchan.studybuddy.ui.common.licenses

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Badge
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLocaleList
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.Check
import com.edricchan.studybuddy.core.resources.icons.outlined.Code
import com.edricchan.studybuddy.core.resources.icons.outlined.Link
import com.edricchan.studybuddy.ui.common.R
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import com.edricchan.studybuddy.ui.widgets.compose.IconButtonWithTooltip
import com.mikepenz.aboutlibraries.entity.Developer
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.entity.License
import com.mikepenz.aboutlibraries.entity.Scm
import com.mikepenz.aboutlibraries.ui.compose.util.author
import java.text.Collator
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun LicenseChipsRow(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    licenseCounts: Map<License, Int>,
    selectedLicenses: Set<License> = licenseCounts.keys,
    onSelectLicenses: (Set<License>) -> Unit
) {
    val collator = Collator.getInstance(LocalLocaleList.current[0].platformLocale)
    val licenses = licenseCounts.entries.sortedWith(compareBy(collator) { it.key.name })

    fun onToggleLicense(license: License) {
        if (license in selectedLicenses) onSelectLicenses(selectedLicenses - license)
        else onSelectLicenses(selectedLicenses + license)
    }

    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            items = licenses,
            key = { it.key.hash }
        ) { (license, count) ->
            val isSelected = license in selectedLicenses
            FilterChip(
                modifier = Modifier.animateContentSize(),
                selected = isSelected,
                onClick = { onToggleLicense(license) },
                leadingIcon = {
                    AnimatedVisibility(
                        visible = isSelected,
                        enter = fadeIn(
                            MaterialTheme.motionScheme.fastEffectsSpec()
                        ) + scaleIn(
                            MaterialTheme.motionScheme.fastSpatialSpec()
                        ),
                        exit = fadeOut(
                            MaterialTheme.motionScheme.fastEffectsSpec()
                        ) + scaleOut(
                            MaterialTheme.motionScheme.fastSpatialSpec()
                        )
                    ) {
                        Icon(AppIcons.Outlined.Check, contentDescription = null)
                    }
                },
                label = {
                    Text(text = license.name)
                },
                trailingIcon = {
                    Badge(
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Text(text = count.toString())
                    }
                },
                shapes = FilterChipDefaults.shapes()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LibraryItem(
    modifier: Modifier = Modifier,
    lib: Library,
    onClick: () -> Unit,
    onLinkClick: (String) -> Unit = LocalUriHandler.current::openUri
) {
    val labelViewLicense = stringResource(R.string.licenses_action_view_license_text_url)
    val labelViewScm = stringResource(R.string.licenses_action_view_scm_url)
    val labelViewWebsite = stringResource(R.string.licenses_action_view_website_url)

    Surface(
        modifier = modifier.semantics {
            customActions = listOfNotNull(
                CustomAccessibilityAction(
                    label = labelViewLicense,
                    action = {
                        onClick()
                        true
                    }
                ),
                lib.scm?.url?.takeIf { it.isNotBlank() }?.let {
                    CustomAccessibilityAction(
                        label = labelViewScm,
                        action = {
                            onLinkClick(it)
                            true
                        }
                    )
                },
                lib.website?.takeIf { it.isNotBlank() }?.let {
                    CustomAccessibilityAction(
                        label = labelViewWebsite,
                        action = {
                            onLinkClick(it)
                            true
                        }
                    )
                }
            )
        },
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = lib.name,
                    style = MaterialTheme.typography.titleLarge
                )
                lib.artifactVersion?.let {
                    Surface(
                        shape = CircleShape
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                            text = it,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
            lib.author.takeIf { it.isNotBlank() }?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleSmall
                )
            }
            lib.description?.takeIf { it.isNotBlank() }?.let { Text(text = it) }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                lib.licenses.forEach {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = AssistChipDefaults.shape
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                            text = it.name,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            if (lib.scm?.url?.isNotBlank() == true || lib.website?.isNotBlank() == true) {
                ButtonGroup(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp)
                        .clearAndSetSemantics {},
                    overflowIndicator = {
                        ButtonGroupDefaults.OverflowIndicator(
                            menuState = it
                        )
                    }
                ) {
                    lib.scm?.url?.takeIf { it.isNotBlank() }?.let {
                        customItem(
                            buttonGroupContent = {
                                val interactionSource = remember { MutableInteractionSource() }
                                OutlinedButton(
                                    modifier = Modifier
                                        .weight(1f)
                                        .animateWidth(interactionSource),
                                    shapes = ButtonDefaults.shapes(),
                                    onClick = { onLinkClick(it) },
                                    interactionSource = interactionSource
                                ) {
                                    Icon(AppIcons.Outlined.Code, contentDescription = null)
                                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                                    Text(
                                        text = labelViewScm
                                    )
                                }
                            },
                            menuContent = { state ->
                                DropdownMenuItem(
                                    leadingIcon = {
                                        Icon(AppIcons.Outlined.Code, contentDescription = null)
                                    },
                                    text = {
                                        Text(
                                            text = labelViewScm
                                        )
                                    },
                                    onClick = {
                                        onLinkClick(it)
                                        state.dismiss()
                                    },
                                )
                            }
                        )
                    }
                    lib.website?.takeIf { it.isNotBlank() }?.let {
                        customItem(
                            buttonGroupContent = {
                                val interactionSource = remember { MutableInteractionSource() }
                                OutlinedButton(
                                    modifier = Modifier
                                        .weight(1f)
                                        .animateWidth(interactionSource),
                                    shapes = ButtonDefaults.shapes(),
                                    onClick = { onLinkClick(it) },
                                    interactionSource = interactionSource
                                ) {
                                    Icon(AppIcons.Outlined.Link, contentDescription = null)
                                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                                    Text(
                                        text = labelViewWebsite
                                    )
                                }
                            },
                            menuContent = { state ->
                                DropdownMenuItem(
                                    leadingIcon = {
                                        Icon(AppIcons.Outlined.Link, contentDescription = null)
                                    },
                                    text = {
                                        Text(
                                            text = labelViewWebsite
                                        )
                                    },
                                    onClick = {
                                        onLinkClick(it)
                                        state.dismiss()
                                    },
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Preview
@Composable
private fun LibraryItemPreview(
    @PreviewParameter(LoremIpsum::class)
    ipsum: String
) {
    StudyBuddyTheme {
        LibraryItem(
            lib = Library(
                uniqueId = Uuid.random().toString(),
                artifactVersion = "1.0.0",
                name = "Example Library with a lot of text that should cover more than the contents",
                description = ipsum.take(1000),
                website = "https://example.com",
                developers = listOf(
                    Developer(
                        name = "Dev A",
                        organisationUrl = null
                    ),
                    Developer(
                        name = "Dev B",
                        organisationUrl = null
                    ),
                    Developer(
                        name = "Dev C",
                        organisationUrl = null
                    )
                ),
                licenses = setOf(
                    License(
                        name = "MIT License",
                        url = null,
                        hash = "abc"
                    ),
                    License(
                        name = "GPL 3.0",
                        url = null,
                        hash = "def"
                    )
                ),
                organization = null,
                scm = Scm(
                    connection = "https://github.com",
                    developerConnection = "https://github.com",
                    url = "https://github.com",
                )
            ),
            onClick = {}
        )
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun LicensesSearchBar(
    modifier: Modifier = Modifier,
    state: SearchBarState = rememberSearchBarState(),
    textFieldState: TextFieldState
) {
    val isNotEmpty by remember { derivedStateOf { textFieldState.text.isNotEmpty() } }

    SearchBar(
        modifier = modifier,
        state = state,
        inputField = {
            SearchBarDefaults.InputField(
                textFieldState = textFieldState,
                searchBarState = state,
                leadingIcon = {
                    Icon(Icons.Outlined.Search, contentDescription = null)
                },
                placeholder = {
                    Text(text = stringResource(R.string.licenses_search_bar_placeholder_text))
                },
                trailingIcon = {
                    AnimatedVisibility(
                        visible = isNotEmpty,
                        label = "Licenses search bar clear button visibility",
                        enter = fadeIn(
                            animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()
                        ) + scaleIn(
                            animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
                        ),
                        exit = fadeOut(
                            animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()
                        ) + scaleOut(
                            animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
                        )
                    ) {
                        IconButtonWithTooltip(
                            tooltip = {
                                PlainTooltip {
                                    Text(text = stringResource(R.string.licenses_search_bar_action_clear_input))
                                }
                            },
                            icon = {
                                Icon(
                                    Icons.Outlined.Clear,
                                    contentDescription = stringResource(R.string.licenses_search_bar_action_clear_input)
                                )
                            },
                            onClick = {
                                textFieldState.clearText()
                            }
                        )
                    }
                },
                onSearch = {
                    // No-op
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicensesFilterOptions(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceContainer,
    contentColor: Color = contentColorFor(color),
    searchTextFieldState: TextFieldState,
    licenseCounts: Map<License, Int>,
    selectedLicenses: Set<License>,
    onSelectLicenses: (Set<License>) -> Unit
) {
    Surface(
        modifier = modifier,
        color = color,
        contentColor = contentColor
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            LicensesSearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp),
                textFieldState = searchTextFieldState
            )
            LicenseChipsRow(
                contentPadding = PaddingValues(8.dp),
                licenseCounts = licenseCounts,
                selectedLicenses = selectedLicenses,
                onSelectLicenses = onSelectLicenses
            )
        }
    }
}

@Composable
internal fun FilteredLicensesEmptyState(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerHighest
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                imageVector = Icons.Outlined.Info, contentDescription = null
            )
            Text(
                text = stringResource(R.string.licenses_search_filtered_results_empty_text_title),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(R.string.licenses_search_filtered_results_empty_text_suggestion)
            )
        }
    }
}
