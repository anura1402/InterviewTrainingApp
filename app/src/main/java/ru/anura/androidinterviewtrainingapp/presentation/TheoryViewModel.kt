package ru.anura.androidinterviewtrainingapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.anura.androidinterviewtrainingapp.data.TheoryRepositoryImpl
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme
import ru.anura.androidinterviewtrainingapp.domain.entity.Theory
import ru.anura.androidinterviewtrainingapp.domain.usecases.GetTheoryListUseCase
import javax.inject.Inject

class TheoryViewModel @Inject constructor(
    private val getTheoryListUseCase: GetTheoryListUseCase,
    private val theme: Theme
) : ViewModel() {

    private val _theoryList = MutableLiveData<List<Theory>>()
    val theoryList: LiveData<List<Theory>>
        get() = _theoryList

    private fun getTheoryList(theme: Theme) {
        viewModelScope.launch {
            _theoryList.value = getTheoryListUseCase(theme)
        }
    }

    init {
        fillTheoryWithTheme(theme)
    }

    private fun fillTheoryWithTheme(theme: Theme) {
        getTheoryList(theme)
    }
}