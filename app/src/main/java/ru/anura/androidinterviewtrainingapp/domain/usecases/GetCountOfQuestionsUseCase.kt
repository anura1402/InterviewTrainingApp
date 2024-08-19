package ru.anura.androidinterviewtrainingapp.domain.usecases

import ru.anura.androidinterviewtrainingapp.domain.repository.InterviewRepository

class GetCountOfQuestionsUseCase(
    private val repository: InterviewRepository
) {
    suspend operator fun invoke() = repository.getCountOfQuestions()
}