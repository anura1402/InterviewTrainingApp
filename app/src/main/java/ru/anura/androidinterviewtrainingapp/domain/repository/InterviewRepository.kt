package ru.anura.androidinterviewtrainingapp.domain.repository

interface InterviewRepository {

    fun addToFav()
    fun addToMistakes()
    fun getTheme()
    fun generateQuestion()
}