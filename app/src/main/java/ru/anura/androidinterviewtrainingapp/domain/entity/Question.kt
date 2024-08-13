package ru.anura.androidinterviewtrainingapp.domain.entity

import androidx.room.ColumnInfo
// 1 kotlin
// 2 java
// 3 go
// 4 c#
// 5 c++
// 6 kotlin
// 7 java

// select * from questionTable where name in ('kotlin', 'java')

data class Question(
    val id: Int,
    val text:String,
    val theme:Theme,
    val image: String,
    val options: List<String>,
    val answer: String,
    val isCorrectAnswer: Boolean,
    val isFavorite: Boolean
)