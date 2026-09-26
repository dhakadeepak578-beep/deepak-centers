package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * High-fidelity Jetpack Compose implementation of the official Deepak Centers
 * circular seal (Sigdola Bara, Sikar, Rajasthan) with gold crown, navy lettering,
 * red "Centers", and blue banner.
 */
@Composable
fun DeepakCenterLogoBadge(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    elevation: Dp = 2.dp
) {
    // Proportional scale factor relative to base 80dp
    val scale = size.value / 80f

    val goldOuter = Brush.sweepGradient(
        listOf(
            Color(0xFFE5A93C),
            Color(0xFFFFD54F),
            Color(0xFFB8860B),
            Color(0xFFFFE082),
            Color(0xFFE5A93C)
        )
    )

    Box(
        modifier = modifier
            .size(size)
            .shadow(elevation, CircleShape)
            .clip(CircleShape)
            .background(goldOuter)
            .padding((2.5f * scale).dp),
        contentAlignment = Alignment.Center
    ) {
        // Inner thin gold ring & white circle
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(Color.White)
                .border((1.2f * scale).dp, Color(0xFFD4AF37), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding((2f * scale).dp)
            ) {
                // Crown at top (Gold crown representation)
                Row(
                    horizontalArrangement = Arrangement.spacedBy((1.5f * scale).dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Box(
                        modifier = Modifier
                            .size((3.5f * scale).dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE5A93C))
                    )
                    Box(
                        modifier = Modifier
                            .size((5.5f * scale).dp)
                            .clip(CircleShape)
                            .background(Color(0xFFD4AF37))
                    )
                    Box(
                        modifier = Modifier
                            .size((3.5f * scale).dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE5A93C))
                    )
                }

                Spacer(modifier = Modifier.height((1f * scale).dp))

                // "Deepak" in Royal Navy Blue
                Text(
                    text = "Deepak",
                    fontSize = (14.5f * scale).sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFF0D2866),
                    lineHeight = (15f * scale).sp,
                    letterSpacing = (-0.5f).sp
                )

                // "— Centers —" in Bold Red
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width((6f * scale).dp)
                            .height((1f * scale).dp)
                            .background(Color(0xFFD4AF37))
                    )
                    Spacer(modifier = Modifier.width((2f * scale).dp))
                    Text(
                        text = "Centers",
                        fontSize = (11f * scale).sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFD32F2F),
                        lineHeight = (11.5f * scale).sp,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.width((2f * scale).dp))
                    Box(
                        modifier = Modifier
                            .width((6f * scale).dp)
                            .height((1f * scale).dp)
                            .background(Color(0xFFD4AF37))
                    )
                }

                Spacer(modifier = Modifier.height((1.5f * scale).dp))

                // Blue Pill: "Sigdola Bara"
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape((6f * scale).dp))
                        .background(Color(0xFF0D2866))
                        .padding(
                            horizontal = (5f * scale).dp,
                            vertical = (1f * scale).dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Sigdola Bara",
                        fontSize = (7f * scale).sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = (8f * scale).sp
                    )
                }

                if (size >= 55.dp) {
                    Spacer(modifier = Modifier.height((1f * scale).dp))
                    Text(
                        text = "Sikar • Rajasthan",
                        fontSize = (5.5f * scale).sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0D2866),
                        lineHeight = (6f * scale).sp
                    )
                }
            }
        }
    }
}
