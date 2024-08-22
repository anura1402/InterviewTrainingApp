package ru.anura.androidinterviewtrainingapp.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
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
//    @TypeConverter
//    fun fromStringList(value: List<String>): String {
//        return Gson().toJson(value)
//    }
//
//    @TypeConverter
//    fun toStringList(value: String): List<String> {
//        val listType = object : TypeToken<List<String>>() {}.type
//        return Gson().fromJson(value, listType)
//    }


    @TypeConverter
    fun fromString(value: String?): List<String> {
        if (value.isNullOrEmpty()) return emptyList()

        return try {
            // Попытка разобрать как JSON массив
            val listType = object : TypeToken<List<String>>() {}.type
            val jsonArray = Gson().fromJson<List<String>>(value, listType)
            jsonArray
        } catch (e: JsonSyntaxException) {
            // Если не удалось разобрать как JSON массив, обрабатываем как простую строку
            listOf(value)
        }
    }

    @TypeConverter
    fun toString(list: List<String>?): String {
        return Gson().toJson(list)
    }
}