package ru.anura.androidinterviewtrainingapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
import ru.anura.androidinterviewtrainingapp.domain.usecases.GetTheoryListUseCase
import ru.anura.androidinterviewtrainingapp.domain.usecases.IsThemePassedUseCase
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    private val viewModelProviders: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
//    private val changeIsCorrectUseCase: ChangeIsCorrectUseCase,
//    private val changeIsFavUseCase: ChangeIsFavUseCase,
//
//    private val generateTestCurrentThemeUseCase: GenerateTestCurrentThemeUseCase,
//    private val generateTestUseCase: GenerateTestUseCase,
//    private val getTestWithWrongQUseCase: GetTestWithWrongQUseCase,
//    private val getTestWithFavQUseCase: GetTestWithFavQUseCase,
//    private val getCountOfQuestionsUseCase: GetCountOfQuestionsUseCase,
//    private val getCountOfQuestionsByCurrentThemeUseCase: GetCountOfQuestionsByCurrentThemeUseCase,
//
//    private val theme: Theme,
//
//    private val getCorrectAnsweredCountUseCase: GetCorrectAnsweredCountUseCase,
//    private val isThemePassedUseCase: IsThemePassedUseCase,
//    private val getTheoryListUseCase: GetTheoryListUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass == QuestionViewModel::class.java) {
//            return QuestionViewModel(
//                changeIsCorrectUseCase,
//                changeIsFavUseCase,
//                generateTestCurrentThemeUseCase,
//                generateTestUseCase,
//                getTestWithWrongQUseCase,
//                getTestWithFavQUseCase,
//                getCountOfQuestionsUseCase,
//                getCountOfQuestionsByCurrentThemeUseCase,
//                theme
//            ) as T
//        } else if (modelClass == WelcomeViewModel::class.java) {
//            return WelcomeViewModel(
//                getCountOfQuestionsUseCase,
//                getCorrectAnsweredCountUseCase,
//                isThemePassedUseCase
//            ) as T
//        } else if (modelClass == TheoryListViewModel::class.java) {
//            return TheoryListViewModel(
//                getTheoryListUseCase,
//                theme
//            ) as T
//        }
//        throw RuntimeException("Unknown view model class $modelClass")
        return viewModelProviders[modelClass]?.get() as T
    }
}