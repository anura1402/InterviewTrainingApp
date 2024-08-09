package ru.anura.androidinterviewtrainingapp.data.database

import androidx.room.TypeConverter
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme

class Converters {
    @TypeConverter
    fun fromTheme(theme: Theme): String {
        return theme.name
    }

    @TypeConverter
    fun toTheme(theme: String): Theme {
        return Theme.valueOf(theme)
    }
}