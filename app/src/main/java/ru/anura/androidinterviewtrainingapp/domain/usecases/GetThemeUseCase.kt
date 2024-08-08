package ru.anura.androidinterviewtrainingapp.domain.usecases

import ru.anura.androidinterviewtrainingapp.domain.repository.InterviewRepository

class GetThemeUseCase(
    private val repository: InterviewRepository
) {
    operator fun invoke() = repository.getTheme()
}