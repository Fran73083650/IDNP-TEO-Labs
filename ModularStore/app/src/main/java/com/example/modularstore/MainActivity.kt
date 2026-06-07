package com.example.modularstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.modularstore.navigation.AppNavigation
import com.example.modularstore.ui.theme.AppThemeMode
import com.example.modularstore.ui.theme.ModularStoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ModularStoreTheme(themeMode = AppThemeMode.BLUE) {
                AppNavigation()
            }
        }
    }
}