package com.edricchan.studybuddy.features.auth.recovery.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.features.auth.R
import com.edricchan.studybuddy.features.auth.common.ui.AuthCardHeader
import com.edricchan.studybuddy.features.auth.common.ui.fields.EmailTextField
import com.edricchan.studybuddy.ui.theming.compose.theme.preview.StudyBuddyThemeWrapperProvider
import com.edricchan.studybuddy.utils.compose.foundation.layout.CenteredBox

@Composable
fun RecoveryContent(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues.Zero,
    emailState: TextFieldState,
    onRequestResetClick: () -> Unit
) {
    val submitEnabled = emailState.text.isNotBlank()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AuthCardHeader(
            headerText = stringResource(R.string.forgot_password_title)
        )

        Text(
            text = stringResource(R.string.forgot_password_msg),
            textAlign = TextAlign.Center
        )

        EmailTextField(
            state = emailState
        )

        val size = ButtonDefaults.MediumContainerHeight
        Button(
            modifier = Modifier.heightIn(size),
            contentPadding = ButtonDefaults.contentPaddingFor(
                buttonHeight = size
            ),
            onClick = onRequestResetClick,
            shapes = ButtonDefaults.shapes(),
            enabled = submitEnabled
        ) {
            Text(
                text = stringResource(R.string.btn_reset_password),
                style = ButtonDefaults.textStyleFor(size)
            )
        }
    }
}

@Composable
fun RecoveryScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    emailState: TextFieldState,
    onRequestResetClick: () -> Unit
) {
    CenteredBox(
        modifier = modifier
    ) {
        RecoveryContent(
            contentPadding = contentPadding,
            emailState = emailState,
            onRequestResetClick = onRequestResetClick
        )
    }
}

@Preview(showSystemUi = true)
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun RecoveryScreenPreview() {
    val emailState = rememberTextFieldState()

    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        RecoveryScreen(
            emailState = emailState,
            onRequestResetClick = {}
        )
    }
}
