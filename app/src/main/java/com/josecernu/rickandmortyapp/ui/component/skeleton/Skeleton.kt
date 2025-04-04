package com.josecernu.rickandmortyapp.ui.component.skeleton

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

fun Modifier.shimmerEffect(): Modifier =
    composed {
        var size = remember { IntSize.Zero }

        val transition = rememberInfiniteTransition(label = "")
        val shimmerTranslateAnim =
            transition.animateFloat(
                initialValue = 0f,
                targetValue = 1000f,
                animationSpec =
                    infiniteRepeatable(
                        animation = tween(durationMillis = 1000, easing = LinearEasing),
                    ),
                label = "",
            )

        val brush =
            Brush.linearGradient(
                colors =
                    listOf(
                        Color.LightGray.copy(alpha = 0.6f),
                        Color.LightGray.copy(alpha = 0.2f),
                        Color.LightGray.copy(alpha = 0.6f),
                    ),
                start = Offset.Zero,
                end = Offset(x = shimmerTranslateAnim.value, y = shimmerTranslateAnim.value),
            )

        this
            .onGloballyPositioned { size = it.size }
            .background(brush = brush)
    }
