package ru.anura.androidinterviewtrainingapp.data

import android.app.Application
import android.hardware.biometrics.BiometricManager.Strings
import android.util.Log
import ru.anura.androidinterviewtrainingapp.data.database.QuestionDao
import ru.anura.androidinterviewtrainingapp.data.database.QuestionDatabase
import ru.anura.androidinterviewtrainingapp.domain.entity.Question
import ru.anura.androidinterviewtrainingapp.domain.entity.Test
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme
import ru.anura.androidinterviewtrainingapp.domain.repository.InterviewRepository
import kotlin.random.Random

class InterviewRepositoryImpl(application: Application) : InterviewRepository {
    private val questionDao: QuestionDao = QuestionDatabase.getInstance(application).questionDao()
    private val mapper: QuestionMapper = QuestionMapper()

    override suspend fun changeIsFav(id: Int, isFav: Boolean) {
        questionDao.changeIsFav(id, isFav)
    }

    override suspend fun changeIsCorrect(id: Int, isCorrect: Boolean) {
        questionDao.changeIsCorrect(id, isCorrect)
    }

//    override fun getTheme() {
//        TODO("Not yet implemented")
//    }

//    override fun generateQuestion(countOfQuestions: Int):Question {
//        val themes = Theme.entries.toTypedArray()
//        return mapper.mapDbModelToEntity(questionDao.generateQuestion(themes,countOfQuestions))
//    }
//    override fun generateQuestionCurrentTheme(theme: Theme,countOfQuestions: Int):Question {
//        val themes = arrayOf(theme)
//        return mapper.mapDbModelToEntity(questionDao.generateQuestion(themes,countOfQuestions))
//    }

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
        Log.d("QuestionDatabase", "generateTest: $countOfQuestions")
        val themes = Theme.entries.toTypedArray()
        val themeNames: List<String> = themes.map { it.toString() }.toList()
        Log.d("QuestionDatabase", "themes: $themeNames")
        Log.d("QuestionDatabase", "num: $countOfQuestions")
        val allQuestions = questionDao.getQuestions(themeNames, countOfQuestions)
        Log.d("QuestionDatabase", "allQuestions: $allQuestions")
        if (allQuestions.size < countOfQuestions) {
            throw IllegalArgumentException("Недостаточно вопросов в базе данных для создания теста.")
        }
        val questions = mutableListOf<Question>()
        for (i in 0 until countOfQuestions) {
            questions.add(
                mapper.mapDbModelToEntity(
                    allQuestions[i]
                )
            )
            Log.d("QuestionDatabase", "i: ${i}")
            Log.d("QuestionDatabase", "Количество вопросов в базе данных: ${questions.size}")
        }
        Log.d("QuestionDatabase", "Test: ${Test(
            countOfQuestions = countOfQuestions,
            questions = questions
        )}")
        return Test(
            countOfQuestions = countOfQuestions,
            questions = questions
        )
    }

//    override fun generateTests(countOfQuestions: Int):Test {
//        val themes = Theme.entries.toTypedArray()
//        for (theme in themes) {
//            this.generateTest(theme, countOfQuestions)
//        }
//        val questions = mutableListOf<Question>()
//        for (i in 1..countOfQuestions) {
//            val randomIndex = Random.nextInt(themes.size)
//            questions.add(generateQuestion(themes[randomIndex]))
//        }
//        return questions
//    }

}