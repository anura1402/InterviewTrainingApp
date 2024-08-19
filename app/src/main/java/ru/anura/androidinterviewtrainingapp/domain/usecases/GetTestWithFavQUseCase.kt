package ru.anura.androidinterviewtrainingapp.domain.usecases

import ru.anura.androidinterviewtrainingapp.domain.repository.InterviewRepository

class GetTestWithFavQUseCase(
    private val repository: InterviewRepository
) {
    suspend operator fun invoke() = repository.getTestWithFavQ()
}