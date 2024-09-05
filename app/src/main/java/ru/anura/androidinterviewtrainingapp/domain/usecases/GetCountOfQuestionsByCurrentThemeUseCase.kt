package ru.anura.androidinterviewtrainingapp.domain.usecases

import ru.anura.androidinterviewtrainingapp.domain.entity.Theme
import ru.anura.androidinterviewtrainingapp.domain.repository.InterviewRepository

class GetCountOfQuestionsByCurrentThemeUseCase(
    private val repository: InterviewRepository
) {
    suspend operator fun invoke(theme: Theme) = repository.getCountOfQuestionsByCurrentTheme(theme)
}