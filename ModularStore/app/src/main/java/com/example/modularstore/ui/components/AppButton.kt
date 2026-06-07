package com.example.modularstore.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppButton(
    text: String,
    modifier: Modifier = Modifier,
    outlined: Boolean = false,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    if (outlined) {
        OutlinedButton(onClick = onClick, modifier = modifier, enabled = enabled) {
            icon?.let { it(); Spacer(Modifier.width(6.dp)) }
            Text(text)
        }
    } else {
        Button(onClick = onClick, modifier = modifier, enabled = enabled) {
            icon?.let { it(); Spacer(Modifier.width(6.dp)) }
            Text(text)
        }
    }
}