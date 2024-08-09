package ru.anura.androidinterviewtrainingapp.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "testTable")
data class TestDBModel (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "test_id")
    val id: Int?,
    @ColumnInfo(name = "question_id")
    val questionId: Int?
)