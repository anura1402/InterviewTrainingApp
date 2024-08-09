package ru.anura.androidinterviewtrainingapp.domain.entity

class Question(
    val id: Int,
    val text:String,
    val theme:Theme,
    val answer: String,
    val isCorrectAnswer: Boolean,
    val isFavorite: Boolean
) {
}