package com.edricchan.studybuddy.core.auth.ui

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.edricchan.studybuddy.core.auth.common.R
import com.edricchan.studybuddy.exts.coil.imageRequest
import com.edricchan.studybuddy.utils.coil.compose.tintedPainter
import me.saket.telephoto.zoomable.coil3.ZoomableAsyncImage

/**
 * Displays a user's profile picture given the [photoUrl].
 * The image is [cropped][Modifier.clip] with the specified [shape].
 * @param modifier Modifier to be passed to [AsyncImage].
 * @param shape The [Shape] to [crop][Modifier.clip] the contents to.
 * @param displayName The user's display name.
 * @param photoUrl URL to the profile picture as a [String].
 * @param context Context to be used to create an [ImageRequest].
 * @param isZoomable Whether the image should be [zoomable][ZoomableAsyncImage].
 * @param imageRequestInit Additional configuration to be passed to the [ImageRequest.Builder].
 */
@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    displayName: String,
    photoUrl: String?,
    context: Context = LocalContext.current,
    isZoomable: Boolean = false,
    imageRequestInit: ImageRequest.Builder.() -> Unit = {}
) = ProfileImage(
    modifier = modifier,
    shape = shape,
    displayName = displayName,
    photoUri = photoUrl?.toUri(),
    context = context,
    isZoomable = isZoomable,
    imageRequestInit = imageRequestInit
)

/**
 * Displays a user's profile picture given the [photoUri].
 * The image is [cropped][Modifier.clip] with the specified [shape].
 * @param modifier Modifier to be passed to [AsyncImage].
 * @param shape The [Shape] to [crop][Modifier.clip] the contents to.
 * @param displayName The user's display name.
 * @param photoUri URL to the profile picture as a [Uri].
 * @param context Context to be used to create an [ImageRequest].
 * @param isZoomable Whether the image should be [zoomable][ZoomableAsyncImage].
 * @param imageRequestInit Additional configuration to be passed to the [ImageRequest.Builder].
 */
@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    displayName: String,
    photoUri: Uri?,
    context: Context = LocalContext.current,
    isZoomable: Boolean = false,
    imageRequestInit: ImageRequest.Builder.() -> Unit = {}
) {
    val model = context.imageRequest {
        data(photoUri)
        crossfade(true)
        imageRequestInit()
    }
    if (isZoomable) {
        ZoomableAsyncImage(
            model = model,
            contentDescription = stringResource(R.string.account_profile_content_desc, displayName),
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    } else {
        AsyncImage(
            modifier = modifier.clip(shape),
            model = model,
            placeholder = tintedPainter(rememberVectorPainter(Icons.Outlined.AccountCircle)),
            contentDescription = stringResource(R.string.account_profile_content_desc, displayName),
            contentScale = ContentScale.Crop,
        )
    }
}
