package com.example.navmbooks.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "authors")
data class Author(
    @PrimaryKey(autoGenerate = true)
    val authorId: Int = 0,
    val authorName: String
)
