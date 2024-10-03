package ru.anura.androidinterviewtrainingapp.domain.usecases

import ru.anura.androidinterviewtrainingapp.domain.repository.InterviewRepository
import javax.inject.Inject

class ChangeIsCorrectUseCase @Inject constructor(
    private val repository: InterviewRepository
) {
    suspend operator fun invoke(id: Int, isCorrect: Boolean) = repository.changeIsCorrect(id, isCorrect)
}