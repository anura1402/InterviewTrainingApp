package ru.anura.androidinterviewtrainingapp.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.anura.androidinterviewtrainingapp.domain.entity.Test
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme
import ru.anura.androidinterviewtrainingapp.domain.usecases.ChangeIsCorrectUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.ChangeIsFavUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GenerateTestCurrentThemeUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GenerateTestUseCase
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.anura.androidinterviewtrainingapp.domain.entity.Mode
import ru.anura.androidinterviewtrainingapp.domain.entity.TestResult
import ru.anura.androidinterviewtrainingapp.domain.usecases.GetCountOfQuestionsByCurrentThemeUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GetCountOfQuestionsUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GetTestWithFavQUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GetTestWithWrongQUseCase
import javax.inject.Inject

class QuestionViewModel @Inject constructor(
    private val changeIsCorrectUseCase: ChangeIsCorrectUseCase,
    private val changeIsFavUseCase: ChangeIsFavUseCase,

    private val generateTestCurrentThemeUseCase: GenerateTestCurrentThemeUseCase,
    private val generateTestUseCase: GenerateTestUseCase,
    private val getTestWithWrongQUseCase: GetTestWithWrongQUseCase,
    private val getTestWithFavQUseCase: GetTestWithFavQUseCase,
    private val getCountOfQuestionsUseCase: GetCountOfQuestionsUseCase,
    private val getCountOfQuestionsByCurrentThemeUseCase: GetCountOfQuestionsByCurrentThemeUseCase,

    private val theme: Theme
) : ViewModel() {

    private var countOfRightAnswers = 0
    private var allCountOfQuestions = 0
    private var _test = MutableLiveData<Test>()
    val test: LiveData<Test>
        get() = _test

    private val _answerResults = MutableLiveData<LinkedHashMap<Int, Boolean>>()
    val answerResults: LiveData<LinkedHashMap<Int, Boolean>>
        get() = _answerResults

    private val _favList = MutableLiveData<Map<Int, Boolean>>()
    val favList: LiveData<Map<Int, Boolean>>
        get() = _favList

    private val _selectedOptionsMap = mutableMapOf<Int, Set<Int>>()
    private val _resultForOptions = mutableMapOf<Int, List<Boolean>>()
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
    private val _explanation = MutableLiveData<String>()
    val explanation: LiveData<String> get() = _explanation

    private val questionIdsFromDB = mutableListOf<Int>()
    private val selectedAnswers = mutableListOf<String>()
    private var isCorrectTotal: Boolean = true

    fun selectOptionForQuestion(questionId: Int, optionIndex: Int) {
        val currentValues = _selectedOptionsMap[questionId] ?: setOf()
        val updatedValues = currentValues + optionIndex
        _selectedOptionsMap[questionId] = updatedValues
        _isOptionSelectedMap[questionId] = true
    }

    fun getSelectedOptionForQuestion(questionId: Int): Set<Int>? {
        return _selectedOptionsMap[questionId]
    }

    fun getResultForOptions(questionId: Int): List<Boolean>? {
        return _resultForOptions[questionId]
    }

    fun isOptionSelectedForQuestion(questionId: Int): Boolean {
        return _isOptionSelectedMap[questionId] ?: false
    }

    fun onTextViewClicked(index: Int) {
        _clickedTextViewId.value = index
    }

    fun onButtonClicked(index: Int) {
        checkIfAnswerWasFull(index)
        _clickedButtonOnQuestionId.value = index

    }

    private suspend fun getCountOfQuestions(theme: Theme): Int {
        return getCountOfQuestionsByCurrentThemeUseCase(theme)
    }


    private fun checkIfAnswerWasFull(index: Int) {
        if (!isCorrectTotal && (_answerResults.value?.entries?.last()?.key ?: -1) == index) {
            Log.d("ResultAnswer", "YES")
            _answerResults.value = (_answerResults.value ?: LinkedHashMap()).apply {
                put(index, isCorrectTotal)
            }
            changeIsCorrectInDB(index, isCorrectTotal)
            setExplanation(index)
        }
    }

    fun setExplanation(index: Int) {
        _explanation.value = _test.value?.questions?.get(index)?.explanation
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
            } else {
                _favList.value = _favList.value.orEmpty().toMutableMap().apply {
                    put(questionId, true)
                }
            }
        }

    }

    fun checkAnswer(questionId: Int, selectedAnswer: String, correctAnswer: List<String>) {
        val isCorrect = correctAnswer.contains(selectedAnswer)
        if (_answerResults.value?.containsKey(questionId) == false)
            selectedAnswers.clear()
        selectedAnswers.add(selectedAnswer)
        isCorrectTotal = selectedAnswers.toSet() == correctAnswer.toSet()
        // Создание массива ответа на вопрос
        //orEmpty() — это метод, который, если значение value равно null,
        // вернет пустую неизменяемую карту. Это предотвращает возможные ошибки, связанные с null.
        //toMutableMap() Преобразует неизменяемую карту (которую мы получили от orEmpty())
        // в изменяемую (MutableMap), чтобы мы могли добавлять или изменять элементы.
        _answerResults.value = (_answerResults.value ?: LinkedHashMap()).apply {
            put(questionId, isCorrect)
        }
        addIsCorrectToResult(questionId, isCorrect)
        if (isCorrectTotal) {
            countOfRightAnswers++
            changeIsCorrectInDB(questionId, true)
        }
        if (!isCorrect) {
            changeIsCorrectInDB(questionId, false)
            setExplanation(questionId)
        }
    }

    private fun addIsCorrectToResult(questionId: Int, isCorrect: Boolean) {
        val currentValues = _resultForOptions[questionId] ?: listOf()
        val updatedValues = currentValues + isCorrect
        _resultForOptions[questionId] = updatedValues
    }

    private fun changeIsCorrectInDB(questionId: Int, isCorrect: Boolean) {
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
            generateTest(mode)
        }
    }

    private suspend fun generateTest(mode: Mode) {
        allCountOfQuestions = getCountOfQuestionsUseCase()
        val job = viewModelScope.launch {
            _test.value = when (mode) {
                Mode.INTERVIEW -> generateTestUseCase(NUMBER_FOR_INTERVIEW)
                Mode.MISTAKES -> getTestWithWrongQUseCase()
                Mode.FAVORITES -> getTestWithFavQUseCase()
                Mode.MARATHON -> generateTestUseCase(allCountOfQuestions)
                Mode.THEMES -> generateTestCurrentThemeUseCase(theme, getCountOfQuestions(theme))
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

    companion object {
        private const val NUMBER_FOR_INTERVIEW = 20

    }
}
