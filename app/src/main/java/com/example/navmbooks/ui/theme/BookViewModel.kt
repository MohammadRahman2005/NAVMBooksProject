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
import com.example.navmbooks.data.ImageItem
import com.example.navmbooks.data.TableItem
import com.example.navmbooks.data.TextItem
import com.example.navmbooks.data.UnzipUtils
import com.example.navmbooks.database.DatabaseViewModel
import com.example.navmbooks.database.entities.Author
import com.example.navmbooks.database.entities.Content
import com.example.navmbooks.ui.theme.Book
import com.example.navmbooks.ui.theme.Chapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import com.example.navmbooks.database.entities.Book as dbBook
import com.example.navmbooks.database.entities.Chapter as dbChapter

/**
 * This class represents a view model for books
 */
@SuppressLint("MutableCollectionMutableState")
class BookViewModel(private val repository: FileRepository, private val dbViewModel : DatabaseViewModel) : ViewModel() {
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

    suspend fun setupDownload(url: String): Boolean {
        return withContext(Dispatchers.IO) {
            val fileName = url.substringAfterLast("/")
            val destDir = "DownloadedFiles"

            val file = repository.createFile(destDir, fileName)

            if (repository.downloadFile(url, file)) {
                if (file.exists() && file.length() > 0) {
                    updateDirectoryContents(destDir)
                    val destDirFile = repository.createFile(destDir, fileName.substringBeforeLast("."))
                    currentBookDirectory = destDirFile.absolutePath
                    UnzipUtils.unzip(file, destDirFile.absolutePath)
                    true
                } else {
                    Log.e("DownloadViewModel", "File is empty or does not exist.")
                    false
                }
            } else {
                Log.e("DownloadViewModel", "Failed to download file")
                false
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
            val dbBooksList = dbViewModel.getAllBooks()
            Log.d("Database", "$dbBooksList")
            if (!dbBooksList.isNullOrEmpty()) {
                dbBooksList.forEach { book->
                    val author = dbViewModel.getAuthorById(book.authorId)
                    val chapters = dbViewModel.getChaptersByBook(bookId = book.bookId)
                    val modelChapters = ArrayList<Chapter>()
                    val modelContents = StringBuilder()
                    chapters.forEach{ chapter->
                        val contents = dbViewModel.getContentByChapter(chapterId = chapter.chapterId)
                        val modelChapter = Chapter(chapter.chapterTitle, chapter.chapterNumber)
                        contents.forEach{ content ->
                            when (content.contentType) {

                                "Text" -> {
                                    val textItem = TextItem(content.chapterContent)
                                    modelChapter.content.add(textItem)
                                }
                                "Image" -> {
                                    val imageItem = ImageItem(content.chapterContent)
                                    modelChapter.content.add(imageItem)
                                }
                                "Table" -> {
                                    val tableItem = TableItem(content.chapterContent)
                                    modelChapter.content.add(tableItem)
                                }
                            }
                        }
                        modelChapters.add(modelChapter)
                    }
                    val image = dbViewModel.getBookById(book.bookId)
                    val modelBook = Book(book.title, author.authorName, modelChapters, modelContents, image.imagePath)
                    bookList = bookList + modelBook
                }
            } else {
                urls.forEachIndexed { index, url ->
                    val filePath = files.getOrNull(index)
                    val imagePath = images.getOrNull(index)

                    if (filePath != null && imagePath != null) {
                        val book = processSingleBook(url, filePath, imagePath)
                        if (book != null) {
                            bookList = bookList + book

                            val authorId = dbViewModel.insertAuthor(Author(authorName = book.author))
                            Log.d("Database", "INSERT AUTHOR ${book.author}")

                            val bookId = dbViewModel.insertBooks(dbBook(title = book.title, authorId = authorId, imagePath = book.coverImage))
                            Log.d("Database", "INSERT BOOK ${book.title}")

                            book.chapters.forEach{ chapter ->
                                val chapterId = dbViewModel.insertChapters(
                                    dbChapter(bookId = bookId, chapterNumber = chapter.chapNum, chapterTitle = chapter.title)
                                )
                                chapter.content.forEach{ cont ->
                                    when (cont) {
                                        is TextItem -> dbViewModel.insertContents(Content(chapterId = chapterId, contentType = "Text", chapterContent = cont.text))
                                        is ImageItem -> dbViewModel.insertContents(Content(chapterId = chapterId, contentType = "Image", chapterContent = cont.imagePath))
                                        is TableItem -> dbViewModel.insertContents(Content(chapterId = chapterId, contentType = "Table", chapterContent = cont.text))
                                    }
                                }
                            }
                        }
                    } else {
                        Log.e("BookViewModel", "Invalid file or image path for index $index")
                    }
                }
            }
        }
    }

    fun addBookToBookList(title: String, url: String, filePath: String, imagePath: String) {
        viewModelScope.launch {
            Log.d("BookViewModel", "Processing book: $url")
            val book = processSingleBook(url, filePath, imagePath)
            if (book != null) {
                bookList = bookList + book
            }
        }
    }


    private suspend fun processSingleBook(url: String, filePath: String, imagePath: String): Book? {
        return try {
            val success = setupDownload(url)
            if (!success) return null

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