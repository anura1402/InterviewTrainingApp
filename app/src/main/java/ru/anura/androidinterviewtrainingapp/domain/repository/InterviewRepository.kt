package ru.anura.androidinterviewtrainingapp.domain.repository

import ru.anura.androidinterviewtrainingapp.domain.entity.Question
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme

interface InterviewRepository {

    fun changeIsFav(id: Int, isFav:Boolean)
    fun changeIsCorrect(id: Int, isCorrect:Boolean)
    //fun getTheme()
    fun generateQuestion(theme:Theme):Question
    fun getQuestionById(id: Int):Question
}