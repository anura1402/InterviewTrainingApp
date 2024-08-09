package ru.anura.androidinterviewtrainingapp.domain.usecases

import ru.anura.androidinterviewtrainingapp.domain.repository.InterviewRepository

class ChangeIsCorrectUseCase(
    private val repository: InterviewRepository
) {
    operator fun invoke(id: Int, isCorrect: Boolean) = repository.changeIsCorrect(id, isCorrect)
}