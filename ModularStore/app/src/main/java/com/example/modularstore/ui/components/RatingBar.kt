package com.example.modularstore.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size

@Composable
fun RatingBar(
    rating: Float,
    modifier: Modifier = Modifier,
    starColor: Color = Color(0xFFFFC107)
) {
    Row(modifier = modifier) {
        repeat(5) { index ->
            Icon(
                imageVector        = if (index < rating.toInt()) Icons.Filled.Star
                else Icons.Outlined.StarBorder,
                contentDescription = null,
                tint               = starColor,
                modifier           = Modifier.size(14.dp)
            )
        }
    }
}