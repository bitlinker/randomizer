package dev.kissed.randomizer.common.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
internal fun AppButton(
    text: String,
    interactionSource: MutableInteractionSource? = null,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
        elevation = ButtonDefaults.elevation(10.dp),
        enabled = enabled,
        modifier = modifier,
    ) {
        Text(text)
    }
}
