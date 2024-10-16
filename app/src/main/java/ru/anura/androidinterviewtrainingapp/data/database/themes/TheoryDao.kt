package ru.anura.androidinterviewtrainingapp.data.database.themes

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme

@Dao
interface TheoryDao {
    @Query("SELECT * FROM theoryTable WHERE theory_theme = :theme")
    suspend fun getTheoryList(theme: String): List<TheoryDBModel>

}