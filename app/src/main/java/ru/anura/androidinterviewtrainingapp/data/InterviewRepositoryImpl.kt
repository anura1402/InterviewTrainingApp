package ru.anura.androidinterviewtrainingapp.data

import android.app.Application
import ru.anura.androidinterviewtrainingapp.data.database.QuestionDao
import ru.anura.androidinterviewtrainingapp.data.database.QuestionDatabase
import ru.anura.androidinterviewtrainingapp.domain.entity.Question
import ru.anura.androidinterviewtrainingapp.domain.entity.Test
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme
import ru.anura.androidinterviewtrainingapp.domain.repository.InterviewRepository

class InterviewRepositoryImpl(application: Application) : InterviewRepository {
    private val questionDao: QuestionDao = QuestionDatabase.getInstance(application).questionDao()
    private val mapper: QuestionMapper = QuestionMapper()

    override suspend fun changeIsFav(id: Int, isFav: Boolean) {
        questionDao.changeIsFav(id, isFav)
    }

    override suspend fun changeIsCorrect(id: Int, isCorrect: Boolean) {
        questionDao.changeIsCorrect(id, isCorrect)
    }

    override fun getQuestionById(id: Int): Question {
        return mapper.mapDbModelToEntity(questionDao.getQuestionById(id))
    }

    override suspend fun generateTestCurrentTheme(theme: Theme, countOfQuestions: Int): Test {
        val themes = arrayOf(theme)
        val themeNames: List<String> = themes.map { it.toString() }.toList()
        val questions = mutableListOf<Question>()
        for (i in 1..countOfQuestions) {
            questions.add(
                mapper.mapDbModelToEntity(
                    questionDao.getQuestions(themeNames, countOfQuestions)[countOfQuestions - 1]
                )
            )
        }
        return Test(
            countOfQuestions = countOfQuestions,
            questions = questions
        )
    }

    override suspend fun generateTest(countOfQuestions: Int): Test {
        val themes = Theme.entries.toTypedArray()
        val themeNames: List<String> = themes.map { it.toString() }.toList()
        val questions = mapper.mapListDbModelToListEntity(
            questionDao.getQuestions(
                themeNames,
                countOfQuestions
            )
        )
        if (questions.size < countOfQuestions) {
            throw IllegalArgumentException("Недостаточно вопросов в базе данных для создания теста.")
        }

        return Test(
            countOfQuestions = countOfQuestions,
            questions = questions
        )
    }

    override suspend fun getTestWithWrongQ(): Test {
        val questions = mapper.mapListDbModelToListEntity(questionDao.getWrongQuestions())
        return Test(
            countOfQuestions = questions.size,
            questions = questions
        )
    }

    override suspend fun getTestWithFavQ(): Test {
        val questions = mapper.mapListDbModelToListEntity(questionDao.getFavQuestions())
        return Test(
            countOfQuestions = questions.size,
            questions = questions
        )
    }

    override suspend fun getCountOfQuestions(): Int {
        return questionDao.getCountOfQuestions()
    }


}