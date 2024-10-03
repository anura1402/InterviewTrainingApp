package ru.anura.androidinterviewtrainingapp.domain.usecases

import ru.anura.androidinterviewtrainingapp.domain.entity.Theme
import ru.anura.androidinterviewtrainingapp.domain.repository.InterviewRepository
import javax.inject.Inject

class GetCorrectAnsweredCountUseCase @Inject constructor(
    private val repository: InterviewRepository
) {
    suspend operator fun invoke() = repository.getCorrectAnsweredCount()
}