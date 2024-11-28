package com.example.navmbooks.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "contents",
    foreignKeys = [
        ForeignKey(
            entity = Chapter::class,
            parentColumns = ["chapterId"],
            childColumns = ["chapterId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Content(
    @PrimaryKey(autoGenerate = true) val contentId: Int = 0,
    val chapterId: Int,
    val contentType: String,
    val chapterContent: String
)
