package ru.anura.androidinterviewtrainingapp.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.anura.androidinterviewtrainingapp.presentation.LearningFragment
import ru.anura.androidinterviewtrainingapp.presentation.QuestionFragment
import ru.anura.androidinterviewtrainingapp.presentation.TheoryFragment
import ru.anura.androidinterviewtrainingapp.presentation.WelcomeFragment

@Component(modules = [DataModule::class, ViewModelModule::class, ThemeModule::class])
@ApplicationScope
interface ApplicationComponent {

    fun inject(fragment: WelcomeFragment)
    fun inject(fragment: QuestionFragment)
    fun inject(fragment: LearningFragment)
    fun inject(fragment: TheoryFragment)
    //fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
            themeModule: ThemeModule
        ): ApplicationComponent
    }
}