package ru.anura.androidinterviewtrainingapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.anura.androidinterviewtrainingapp.presentation.QuestionViewModel
import ru.anura.androidinterviewtrainingapp.presentation.QuestionViewModelFactory
import ru.anura.androidinterviewtrainingapp.presentation.TheoryViewModel
import ru.anura.androidinterviewtrainingapp.presentation.WelcomeViewModel

@Module
interface ViewModelModule {
//    @Binds
//    @IntoMap
//    @ViewModelKey(QuestionViewModel::class)
//    fun bindQuestionViewModel(questionViewModel: QuestionViewModel): ViewModel

//    @Binds
//    @IntoMap
//    @ViewModelKey(QuestionViewModel::class)
//    fun bindQuestionViewModel(questionViewModel: QuestionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(QuestionViewModel::class)
    fun bindQuestionViewModel(viewModel: QuestionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WelcomeViewModel::class)
    fun bindWelcomeViewModel(viewModel: WelcomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TheoryViewModel::class)
    fun bindTheoryViewModel(viewModel: TheoryViewModel): ViewModel

    @Binds
    fun bindViewModelFactory(factory: QuestionViewModelFactory): ViewModelProvider.Factory
//    @Binds
//    fun bindQuestionViewModelFactory(
//        factory: QuestionViewModelFactory
//    ): QuestionViewModelFactory
}