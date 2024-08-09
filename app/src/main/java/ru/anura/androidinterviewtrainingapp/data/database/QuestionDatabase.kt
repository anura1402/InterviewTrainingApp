package ru.anura.androidinterviewtrainingapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [QuestionDBModel::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class QuestionDatabase: RoomDatabase() {
    abstract fun getQuestionDao(): QuestionDao

    companion object {
        // Volatile annotation means any change to this field
        // are immediately visible to other threads.
        @Volatile
        private var INSTANCE: QuestionDatabase? = null

        private const val DB_NAME = "question_database.db"

        fun getDatabase(context: Context): QuestionDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            // here synchronised used for blocking the other thread
            // from accessing another while in a specific code execution.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuestionDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}