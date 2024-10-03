package ru.anura.androidinterviewtrainingapp.domain.usecases

import ru.anura.androidinterviewtrainingapp.domain.repository.InterviewRepository
import javax.inject.Inject

class GetCountOfQuestionsUseCase @Inject constructor(
    private val repository: InterviewRepository
) {
    suspend operator fun invoke() = repository.getCountOfQuestions()
}