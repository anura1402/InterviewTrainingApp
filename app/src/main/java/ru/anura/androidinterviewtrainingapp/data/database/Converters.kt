package ru.anura.androidinterviewtrainingapp.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }
}