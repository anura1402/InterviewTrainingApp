package ru.anura.androidinterviewtrainingapp.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.anura.androidinterviewtrainingapp.data.InterviewRepositoryImpl
import ru.anura.androidinterviewtrainingapp.domain.entity.Question
import ru.anura.androidinterviewtrainingapp.domain.entity.Test
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme
import ru.anura.androidinterviewtrainingapp.domain.usecases.ChangeIsCorrectUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.ChangeIsFavUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GenerateTestCurrentThemeUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GenerateTestUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GetQuestionByIdUseCase
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class QuestionViewModel(
    private val application: Application,
    private val theme: Theme
) : AndroidViewModel(application) {
    private val repository = InterviewRepositoryImpl(application)

    private val changeIsCorrectUseCase = ChangeIsCorrectUseCase(repository)
    private val changeIsFavUseCase = ChangeIsFavUseCase(repository)

    //    private val generateQuestionsUseCase = GenerateQuestionsUseCase(repository)
//    private val generateQuestionsCurrentThemeUseCase = GenerateQuestionsCurrentThemeUseCase(repository)
    private val getQuestionByIdUseCase = GetQuestionByIdUseCase(repository)
    private val generateTestCurrentThemeUseCase = GenerateTestCurrentThemeUseCase(repository)
    private val generateTestUseCase = GenerateTestUseCase(repository)


    private var _test = MutableLiveData<Test>()
    val test: LiveData<Test>
        get() = _test

    private val _answeredQuestions = MutableLiveData<MutableList<Boolean>>().apply {
        value = mutableListOf()
    }
    val answeredQuestions: LiveData<MutableList<Boolean>>
        get() = _answeredQuestions

    private val _answerResults = MutableLiveData<Map<Int, Boolean>>()
    val answerResults: LiveData<Map<Int, Boolean>>
        get() = _answerResults


    private val _selectedOptionsMap = mutableMapOf<Int, Int>()
    val selectedOptionsMap: Map<Int, Int> get() = _selectedOptionsMap

    fun selectOptionForQuestion(questionId: Int, optionIndex: Int) {
        _selectedOptionsMap[questionId] = optionIndex
    }

    fun getSelectedOptionForQuestion(questionId: Int): Int? {
        return _selectedOptionsMap[questionId]
    }

    fun checkAnswer(questionId: Int, selectedAnswer: String, correctAnswer: String) {
        val isCorrect = selectedAnswer == correctAnswer

        // Создание массива ответа на вопрос
        //orEmpty() — это метод, который, если значение value равно null,
        // вернет пустую неизменяемую карту. Это предотвращает возможные ошибки, связанные с null.
        //toMutableMap() Преобразует неизменяемую карту (которую мы получили от orEmpty())
        // в изменяемую (MutableMap), чтобы мы могли добавлять или изменять элементы.
        _answerResults.value = _answerResults.value.orEmpty().toMutableMap().apply {
            put(questionId, isCorrect)
        }


    }

    init {
        startTest()
    }


    private fun startTest() {
        generateTest(9)
    }

    private fun generateTest(countOfQuestions: Int) {
        viewModelScope.launch {
            _test.value = generateTestUseCase(countOfQuestions)
        }
    }

}