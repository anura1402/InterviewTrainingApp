package ru.anura.androidinterviewtrainingapp.domain.usecases

import ru.anura.androidinterviewtrainingapp.domain.repository.InterviewRepository
import javax.inject.Inject

class GetQuestionByIdUseCase @Inject constructor(
private val repository: InterviewRepository
) {

    operator fun invoke(id: Int) = repository.getQuestionById(id)
}