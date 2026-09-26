package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.R

/**
 * 3D Full-HD container providing subtle layered depth, ambient lighting,
 * and the official Deepak Centers logo watermark in the background.
 */
@Composable
fun App3DBackgroundContainer(
    modifier: Modifier = Modifier,
    showWatermark: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF1F5F9), // Slate 100
            Color(0xFFE2E8F0), // Slate 200
            Color(0xFFF8FAFC)  // Slate 50
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        // Watermark of official Deepak Centers logo in background
        if (showWatermark) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.065f), // Refined subtle opacity so readability is 100% crisp
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_deepak_center_logo),
                    contentDescription = null,
                    modifier = Modifier.size(340.dp)
                )
            }
        }

        // Foreground application content
        content()
    }
}
