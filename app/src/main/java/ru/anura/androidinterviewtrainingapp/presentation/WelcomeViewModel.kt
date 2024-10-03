package ru.anura.androidinterviewtrainingapp.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.anura.androidinterviewtrainingapp.data.InterviewRepositoryImpl
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme
import ru.anura.androidinterviewtrainingapp.domain.usecases.ChangeIsCorrectUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.ChangeIsFavUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GenerateTestCurrentThemeUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GenerateTestUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GetCorrectAnsweredCountUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GetCountOfQuestionsByCurrentThemeUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GetCountOfQuestionsUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GetTestWithFavQUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.GetTestWithWrongQUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.IsThemePassedUseCase
import javax.inject.Inject

class WelcomeViewModel @Inject constructor(
    private val getCountOfQuestionsUseCase: GetCountOfQuestionsUseCase,
    private val getCorrectAnsweredCountUseCase:GetCorrectAnsweredCountUseCase,
    private val isThemePassedUseCase:IsThemePassedUseCase
) : ViewModel(){
    private val _countOfQuestions = MutableLiveData<Int>()
    private val _correctAnsweredCount = MutableLiveData<Int>()
    private val _countOfPassedThemes = MutableLiveData<Int>()
    val countOfQuestions: LiveData<Int> get() = _countOfQuestions
    val correctAnsweredCount: LiveData<Int> get() = _correctAnsweredCount
    val countOfPassedThemes: LiveData<Int> get() = _countOfPassedThemes


    fun getCountOfQuestions() {
        viewModelScope.launch {
            val count = getCountOfQuestionsUseCase()
            _countOfQuestions.value = count
        }
    }
    fun getCorrectAnsweredCount() {
        viewModelScope.launch {
            val count = getCorrectAnsweredCountUseCase()
            _correctAnsweredCount.value = count
        }
    }

    fun getCountOfPassedThemes() {
        viewModelScope.launch {
            var count = 0
            if (isThemePassedUseCase(Theme.JAVA)) count++
            if (isThemePassedUseCase(Theme.ANDROID)) count++
            if (isThemePassedUseCase(Theme.KOTLIN)) count++
            if (isThemePassedUseCase(Theme.BASE)) count++
            if (isThemePassedUseCase(Theme.THREADS)) count++
            if (isThemePassedUseCase(Theme.SQL)) count++
            _countOfPassedThemes.value = count
        }
    }

}
