package ru.anura.androidinterviewtrainingapp.domain.usecases

import ru.anura.androidinterviewtrainingapp.domain.repository.InterviewRepository

class ChangeIsFavUseCase(
    private val repository: InterviewRepository
) {
    suspend operator fun invoke(id: Int, isFav: Boolean) = repository.changeIsFav(id,isFav)
}