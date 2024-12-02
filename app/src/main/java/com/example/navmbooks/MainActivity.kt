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
import com.example.navmbooks.ui.theme.NavRoutes
import com.example.navmbooks.ui.theme.NAVMBooksTheme
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

//        CoroutineScope(Dispatchers.IO).launch {
//            dataBaseUsage(dbViewModel)
//        }

        setContent {
            NAVMBooksTheme {
                val windowSize = calculateWindowSizeClass(this)
                BookReadingApp(
                    locale = Locale.US,
                    windowSizeClass = windowSize.widthSizeClass,
                    factory = factory,
                    dbViewModel = dbViewModel,
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


//    private suspend fun dataBaseUsage(dbViewModel: DatabaseViewModel) {
//        val newAuthor = Author(authorName = "TESTING")
//        dbViewModel.insertAuthor(newAuthor)
//
//        delay(500)
//
//        val author = dbViewModel.getAuthorById(1)
//        Log.d("Testing Author and Retrieval Insert", author.toString())
//
//        val newBook = Book(authorId = 1, title = "TESTING", summary = "TESTING", releaseDate = "TESTING")
//        dbViewModel.insertBooks(listOf(newBook))
//
//        delay(500)
//
//        val book = dbViewModel.getBookById(1)
//        Log.d("Testing Book and Retrieval Insert", book.toString())
//
//
//        val chapter = Chapter(bookId = 1, chapterNumber = 1, chapterTitle = "TESTING")
//        dbViewModel.insertChapters(listOf(chapter))
//
//        delay(500)
//
//        val chapterDetails = dbViewModel.getChaptersByBook(1)
//        Log.d("Testing Chapter and Retrieval Insert", chapterDetails[0].toString())
//
//
//        val content = Content(chapterId = 1, contentType = "Text", chapterContent = "TESTING")
//        dbViewModel.insertContents(listOf(content))
//
//        delay(500)
//
//        val chapterContent = dbViewModel.getContentByChapter(1)
//        Log.d("Testing Content and Retrieval Insert", chapterContent[0].toString())
//
//
//        val searchResult = dbViewModel.searchContentInBook(1, "TESTING")
//        Log.d("Testing Search", searchResult[0].toString())
//    }
