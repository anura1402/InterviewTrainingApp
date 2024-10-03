package ru.anura.androidinterviewtrainingapp.data

import android.app.Application
import ru.anura.androidinterviewtrainingapp.data.database.QuestionDatabase
import ru.anura.androidinterviewtrainingapp.data.database.mappers.TheoryMapper
import ru.anura.androidinterviewtrainingapp.data.database.themes.TheoryDao
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme
import ru.anura.androidinterviewtrainingapp.domain.entity.Theory
import ru.anura.androidinterviewtrainingapp.domain.repository.TheoryRepository
import javax.inject.Inject

class TheoryRepositoryImpl @Inject constructor(
    private val theoryDao: TheoryDao, //= QuestionDatabase.getInstance(application).theoryDao()
    private val mapper: TheoryMapper //= TheoryMapper()
) : TheoryRepository {

    override suspend fun getTheoryList(theme: Theme): List<Theory> {
        return mapper.mapListDbModelToListEntity(theoryDao.getTheoryList(theme.toString()))
        //return mapper.mapListDbModelToListEntity(theoryDao.getTheoryList())
    }
}