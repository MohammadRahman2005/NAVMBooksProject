package com.example.navmbooks

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navmbooks.data.FileRepository
import com.example.navmbooks.data.UnzipUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

@SuppressLint("MutableCollectionMutableState")
class BookViewModel(private val repository: FileRepository) : ViewModel() {

    private val _directoryContents = MutableLiveData<List<String>>()
    val directoryContents: LiveData<List<String>> = _directoryContents

    // Function to set up file download
    fun setupDownload(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val fileName = url.substringAfterLast("/")
            val destDir = "DownloadedFiles"

            val file = repository.createFile(destDir, fileName)

            if (repository.downloadFile(url, file)) {
                if (file.exists() && file.length() > 0) {
                    updateDirectoryContents(destDir)
                    val destDirFile = repository.createFile(destDir, fileName.substringBeforeLast("."))
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


    var book1 by mutableStateOf<Book?>(null)
        private set

    var bookList by mutableStateOf<List<Book?>>(emptyList())
        private set

    var book2 by mutableStateOf<Book?>(null)
        private set

    var book3 by mutableStateOf<Book?>(null)
        private set
    init {
        loadBook()
    }

    private fun loadBook() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                setupDownload("https://www.gutenberg.org/cache/epub/8710/pg8710-h.zip")
//                setupDownload("https://www.gutenberg.org/cache/epub/20195/pg20195-images.html")
//                setupDownload("https://www.gutenberg.org/cache/epub/40367/pg40367-images.html")

                book1 = Book.readBookURL("https://www.gutenberg.org/cache/epub/8710/pg8710-images.html")
                book2 = Book.readBookURL("https://www.gutenberg.org/cache/epub/20195/pg20195-images.html")
                book3 = Book.readBookURL("https://www.gutenberg.org/cache/epub/40367/pg40367-images.html")
                bookList = bookList + book1
                bookList = bookList + book2
                bookList = bookList + book3
            } catch (e: IOException) {
                // Handle the error appropriately
                e.printStackTrace()
            }
        }
    }

    var isReadingMode = mutableStateOf(false)
        private set

    fun toggleReadingMode(isEnabled: Boolean) {
        isReadingMode.value = isEnabled
    }
}