package ru.anura.androidinterviewtrainingapp.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import ru.anura.androidinterviewtrainingapp.data.InterviewRepositoryImpl
import ru.anura.androidinterviewtrainingapp.domain.entity.Test
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme
import ru.anura.androidinterviewtrainingapp.domain.usecases.ChangeIsCorrectUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.ChangeIsFavUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GenerateTestCurrentThemeUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GenerateTestUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GetQuestionByIdUseCase
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.anura.androidinterviewtrainingapp.domain.entity.Mode
import ru.anura.androidinterviewtrainingapp.domain.entity.TestResult
import ru.anura.androidinterviewtrainingapp.domain.usecases.GetCountOfQuestionsUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GetTestWithFavQUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GetTestWithWrongQUseCase

class QuestionViewModel(
    private val application: Application,
    private val theme: Theme
) : AndroidViewModel(application) {
    private val repository = InterviewRepositoryImpl(application)

    private val changeIsCorrectUseCase = ChangeIsCorrectUseCase(repository)
    private val changeIsFavUseCase = ChangeIsFavUseCase(repository)

    private val generateTestCurrentThemeUseCase = GenerateTestCurrentThemeUseCase(repository)
    private val generateTestUseCase = GenerateTestUseCase(repository)
    private val getTestWithWrongQUseCase = GetTestWithWrongQUseCase(repository)
    private val getTestWithFavQUseCase = GetTestWithFavQUseCase(repository)
    private val getCountOfQuestionsUseCase = GetCountOfQuestionsUseCase(repository)

    private var countOfRightAnswers = 0
    private var countOfQuestions = 0
    private val allCountOfQuestions by lazy {
        viewModelScope.launch {
            getCountOfQuestionsUseCase()
        }
    }
    private var _test = MutableLiveData<Test>()
    val test: LiveData<Test>
        get() = _test

    private val _answerResults = MutableLiveData<Map<Int, Boolean>>()
    val answerResults: LiveData<Map<Int, Boolean>>
        get() = _answerResults

    private val _favList = MutableLiveData<Map<Int, Boolean>>()
    val favList: LiveData<Map<Int, Boolean>>
        get() = _favList

    private val _selectedOptionsMap = mutableMapOf<Int, Int>()
    private val _isOptionSelectedMap = mutableMapOf<Int, Boolean>()

    private var _testResult = MutableLiveData<TestResult>()
    val testResult: LiveData<TestResult>
        get() = _testResult

    private val _clickedTextViewId = MutableLiveData<Int>()
    val clickedTextViewId: LiveData<Int> get() = _clickedTextViewId

    private val _clickedButtonOnQuestionId = MutableLiveData<Int>()
    val clickedButtonOnQuestionId: LiveData<Int> get() = _clickedButtonOnQuestionId

    private val _clickedFavTextView = MutableLiveData<Int>()
    val clickedFavTextView: LiveData<Int> get() = _clickedFavTextView

    private val questionIdsFromDB = mutableListOf<Int>()


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

    fun onTextViewClicked(index: Int) {
        _clickedTextViewId.value = index
    }

    fun onButtonClicked(index: Int) {
        _clickedButtonOnQuestionId.value = index
    }

    fun isFavTextViewClicked(index: Int) {
        _clickedFavTextView.value = index
    }

    fun changeFavList(questionId: Int, isFav: Boolean) {
        viewModelScope.launch {
            changeIsFavUseCase(questionIdsFromDB[questionId], isFav)
        }
        if (_clickedFavTextView.value == questionId) {
            if (_favList.value?.get(questionId) == true) {
                _favList.value = _favList.value.orEmpty().toMutableMap().apply {
                    put(questionId, false)
                }
            } else{
                _favList.value = _favList.value.orEmpty().toMutableMap().apply {
                    put(questionId, true)
                }
            }
        }

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
        if (isCorrect) {
            countOfRightAnswers++
            changeIsCorrect(questionId, true)
        } else {
            changeIsCorrect(questionId, false)
        }
    }

    private fun changeIsCorrect(questionId: Int, isCorrect: Boolean) {
        viewModelScope.launch {
            changeIsCorrectUseCase(questionIdsFromDB[questionId], isCorrect)
        }
    }


    fun finishTest() {
        _testResult.value = TestResult(
            countOfRightAnswers,
            test.value?.countOfQuestions ?: 0
        )

    }

    fun startTest(mode: Mode) {
        viewModelScope.launch {
            generateTest(2, mode)
        }
    }

    private suspend fun generateTest(countOfQuestions: Int, mode: Mode) {
        this.countOfQuestions = countOfQuestions
        val job = viewModelScope.launch {
            _test.value = when (mode) {
                Mode.INTERVIEW -> generateTestUseCase(countOfQuestions)
                Mode.MISTAKES -> getTestWithWrongQUseCase()
                Mode.FAVORITES -> getTestWithFavQUseCase()
            }
        }
        job.join()
        _test.value?.let { testValue ->
            for (i in 0 until testValue.countOfQuestions) {
                questionIdsFromDB.add(testValue.questions[i].id)
                if (testValue.questions[i].isFavorite) {
                    _favList.value = _favList.value.orEmpty().toMutableMap().apply {
                        put(i, true)
                    }
                } else
                    _favList.value = _favList.value.orEmpty().toMutableMap().apply {
                        put(i, false)
                    }
            }
        }
    }
}