package ru.anura.androidinterviewtrainingapp.domain.repository

import ru.anura.androidinterviewtrainingapp.domain.entity.Question
import ru.anura.androidinterviewtrainingapp.domain.entity.Test
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme

interface InterviewRepository {

    suspend fun changeIsFav(id: Int, isFav:Boolean)
    suspend fun changeIsCorrect(id: Int, isCorrect:Boolean)
    //fun getTheme()
//    fun generateQuestion():Question
//    fun generateQuestionCurrentTheme(theme:Theme):Question
    fun getQuestionById(id: Int):Question

    suspend fun generateTestCurrentTheme(theme:Theme,countOfQuestions:Int): Test
    suspend fun generateTest(countOfQuestions:Int):Test
}