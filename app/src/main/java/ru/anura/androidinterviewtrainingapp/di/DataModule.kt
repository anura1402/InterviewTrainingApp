package ru.anura.androidinterviewtrainingapp.di

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.anura.androidinterviewtrainingapp.data.InterviewRepositoryImpl
import ru.anura.androidinterviewtrainingapp.data.TheoryRepositoryImpl
import ru.anura.androidinterviewtrainingapp.data.database.QuestionDatabase
import ru.anura.androidinterviewtrainingapp.data.database.questions.QuestionDao
import ru.anura.androidinterviewtrainingapp.data.database.themes.TheoryDao
import ru.anura.androidinterviewtrainingapp.domain.repository.InterviewRepository
import ru.anura.androidinterviewtrainingapp.domain.repository.TheoryRepository

@Module
interface DataModule {

    @ApplicationScope
    @Binds
     fun bindInterviewRepository(repository: InterviewRepositoryImpl): InterviewRepository

    @ApplicationScope
    @Binds
    fun bindTheoryRepository(repository: TheoryRepositoryImpl): TheoryRepository


    companion object{
        @Provides
        @ApplicationScope
        fun provideQuestionDao(application: Application): QuestionDao {
            return QuestionDatabase.getInstance(application).questionDao()
        }
        @Provides
        @ApplicationScope
        fun provideTheoryDao(application: Application): TheoryDao {
            return QuestionDatabase.getInstance(application).theoryDao()
        }
    }
}