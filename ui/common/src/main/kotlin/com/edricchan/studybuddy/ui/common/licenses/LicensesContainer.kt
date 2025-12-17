package com.edricchan.studybuddy.ui.common.licenses

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.Code
import com.edricchan.studybuddy.core.resources.icons.outlined.Link
import com.edricchan.studybuddy.exts.androidx.compose.plus
import com.edricchan.studybuddy.ui.common.R
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import com.edricchan.studybuddy.ui.widgets.compose.IconButtonWithTooltip
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.entity.Developer
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.entity.License
import com.mikepenz.aboutlibraries.entity.Scm
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import com.mikepenz.aboutlibraries.ui.compose.m3.LicenseDialog
import com.mikepenz.aboutlibraries.ui.compose.m3.LicenseDialogBody
import com.mikepenz.aboutlibraries.ui.compose.m3.libraryColors
import com.mikepenz.aboutlibraries.ui.compose.util.author
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlin.time.Duration.Companion.milliseconds
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LibraryItem(
    modifier: Modifier = Modifier,
    lib: Library,
    onClick: () -> Unit,
    onLinkClick: (String) -> Unit = LocalUriHandler.current::openUri
) {
    Surface(
        modifier = modifier,
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
                Row(
                    modifier = Modifier.padding(top = 14.dp),
                    horizontalArrangement = ButtonGroupDefaults.HorizontalArrangement
                ) {
                    lib.scm?.url?.takeIf { it.isNotBlank() }?.let {
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            shapes = ButtonDefaults.shapes(),
                            onClick = { onLinkClick(it) }
                        ) {
                            Icon(AppIcons.Outlined.Code, contentDescription = null)
                            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                            Text(
                                text = stringResource(R.string.licenses_action_view_scm_url)
                            )
                        }
                    }
                    lib.website?.takeIf { it.isNotBlank() }?.let {
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            shapes = ButtonDefaults.shapes(),
                            onClick = { onLinkClick(it) }
                        ) {
                            Icon(AppIcons.Outlined.Link, contentDescription = null)
                            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                            Text(
                                text = stringResource(R.string.licenses_action_view_website_url)
                            )
                        }
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
                developers = persistentListOf(
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
                licenses = persistentSetOf(
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
        shadowElevation = 1.dp,
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

private fun Library.matchesQuery(query: String): Boolean {
    return name.contains(query, ignoreCase = true) ||
        (description?.contains(query, ignoreCase = true) == true) ||
        licenses.any { name.contains(query, ignoreCase = true) } ||
        developers.any { name.contains(query, ignoreCase = true) } ||
        (organization?.name?.contains(query, ignoreCase = true) == true)
}

@Composable
private fun FilteredLicensesEmptyState(modifier: Modifier = Modifier) {
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

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class, ExperimentalCoroutinesApi::class)
@Composable
fun LicensesContainer(
    modifier: Modifier = Modifier,
    libs: Libs,
    lazyListState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = WindowInsets.navigationBars.only(WindowInsetsSides.Bottom)
        .asPaddingValues() + PaddingValues(16.dp),
    showSearchBar: Boolean = true
) {
    var openedLib by remember { mutableStateOf<Library?>(null) }

    val searchBarTextFieldState = rememberTextFieldState()
    val filteredLibs by remember {
        snapshotFlow { searchBarTextFieldState.text }
            .debounce(100.milliseconds)
            .mapLatest { query ->
                libs.libraries.filter { it.matchesQuery(query = query.toString()) }
            }
    }.collectAsStateWithLifecycle(initialValue = libs.libraries)

    val padding = if (filteredLibs.isEmpty()) PaddingValues(16.dp)
    else contentPadding

    LazyColumn(
        modifier = modifier,
        contentPadding = padding,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = lazyListState
    ) {
        if (showSearchBar) {
            stickyHeader {
                LicensesSearchBar(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(top = 8.dp),
                    textFieldState = searchBarTextFieldState
                )
            }
        }
        items(items = filteredLibs, key = { it.uniqueId }) {
            LibraryItem(
                modifier = Modifier.animateItem(),
                lib = it,
                onClick = {
                    openedLib = it
                }
            )
        }
        if (filteredLibs.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillParentMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    FilteredLicensesEmptyState()
                }
            }
        }
    }

    openedLib?.let {
        LicenseDialog(
            library = it,
            body = { lib, modifier ->
                LicenseDialogBody(
                    library = lib,
                    modifier = modifier,
                    colors = LibraryDefaults.libraryColors()
                )
            },
            onDismiss = {
                openedLib = null
            }
        )
    }
}
