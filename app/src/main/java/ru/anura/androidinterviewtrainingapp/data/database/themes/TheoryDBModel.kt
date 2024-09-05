package ru.anura.androidinterviewtrainingapp.data.database.themes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme

@Entity(tableName = "theoryTable")
data class TheoryDBModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "theory_id")
    val id: Int,
    @ColumnInfo(name = "theory_name")
    val name: String,
    @ColumnInfo(name = "theory_text")
    val text: String,
    @ColumnInfo(name = "theory_theme")
    val theme: Theme
)