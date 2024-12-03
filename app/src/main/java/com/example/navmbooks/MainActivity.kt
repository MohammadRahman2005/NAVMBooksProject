package com.example.navmbooks

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.example.navmbooks.database.DatabaseViewModel
import com.example.navmbooks.ui.theme.BookReadingApp
import com.example.navmbooks.ui.theme.BookViewModelFactory
import com.example.navmbooks.ui.theme.NAVMBooksTheme
import com.example.navmbooks.ui.theme.NavRoutes
import java.util.Locale

/**
 * MainActivity is the entry point of the application.
 * It sets up the content view and determines the navigation style
 * based on the device's window size.
 */
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val dbViewModel = DatabaseViewModel(application)
        val factory by lazy {
            BookViewModelFactory(this.applicationContext, dbViewModel) // Use application context to prevent memory leaks
        }

        // Retrieve last accessed book and chapter from SharedPreferences
        val sharedPreferences = getSharedPreferences("book_preferences", Context.MODE_PRIVATE)
        val lastAccessedBookIndex = sharedPreferences.getInt("last_accessed_book", -1)
        val lastAccessedChapter = sharedPreferences.getInt("last_accessed_chapter_$lastAccessedBookIndex", -1)

        // Determine the start destination
        val startDestination = if (lastAccessedBookIndex != -1 && lastAccessedChapter != -1) {
            NavRoutes.ReadingScreen.createRoute(lastAccessedBookIndex, lastAccessedChapter - 1)
        } else {
            NavRoutes.HomeScreen.route // Default route if no chapter is saved
        }

        setContent {
            NAVMBooksTheme {
                val windowSize = calculateWindowSizeClass(this)
                BookReadingApp(
                    locale = Locale.US,
                    windowSizeClass = windowSize.widthSizeClass,
                    factory = factory,
                    startDestination = startDestination, // Pass the start destination to the composable
                    onResetLastAccessed = { resetLastAccessed(sharedPreferences) }
                )
            }
        }
    }

    private fun resetLastAccessed(sharedPreferences: SharedPreferences) {
        with(sharedPreferences.edit()) {
            clear() // Clear all saved data
            apply()
        }
        Toast.makeText(this, "Last accessed chapter reset successfully", Toast.LENGTH_SHORT).show()
    }

}
