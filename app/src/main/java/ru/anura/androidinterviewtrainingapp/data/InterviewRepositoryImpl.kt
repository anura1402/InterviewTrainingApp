package ru.anura.androidinterviewtrainingapp.data

import ru.anura.androidinterviewtrainingapp.data.database.QuestionDao
import ru.anura.androidinterviewtrainingapp.domain.entity.Question
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme
import ru.anura.androidinterviewtrainingapp.domain.repository.InterviewRepository

class InterviewRepositoryImpl(
    private val questionDao: QuestionDao,
    private val mapper:QuestionMapper
) : InterviewRepository {
    override fun changeIsFav(id: Int,isFav:Boolean) {
        questionDao.changeIsFav(id,isFav)
    }

    override fun changeIsCorrect(id: Int,isCorrect:Boolean) {
        questionDao.changeIsCorrect(id,isCorrect)
    }

//    override fun getTheme() {
//        TODO("Not yet implemented")
//    }

    override fun generateQuestion(theme: Theme):Question {
        return mapper.mapDbModelToEntity(questionDao.generateQuestion(theme))
    }

    override fun getQuestionById(id: Int): Question {
        return mapper.mapDbModelToEntity(questionDao.getQuestionById(id))
    }

}