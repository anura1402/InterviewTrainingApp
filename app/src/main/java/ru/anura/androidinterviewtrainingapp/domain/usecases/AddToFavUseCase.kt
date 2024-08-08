package ru.anura.androidinterviewtrainingapp.domain.usecases

import ru.anura.androidinterviewtrainingapp.domain.repository.InterviewRepository

class AddToFavUseCase(
    private val repository: InterviewRepository
) {
    operator fun invoke() = repository.addToFav()
}