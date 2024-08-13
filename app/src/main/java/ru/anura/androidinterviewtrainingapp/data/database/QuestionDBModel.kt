package ru.anura.androidinterviewtrainingapp.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme

@Entity(tableName = "questionTable")
data class QuestionDBModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "question_id")
    val id: Int,
    @ColumnInfo(name = "question_text")
    val text: String,
    @ColumnInfo(name = "question_image")
    val image: String,
    @ColumnInfo(name = "question_options")
    val options: List<String>,
    @ColumnInfo(name = "question_answer")
    val answer: String,
    @ColumnInfo(name = "question_theme")
    val theme: Theme,
    @ColumnInfo(name = "isCorrectAnswer")
    val isCorrectAnswer: Boolean,
    @ColumnInfo(name = "isFavorite")
    val isFavorite: Boolean
)