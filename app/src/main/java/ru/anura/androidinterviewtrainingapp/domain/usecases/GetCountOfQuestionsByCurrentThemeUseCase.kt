package ru.anura.androidinterviewtrainingapp.domain.usecases

import ru.anura.androidinterviewtrainingapp.domain.entity.Theme
import ru.anura.androidinterviewtrainingapp.domain.repository.InterviewRepository
import javax.inject.Inject

class GetCountOfQuestionsByCurrentThemeUseCase @Inject constructor(
    private val repository: InterviewRepository
) {
    suspend operator fun invoke(theme: Theme) = repository.getCountOfQuestionsByCurrentTheme(theme)
}