package dev.kissed.randomizer.common.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.random.nextInt

@Composable
internal fun Modifier.glitchEffect(
    key: Any? = null,
    glitchColors: List<Color>,
    slices: Int = 20,
    isEnabled: Boolean = true,
): Modifier {

    val graphicsLayer = rememberGraphicsLayer()
    var step by remember { mutableStateOf(0) }

    LaunchedEffect(key) {
        Animatable(if (isEnabled) 10f else 0f)
            .animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = LinearEasing,
                )
            ) {
                step = this.value.roundToInt()
            }
    }

    return drawWithContent {
        if (step == 0) {
            drawContent()
            return@drawWithContent
        }
        graphicsLayer.record { this@drawWithContent.drawContent() }

        val intensity = step / 10f
        for (i in 0 until slices) {
            translate(
                left = if (Random.nextInt(5) < step)
                    Random.nextInt(-20..20).toFloat() * intensity
                else
                    0f
            ) {
                scale(
                    scaleY = 1f,
                    scaleX = if (Random.nextInt(10) < step)
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
                            if (Random.nextInt(5, 30) < step) {
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