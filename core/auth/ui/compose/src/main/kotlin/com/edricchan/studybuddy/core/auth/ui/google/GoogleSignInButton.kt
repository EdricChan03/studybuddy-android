package com.edricchan.studybuddy.core.auth.ui.google

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import com.edricchan.studybuddy.core.auth.common.R as CommonR

@Composable
fun GoogleSignInButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors,
    border: BorderStroke?,
    text: String = stringResource(CommonR.string.google_sign_in_btn_default_text),
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        border = border,
        onClick = onClick,
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
    ) {
        Image(
            modifier = Modifier.size(ButtonDefaults.IconSize),
            painter = painterResource(CommonR.drawable.ic_google_logo),
            contentDescription = null
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = text,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun GoogleSignInButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    theme: GoogleSignInButtonTheme = GoogleSignInButtonTheme.Auto,
    text: String = stringResource(CommonR.string.google_sign_in_btn_default_text),
    onClick: () -> Unit
) = GoogleSignInButton(
    modifier = modifier,
    enabled = enabled,
    colors = theme.getColors(),
    border = theme.getBorder(),
    text = text,
    onClick = onClick
)

@PreviewFontScale
@PreviewLightDark
@Composable
private fun GoogleSignInButtonPreview() {
    StudyBuddyTheme {
        GoogleSignInButton(onClick = {})
    }
}

@Preview
@Composable
private fun GoogleSignInButtonNeutralPreview() {
    StudyBuddyTheme {
        GoogleSignInButton(theme = GoogleSignInButtonTheme.Neutral, onClick = {})
    }
}
