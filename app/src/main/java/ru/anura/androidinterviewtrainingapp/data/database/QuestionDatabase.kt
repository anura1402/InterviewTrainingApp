package ru.anura.androidinterviewtrainingapp.data.database

import android.app.Application
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.anura.androidinterviewtrainingapp.data.database.questions.QuestionDBModel
import ru.anura.androidinterviewtrainingapp.data.database.questions.QuestionDao
import ru.anura.androidinterviewtrainingapp.data.database.themes.TheoryDBModel
import ru.anura.androidinterviewtrainingapp.data.database.themes.TheoryDao
import java.io.File

@Database(
    entities = [QuestionDBModel::class, TheoryDBModel::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class QuestionDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun theoryDao(): TheoryDao

    companion object {
        // Volatile annotation means any change to this field
        // are immediately visible to other threads.
        @Volatile
        private var INSTANCE: QuestionDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "question_database.db"

        private val MIGRATION_1_2 = object : Migration(1,2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "UPDATE questionTable\n" +
                            "SET isCorrectAnswer = 1;"
                )
            }
        }

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
                    .createFromAsset("new_question_database.db")
                    .addMigrations(MIGRATION_1_2)
                    //.fallbackToDestructiveMigration()
                    .build()
                Log.d("QuestionDatabase", "База данных успешно загружена. Версия базы данных: ${db.openHelper.readableDatabase.version}")
                INSTANCE = db
                return db
            }
        }
    }
}