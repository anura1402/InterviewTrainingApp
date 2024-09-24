package ru.anura.androidinterviewtrainingapp.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.anura.androidinterviewtrainingapp.data.InterviewRepositoryImpl
import ru.anura.androidinterviewtrainingapp.domain.usecases.ChangeIsCorrectUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.ChangeIsFavUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GenerateTestCurrentThemeUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GenerateTestUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GetCountOfQuestionsByCurrentThemeUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GetCountOfQuestionsUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GetTestWithFavQUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GetTestWithWrongQUseCase

class WelcomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = InterviewRepositoryImpl(application)
    private val getCountOfQuestionsUseCase = GetCountOfQuestionsUseCase(repository)
    private val _countOfQuestions = MutableLiveData<Int>()
    val countOfQuestions: LiveData<Int> get() = _countOfQuestions



     fun getCountOfQuestions() {
        viewModelScope.launch {
            val count = getCountOfQuestionsUseCase()
            _countOfQuestions.value = count
        }
    }

}
