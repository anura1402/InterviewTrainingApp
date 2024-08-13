package ru.anura.androidinterviewtrainingapp.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme

class QuestionViewModelFactory(
    private val theme: Theme,
private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestionViewModel::class.java)) {
            return QuestionViewModel(application, theme) as T
        }
        throw RuntimeException("Unknown view model class $modelClass")
    }

}