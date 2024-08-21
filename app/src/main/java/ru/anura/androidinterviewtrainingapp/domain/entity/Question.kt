package ru.anura.androidinterviewtrainingapp.domain.entity

import androidx.room.ColumnInfo

data class Question(
    val id: Int,
    val text:String,
    val theme:Theme,
    val image: String,
    val options: List<String>,
    val answer: String,
    val isCorrectAnswer: Boolean,
    val isFavorite: Boolean,
    val explanation: String
)