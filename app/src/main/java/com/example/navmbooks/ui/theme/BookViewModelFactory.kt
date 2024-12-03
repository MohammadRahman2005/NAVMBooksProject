package com.example.navmbooks.ui.theme

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.navmbooks.data.FileRepository
import com.example.navmbooks.database.DatabaseViewModel

/**
 * this class is a factory for creating a BookViewModel
 */
class BookViewModelFactory(private val context: Context, private val dbViewModel: DatabaseViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookViewModel::class.java)) {
            val repository = FileRepository(context)
            return BookViewModel(repository, dbViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
