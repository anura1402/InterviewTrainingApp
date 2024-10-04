package ru.anura.androidinterviewtrainingapp.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme
import ru.anura.androidinterviewtrainingapp.domain.entity.Theory
import ru.anura.androidinterviewtrainingapp.domain.usecases.GetTheoryListUseCase
import javax.inject.Inject

class TheoryListViewModel @Inject constructor(
    private val getTheoryListUseCase: GetTheoryListUseCase,
    private val theme: Theme
) : ViewModel() {

    private val _theoryList = MutableLiveData<List<Theory>>()
    val theoryList: LiveData<List<Theory>>
        get() = _theoryList

    private fun getTheoryList(theme: Theme) {
        viewModelScope.launch {
            Log.d("Dagger", "THEORY_LIST_FRAGMENT theme: $theme ")
            _theoryList.value = getTheoryListUseCase(theme)
        }
    }

    init {
        Log.d("Dagger", "Received theme: $theme")
        fillTheoryWithTheme(theme)
    }

    private fun fillTheoryWithTheme(theme: Theme) {
        getTheoryList(theme)
    }
}