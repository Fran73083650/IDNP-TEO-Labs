package com.example.modularstore.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun PriceTag(
    price: Double,
    originalPrice: Double = 0.0,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (originalPrice > price) {
            val discount = ((1 - price / originalPrice) * 100).toInt()
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text  = "$${String.format("%,.2f", price)}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text  = "$${String.format("%,.2f", originalPrice)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textDecoration = TextDecoration.LineThrough
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text  = "-$discount%",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            Text(
                text  = "$${String.format("%,.2f", price)}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}