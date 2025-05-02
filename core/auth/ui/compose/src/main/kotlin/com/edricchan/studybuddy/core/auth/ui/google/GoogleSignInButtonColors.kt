package com.edricchan.studybuddy.core.auth.ui.google

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.unit.dp

// Colours from https://developers.google.com/identity/branding-guidelines#custom-button
@Stable
object GoogleSignInButtonDefaults {
    fun colors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        disabledContainerColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): ButtonColors = ButtonColors(
        containerColor = containerColor.takeOrElse { GoogleSignInButtonTokens.NeutralContainerColor },
        contentColor = contentColor.takeOrElse { GoogleSignInButtonTokens.NeutralContentColor },
        disabledContainerColor = disabledContainerColor.takeOrElse {
            containerColor.copy(
                GoogleSignInButtonTokens.DisabledAlpha
            )
        },
        disabledContentColor = disabledContentColor.takeOrElse {
            contentColor.copy(
                GoogleSignInButtonTokens.DisabledAlpha
            )
        }
    )

    /** Default light colours for the [GoogleSignInButton]. */
    val lightColors: ButtonColors = colors(
        containerColor = GoogleSignInButtonTokens.LightContainerColor,
        contentColor = GoogleSignInButtonTokens.LightContentColor,
        disabledContainerColor = GoogleSignInButtonTokens.LightContainerColor.copy(alpha = GoogleSignInButtonTokens.DisabledAlpha),
        disabledContentColor = GoogleSignInButtonTokens.LightContentColor.copy(alpha = GoogleSignInButtonTokens.DisabledAlpha),
    )

    /** Default light [BorderStroke] for the [GoogleSignInButton]. */
    val lightBorderStroke: BorderStroke = BorderStroke(
        width = 1.dp,
        color = GoogleSignInButtonTokens.LightStrokeColor
    )

    /** Default dark colours for the [GoogleSignInButton]. */
    val darkColors: ButtonColors = colors(
        containerColor = GoogleSignInButtonTokens.DarkContainerColor,
        contentColor = GoogleSignInButtonTokens.DarkContentColor,
        disabledContainerColor = GoogleSignInButtonTokens.DarkContainerColor.copy(alpha = GoogleSignInButtonTokens.DisabledAlpha),
        disabledContentColor = GoogleSignInButtonTokens.DarkContentColor.copy(alpha = GoogleSignInButtonTokens.DisabledAlpha),
    )

    /** Default dark [BorderStroke] for the [GoogleSignInButton]. */
    val darkBorderStroke: BorderStroke = BorderStroke(
        width = 1.dp,
        color = GoogleSignInButtonTokens.DarkStrokeColor
    )

    /** Default neutral colours for the [GoogleSignInButton]. */
    val neutralColors: ButtonColors = colors(
        containerColor = GoogleSignInButtonTokens.NeutralContainerColor,
        contentColor = GoogleSignInButtonTokens.NeutralContentColor,
        disabledContainerColor = GoogleSignInButtonTokens.NeutralContainerColor.copy(alpha = GoogleSignInButtonTokens.DisabledAlpha),
        disabledContentColor = GoogleSignInButtonTokens.NeutralContentColor.copy(alpha = GoogleSignInButtonTokens.DisabledAlpha),
    )
}
