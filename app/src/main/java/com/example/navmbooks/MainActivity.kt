@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.navmbooks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.example.navmbooks.ui.theme.BookReadingApp
import com.example.navmbooks.ui.theme.BookViewModelFactory
import com.example.navmbooks.ui.theme.NAVMBooksTheme
import java.util.Locale

/**
 * MainActivity is the entry point of the application.
 * It sets up the content view and determines the navigation style
 * based on the device's window size.
 */
class MainActivity : ComponentActivity() {

    private val factory by lazy {
        BookViewModelFactory(this.applicationContext) // Use application context to prevent memory leaks
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NAVMBooksTheme {
                val windowSize = calculateWindowSizeClass(this)
                BookReadingApp(
                    locale = Locale.US,
                    windowSizeClass = windowSize.widthSizeClass,
                    factory=factory
                )
            }
        }
    }
}