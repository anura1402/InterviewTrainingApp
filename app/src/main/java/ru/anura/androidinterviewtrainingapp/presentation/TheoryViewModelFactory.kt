package ru.anura.androidinterviewtrainingapp.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme

//class TheoryViewModelFactory(
//    private val theme: Theme,
//private val application: Application
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(TheoryViewModel::class.java)) {
//            return TheoryViewModel(application, theme) as T
//        }
//        throw RuntimeException("Unknown view model class $modelClass")
//    }
//
//}