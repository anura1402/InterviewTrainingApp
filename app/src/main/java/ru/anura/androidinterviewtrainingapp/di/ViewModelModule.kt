package ru.anura.androidinterviewtrainingapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.anura.androidinterviewtrainingapp.presentation.QuestionViewModel
import ru.anura.androidinterviewtrainingapp.presentation.ViewModelFactory
import ru.anura.androidinterviewtrainingapp.presentation.TheoryListViewModel
import ru.anura.androidinterviewtrainingapp.presentation.WelcomeViewModel

@Module
interface ViewModelModule {

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
    @ViewModelKey(TheoryListViewModel::class)
    fun bindTheoryViewModel(viewModel: TheoryListViewModel): ViewModel

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}