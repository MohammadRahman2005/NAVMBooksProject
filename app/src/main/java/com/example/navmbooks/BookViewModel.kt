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
import java.io.File
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

    var bookList by mutableStateOf<List<Book?>>(emptyList())
        private set

    init {
        loadBookFromLocalStorage()
    }
    private fun loadBookFromLocalStorage(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val urls = repository.context.resources.getStringArray(R.array.booksUrl)
                urls.forEach { url ->
                    setupDownload(url)
                }
                val files = repository.context.resources.getStringArray(R.array.booksFile)
                val coverImages = repository.context.resources.getStringArray(R.array.booksCover)
                var i = 0;
                files.forEach {file ->
                    val htmlFile = File(repository.context.getExternalFilesDir(null), file)
                    val cover  = File(repository.context.getExternalFilesDir(null), coverImages[i])
                    i++
                    if (htmlFile.exists() && cover.exists()) {
                        val book = Book.readBookFromFile(htmlFile, cover)
                        bookList= bookList + book
                    }else{
                        Log.e("BookViewModel", "The HTML file does not exist at $htmlFile")
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("BookViewModel", "Error reading local book file", e)
            }
        }
    }

    var isReadingMode = mutableStateOf(false)
        private set

    fun toggleReadingMode(isEnabled: Boolean) {
        isReadingMode.value = isEnabled
    }
}