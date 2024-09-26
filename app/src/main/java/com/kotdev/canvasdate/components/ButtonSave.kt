package com.kotdev.canvasdate.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotdev.canvasdate.ui.theme.Purple40
import com.kotdev.canvasdate.ui.theme.Purple80

@Composable
fun measureTextWidth(text: String, style: TextStyle): Float {
    val textMeasurer = rememberTextMeasurer()
    val widthInPixels = textMeasurer.measure(text, style).size.width
    return with(LocalDensity.current) { widthInPixels.toDp().toPx() }
}

@Composable
private fun ButtonSaveBackground(update: () -> Unit, buttonState: () -> ButtonState) {
    val length = measureTextWidth("Refresh", TextStyle(fontSize = 20.sp))
    val colorAnim by animateColorAsState(targetValue = if (buttonState() == ButtonState.TEXT) Purple40.copy(alpha = 0.1f)
    else Purple80,
        animationSpec = tween(
            durationMillis = 500
        ), label = ""
    )
    val width =
        remember { Animatable(initialValue = if (buttonState() == ButtonState.TEXT) length else 50f) }
    LaunchedEffect(buttonState()) {
        width.animateTo(
            targetValue = if (buttonState() == ButtonState.TEXT) length else 50f,
            animationSpec = spring(
                dampingRatio = 0.8f,
                stiffness = Spring.StiffnessLow
            )
        )
    }
    Box(
        modifier = Modifier
            .width(width.value.dp)
            .height(40.dp)
            .drawBehind {
                drawRoundRect(
                    color = colorAnim,
                    cornerRadius = CornerRadius(20.dp.toPx(), 20.dp.toPx())
                )
            }
            .padding(10.dp)
            .clickableNoRipple(
                onClick = update
            )
    )
}

@Composable
internal fun ButtonSave() {
    var buttonState by remember { mutableStateOf(ButtonState.TEXT) }
    Box(
        contentAlignment = Alignment.Center,
    ) {
        ButtonSaveBackground(
            update = {
                buttonState = buttonState.getOppositeState()
            },
            buttonState = { buttonState }
        )
        AnimatedVisibility(
            visible = buttonState == ButtonState.TEXT,
            enter = scaleIn(
                animationSpec = tween(
                    durationMillis = 500
                )
            ),
            exit = scaleOut(
                animationSpec = tween(
                    durationMillis = 500
                )
            )
        ) {
            Text(
                text = "Refresh",
                color = Purple40,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp)
            )
        }
        AnimatedVisibility(
            visible = buttonState == ButtonState.ICON,
            enter = scaleIn(
                animationSpec = tween(
                    durationMillis = 500
                )
            ),
            exit = scaleOut(
                animationSpec = tween(
                    durationMillis = 500
                )
            )
        ) {
            val rotate = rotateComposable()
            Icon(
                modifier = Modifier
                    .width(40.dp)
                    .graphicsLayer {
                        rotationZ = if (buttonState == ButtonState.TEXT) 0f else rotate
                    },
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh",
                tint = Purple40,
            )
        }
    }
}

@Composable
fun rotateComposable(): Float {
    val transition = rememberInfiniteTransition(label = "")
    val twinCircleAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    return twinCircleAnimation
}

@Immutable
enum class ButtonState {
    TEXT, ICON
}

fun ButtonState.getOppositeState() = when (this) {
    ButtonState.TEXT -> ButtonState.ICON
    ButtonState.ICON -> ButtonState.TEXT
}

private inline fun Modifier.clickableNoRipple(
    crossinline onClick: () -> Unit
): Modifier = composed {
    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
        onClick = {
            onClick()
        }
    )
}