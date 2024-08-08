package ru.anura.androidinterviewtrainingapp.domain.usecases

import ru.anura.androidinterviewtrainingapp.domain.repository.InterviewRepository

class AddToMistakesUseCase(
    private val repository: InterviewRepository
) {
    operator fun invoke() = repository.addToMistakes()
}