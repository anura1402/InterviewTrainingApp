package ru.anura.androidinterviewtrainingapp.domain.usecases

import ru.anura.androidinterviewtrainingapp.domain.entity.Theme
import ru.anura.androidinterviewtrainingapp.domain.repository.InterviewRepository

class GenerateTestCurrentThemeUseCase (
    private val repository: InterviewRepository
){

    suspend operator fun invoke(theme: Theme, countOfQuestions:Int) = repository.generateTestCurrentTheme(theme, countOfQuestions)
}