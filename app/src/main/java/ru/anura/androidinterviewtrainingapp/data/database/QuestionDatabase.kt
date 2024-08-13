package ru.anura.androidinterviewtrainingapp.data.database

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [QuestionDBModel::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class QuestionDatabase: RoomDatabase() {
    abstract fun questionDao(): QuestionDao

    companion object {
        // Volatile annotation means any change to this field
        // are immediately visible to other threads.
        @Volatile
        private var INSTANCE: QuestionDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "question_database.db"

        fun getInstance(application: Application): QuestionDatabase {
            //Если не null, то сразу возвращаем значение
            INSTANCE?.let {
                return it
            }
            //double-check для нескольких потоков, что бы не меняли на разные значение
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                //если null, то
                val db = Room.databaseBuilder(
                    application,
                    QuestionDatabase::class.java,
                    DB_NAME
                )
                    .createFromAsset("databases/question_database.db")
                    .build()
                Log.d("QuestionDatabase", "База данных успешно загружена.")
                INSTANCE = db
                return db
            }
        }
    }
}