package dev.kissed.randomizer.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
internal fun ColorViewComposable(
    color: Color,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null,
    size: Dp = Dimens.dp32,
) {
    val shape = remember { RoundedCornerShape(Dimens.dp4) }
    Box(
        Modifier
            .border(
                width = Dimens.dp2,
                color = if (isSelected) Color.Black else Color.Transparent,
                shape = shape,
            )
            .shadow(
                elevation = Dimens.dp4,
                shape = shape
            )
            .clickable(
                enabled = onClick != null,
                onClick = { onClick?.invoke() },
            )
            .size(size)
            .background(
                color = color,
                shape = shape
            )
    )
}