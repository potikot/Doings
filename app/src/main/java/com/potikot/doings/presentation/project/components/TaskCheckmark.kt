package com.potikot.doings.presentation.project.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.EaseInCirc
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.IconToggleButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.potikot.doings.R
import com.potikot.doings.ui.theme.GrayGreenCheckmark
import com.potikot.doings.ui.theme.GreenCheckmark

@Composable
fun TaskCheckmark(
    isChecked: Boolean,
    onToggleCompleted: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 30.dp
) {
    IconToggleButton(
        checked = isChecked,
        onCheckedChange = onToggleCompleted,
        modifier = modifier
            .size(size),
        colors = IconToggleButtonColors(
            containerColor = Color.Transparent,
            contentColor = GrayGreenCheckmark,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = GrayGreenCheckmark.copy(alpha = 0.75f),
            checkedContainerColor = Color.Transparent,
            checkedContentColor = GreenCheckmark
        )
    ) {
        Crossfade(
            targetState = isChecked,
            label = "IconCrossfade",
            animationSpec = TweenSpec(
                durationMillis = 150,
                easing = EaseInCirc
            )
        ) { isChecked ->
            if (isChecked) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.filled_check_circle_24),
                    contentDescription = null,
                    modifier = Modifier.size(size),
                )
            } else {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.outlined_circle_24),
                    contentDescription = null,
                    modifier = Modifier.size(size),
                )
            }
        }
    }
}