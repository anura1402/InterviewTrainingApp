package ru.anura.androidinterviewtrainingapp.domain.usecases

import ru.anura.androidinterviewtrainingapp.domain.entity.Theme
import ru.anura.androidinterviewtrainingapp.domain.repository.TheoryRepository

class GetTheoryListUseCase(
    private val repository: TheoryRepository
) {
    suspend operator fun invoke(theme: Theme) = repository.getTheoryList(theme)
}