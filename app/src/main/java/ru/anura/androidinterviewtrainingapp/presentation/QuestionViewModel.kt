package ru.anura.androidinterviewtrainingapp.presentation

import android.app.Application
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



    init {
        startTest()
    }

    private fun startTest() {
        generateTest(2)
    }
    private fun generateTest(countOfQuestions: Int) {
        viewModelScope.launch {
            _test.value = generateTestUseCase(countOfQuestions)
        }
    }

}