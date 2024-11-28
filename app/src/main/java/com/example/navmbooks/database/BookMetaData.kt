package com.example.navmbooks.database

data class BookMetadata(
    val bookId: Int,
    val title: String,
    val summary: String?,
    val releaseDate: String?,
    val authorName: String
)
