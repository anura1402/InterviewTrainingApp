package ru.anura.androidinterviewtrainingapp.domain.usecases

import ru.anura.androidinterviewtrainingapp.domain.entity.Theme
import ru.anura.androidinterviewtrainingapp.domain.repository.InterviewRepository

class GenerateQuestionUseCase(
    private val repository: InterviewRepository
) {
    operator fun invoke(theme: Theme) = repository.generateQuestion()
}