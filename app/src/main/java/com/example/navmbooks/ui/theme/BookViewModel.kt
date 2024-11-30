package com.example.navmbooks.ui.theme

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navmbooks.R
import com.example.navmbooks.data.FileRepository
import com.example.navmbooks.data.UnzipUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

/**
 * This class represents a view model for books
 */
@SuppressLint("MutableCollectionMutableState")
class BookViewModel(private val repository: FileRepository) : ViewModel() {
    var titles = repository.context.resources.getStringArray(R.array.DownloadedBooksTitle).toList()

    var urls = repository.context.resources.getStringArray(R.array.DownloadableBooksUrl).toList()

    var files = repository.context.resources.getStringArray(R.array.DownloadableBooksFile).toList()

    var images = repository.context.resources.getStringArray(R.array.DownloadedBooksCover).toList()

    fun removeBookAt(index: Int) {
        titles = titles.toMutableList().apply { removeAt(index) }
        urls = urls.toMutableList().apply { removeAt(index) }
        files = files.toMutableList().apply { removeAt(index) }
        images = images.toMutableList().apply { removeAt(index) }
    }

    private val _directoryContents = MutableLiveData<List<String>>()
    val directoryContents: LiveData<List<String>> = _directoryContents

    private var currentBookDirectory: String? = null

    fun setupDownload(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val fileName = url.substringAfterLast("/")
            val destDir = "DownloadedFiles"

            val file = repository.createFile(destDir, fileName)

            if (repository.downloadFile(url, file)) {
                if (file.exists() && file.length() > 0) {
                    updateDirectoryContents(destDir)
                    val destDirFile = repository.createFile(destDir, fileName.substringBeforeLast("."))
                    currentBookDirectory = destDirFile.absolutePath
                    UnzipUtils.unzip(file, destDirFile.absolutePath)
                } else {
                    Log.e("DownloadViewModel", "File is empty or does not exist.")
                }
            } else {
                Log.e("DownloadViewModel", "Failed to download file")
            }
        }
    }

    private fun updateDirectoryContents(directoryName: String) {
        val contents = repository.listDirectoryContents(directoryName)
        _directoryContents.postValue(contents)
    }

    fun confirmDeletion(directoryName: String) {
        repository.deleteDirectoryContents(directoryName)
        updateDirectoryContents(directoryName)
    }

    var bookList by mutableStateOf<List<Book?>>(emptyList())
        private set
    init {
       getBooks(urls = repository.context.resources.getStringArray(R.array.booksUrl), files = repository.context.resources.getStringArray(R.array.booksFile), images = repository.context.resources.getStringArray(R.array.booksCover))
    }
    private fun getBooks(urls: Array<String>, files: Array<String>, images: Array<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            urls.forEachIndexed { index, url ->
                val filePath = files.getOrNull(index)
                val imagePath = images.getOrNull(index)

                if (filePath != null && imagePath != null) {
                    val book = processSingleBook(url, filePath, imagePath)
                    if (book != null) {
                        bookList = bookList + book
                    }
                } else {
                    Log.e("BookViewModel", "Invalid file or image path for index $index")
                }
            }
        }
    }

    fun addBookToBookList(url: String, filePath: String, imagePath: String) {
        viewModelScope.launch {
            val book = processSingleBook(url, filePath, imagePath)
            bookList = bookList + book
        }
    }


    private fun processSingleBook(url: String, filePath: String, imagePath: String): Book? {
        return try {
            setupDownload(url)

            val htmlFile = File(repository.context.getExternalFilesDir(null), filePath)
            val coverImage = File(repository.context.getExternalFilesDir(null), imagePath)

            if (htmlFile.exists() && coverImage.exists()) {
                currentBookDirectory = htmlFile.parent
                Book.readBookFromFile(htmlFile, coverImage, currentBookDirectory!!)
            } else {
                Log.e("BookViewModel", "Files do not exist: HTML=$htmlFile, Cover=$coverImage")
                null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("BookViewModel", "Error processing book: $url", e)
            null
        }
    }

    var isReadingMode = mutableStateOf(false)
        private set

    fun toggleReadingMode(isEnabled: Boolean) {
        isReadingMode.value = isEnabled
    }
}