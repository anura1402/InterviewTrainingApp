package ru.anura.androidinterviewtrainingapp.domain.usecases

import ru.anura.androidinterviewtrainingapp.domain.entity.Theme
import ru.anura.androidinterviewtrainingapp.domain.repository.InterviewRepository

class GenerateTestUseCase (
    private val repository: InterviewRepository
){

    suspend operator fun invoke(countOfQuestions:Int) = repository.generateTest(countOfQuestions)
}