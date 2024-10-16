package ru.anura.androidinterviewtrainingapp.domain.repository

import ru.anura.androidinterviewtrainingapp.domain.entity.Question
import ru.anura.androidinterviewtrainingapp.domain.entity.Test
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme

interface InterviewRepository {

    suspend fun changeIsFav(id: Int, isFav:Boolean)
    suspend fun changeIsCorrect(id: Int, isCorrect:Boolean)

    fun getQuestionById(id: Int):Question

    suspend fun generateTestCurrentTheme(theme:Theme,countOfQuestions:Int): Test
    suspend fun generateTest(countOfQuestions:Int):Test

    suspend fun getTestWithWrongQ(): Test
    suspend fun getTestWithFavQ(): Test

    suspend fun getCountOfQuestions(): Int
    suspend fun getCountOfQuestionsByCurrentTheme(theme:Theme): Int
    suspend fun getCorrectAnsweredCount(): Int
    suspend fun isThemePassed(theme: Theme): Boolean

}