package com.example.navmbooks.database

data class ContentWithChapterInfo(
    val contentId: Int,
    val contentType: String,
    val chapterContent: String,
    val chapterTitle: String,
    val chapterNumber: Int
)
