package com.tyshi00.astrolight.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.thelightphone.sdk.ui.LightThemeTokens

@Composable
fun rememberHaptic(): () -> Unit {
    val haptic = LocalHapticFeedback.current
    return {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }
}

class TintedPainter(
    private val delegate: Painter,
    private val colorFilter: ColorFilter,
) : Painter() {
    override val intrinsicSize: Size get() = delegate.intrinsicSize

    override fun DrawScope.onDraw() {
        with(delegate) {
            draw(size = size, colorFilter = colorFilter)
        }
    }
}

@Composable
fun Painter.tinted(): Painter {
    val tint = ColorFilter.tint(LightThemeTokens.colors.content)
    return TintedPainter(this, tint)
}
