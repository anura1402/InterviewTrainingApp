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
import ru.anura.androidinterviewtrainingapp.domain.entity.TestResult

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

    private var countOfRightAnswers = 0
    private var countOfQuestions = 0
    private var _test = MutableLiveData<Test>()
    val test: LiveData<Test>
        get() = _test

    private val _answeredQuestions = MutableLiveData<MutableList<Boolean>>().apply {
        value = mutableListOf()
    }

    private val _answerResults = MutableLiveData<Map<Int, Boolean>>()
    val answerResults: LiveData<Map<Int, Boolean>>
        get() = _answerResults


    private val _selectedOptionsMap = mutableMapOf<Int, Int>()
    val selectedOptionsMap: Map<Int, Int>
        get() = _selectedOptionsMap

    private val _isOptionSelectedMap = mutableMapOf<Int, Boolean>()
    val isOptionSelectedMap: Map<Int, Boolean>
        get() = _isOptionSelectedMap

    private var _testResult = MutableLiveData<TestResult>()
    val testResult: LiveData<TestResult>
        get() = _testResult

    private var _enoughCountOfRightAnswers = MutableLiveData<Boolean>()
    val enoughCountOfRightAnswers: LiveData<Boolean>
        get() = _enoughCountOfRightAnswers


    fun selectOptionForQuestion(questionId: Int, optionIndex: Int) {
        _selectedOptionsMap[questionId] = optionIndex
        _isOptionSelectedMap[questionId] = true
    }

    fun getSelectedOptionForQuestion(questionId: Int): Int? {
        return _selectedOptionsMap[questionId]
    }

    fun isOptionSelectedForQuestion(questionId: Int): Boolean {
        return _isOptionSelectedMap[questionId] ?: false
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
        if (isCorrect)
            countOfRightAnswers++

    }

    init {
        startTest()
    }

    fun finishTest() {
        _testResult.value = TestResult(
            countOfRightAnswers,
            countOfQuestions
        )

    }

    private fun startTest() {
        generateTest(20)

    }

    private fun generateTest(countOfQuestions: Int) {
        this.countOfQuestions = countOfQuestions
        viewModelScope.launch {
            _test.value = generateTestUseCase(countOfQuestions)
        }
        //_enoughCountOfRightAnswers.value = countOfRightAnswers >= (countOfQuestions*0.85)
    }

}