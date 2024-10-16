package ru.anura.androidinterviewtrainingapp.domain.repository

import ru.anura.androidinterviewtrainingapp.domain.entity.Theme
import ru.anura.androidinterviewtrainingapp.domain.entity.Theory

interface TheoryRepository {

    suspend fun getTheoryList(theme: Theme): List<Theory>
}