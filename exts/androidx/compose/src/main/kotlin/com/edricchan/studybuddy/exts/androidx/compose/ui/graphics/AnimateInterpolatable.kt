package com.edricchan.studybuddy.exts.androidx.compose.ui.graphics

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Interpolatable
import androidx.compose.ui.unit.Dp

// Implementation based on https://www.sinasamaki.com/animating-fonts-in-jetpack-compose/
/**
 * Fire-and-forget animation function for [Interpolatable]. This Composable function is overloaded for
 * different parameter types such as [Dp], [Float], [Int], [Size], [Offset], etc. When the provided
 * [targetValue] is changed, the animation will run automatically. If there is already an animation
 * in-flight when [targetValue] changes, the on-going animation will adjust course to animate
 * towards the new target value.
 *
 * [animateInterpolatableAsState] returns a [State] object. The value of the state object will continuously
 * be updated by the animation until the animation finishes.
 *
 * Note, [animateInterpolatableAsState] cannot be canceled/stopped without removing this composable function
 * from the tree. See [Animatable][androidx.compose.animation.Animatable] for cancelable animations.
 *
 * @param targetValue Target value of the animation
 * @param animationSpec The animation that will be used to change the value through time, [spring]
 *   by default
 * @param label An optional label to differentiate from other animations in Android Studio.
 * @param finishedListener An optional listener to get notified when the animation is finished.
 */
@Composable
fun <I : Interpolatable> animateInterpolatableAsState(
    targetValue: I,
    animationSpec: AnimationSpec<Float> = spring(),
    label: String = "InterpolatableAnimation",
    finishedListener: ((I) -> Unit)? = null
): State<I> {
    val animation = remember {
        Animatable(
            initialValue = 0f,
            typeConverter = Float.VectorConverter,
            label = label
        )
    }
    var previous by remember { mutableStateOf(targetValue) }
    var next by remember { mutableStateOf(targetValue) }

    @Suppress("UNCHECKED_CAST")
    val interpolatableState: State<I> = remember(animation.value) {
        derivedStateOf {
            previous.lerp(other = next, t = animation.value) as I
        }
    }

    LaunchedEffect(targetValue, animationSpec) {
        previous = interpolatableState.value
        next = targetValue
        animation.snapTo(0f)
        animation.animateTo(1f, animationSpec)
        finishedListener?.invoke(interpolatableState.value)
    }

    return interpolatableState
}
