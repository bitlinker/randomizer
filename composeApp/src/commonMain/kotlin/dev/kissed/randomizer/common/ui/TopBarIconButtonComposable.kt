package dev.kissed.randomizer.common.ui

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

@Composable
internal fun TopBarIconButtonComposable(
    painter: Painter,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
) {
    IconButton(
        onClick = onClick,
        enabled = isEnabled,
        content = {
            Icon(
                painter,
                contentDescription = null
            )
        }
    )
}