package ru.anura.androidinterviewtrainingapp.domain.usecases

import ru.anura.androidinterviewtrainingapp.domain.repository.InterviewRepository

class GetQuestionByIdUseCase(
private val repository: InterviewRepository
) {

    operator fun invoke(id: Int) = repository.getQuestionById(id)
}