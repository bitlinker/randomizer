package dev.kissed.randomizer.common.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.graphics.withSaveLayer
import kotlin.random.Random
import kotlin.random.nextInt

@Composable
internal fun Modifier.glitchEffect(
    glitchColors: List<Color>,
    slices: Int = 25,
    horizontalDrift: Float = 30F,
    horizontalSliceDrift: Int = 15,
    isEnabled: Boolean = true,
): Modifier {

    val graphicsLayer = rememberGraphicsLayer()

    val infiniteTransition = rememberInfiniteTransition(label = "glitch_transition")
    val transitionOffset by infiniteTransition.animateFloat(
        initialValue = -horizontalDrift,
        targetValue = horizontalDrift,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glitch_value"
    )

    val intensity by animateFloatAsState(
        targetValue = if (isEnabled) 1F else 0F,
        label = "glitch_intensity"
    )

    return drawWithContent {
        if (intensity == 0F) {
            drawContent()
            return@drawWithContent
        }
        graphicsLayer.record { this@drawWithContent.drawContent() }

        for (i in 0 until slices) {
            translate(
                left = if (Random.nextFloat() * 0.5F < intensity)
                    Random.nextInt(-horizontalSliceDrift..horizontalSliceDrift)
                        .toFloat() * intensity + transitionOffset
                else
                    0f
            ) {
                scale(
                    scaleY = 1f,
                    scaleX = if (Random.nextFloat() < intensity)
                        1f + (1f * Random.nextFloat() * intensity)
                    else
                        1f
                ) {
                    clipRect(
                        top = (i / slices.toFloat()) * size.height,
                        bottom = (((i + 1) / slices.toFloat()) * size.height) + 1f,
                    ) {
                        layer {
                            drawLayer(graphicsLayer)
                            if (0.5F + Random.nextFloat() * 3F < intensity) {
                                drawRect(
                                    color = glitchColors.random(),
                                    blendMode = BlendMode.SrcAtop
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun DrawScope.layer(block: DrawScope.() -> Unit) =
    drawIntoCanvas { canvas ->
        canvas.withSaveLayer(
            bounds = size.toRect(),
            paint = Paint(),
        ) { block() }
    }