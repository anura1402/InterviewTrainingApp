package ru.anura.androidinterviewtrainingapp.data.database

import android.app.Application
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [QuestionDBModel::class], version =2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class QuestionDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao

    companion object {
        // Volatile annotation means any change to this field
        // are immediately visible to other threads.
        @Volatile
        private var INSTANCE: QuestionDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "question_database.db"

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DELETE FROM questionTable")

                db.execSQL(
                    """
                    INSERT INTO questionTable (
                      question_text, question_image, question_options, question_answer, question_theme, isCorrectAnswer, isFavorite, question_explanation
                    ) VALUES
                   ('Какие ключевые особенности языка Java?', '', '["Объектно-Ориентированное Программирование", "Платформенная независимость", "Автоматическое управление памятью", "Низкоуровневое программирование"]', '["Объектно-Ориентированное Программирование", "Платформенная независимость", "Автоматическое управление памятью"]', 'JAVA', 1, 0, 'Java поддерживает ООП, платформенную независимость и автоматическое управление памятью через сборщик мусора.'),
                   ('Что такое корутины в Kotlin?', '', '["Механизм асинхронного программирования", "Тип данных", "Интерфейс", "Класс"]', '["Механизм асинхронного программирования"]', 'KOTLIN', 1, 0, 'Корутины в Kotlin позволяют писать асинхронный код более простым и понятным способом.'),
                    ('Что такое SQL JOIN?', '', '["Операция объединения данных из нескольких таблиц", "Команда для создания таблицы", "Функция для фильтрации данных", "Механизм для сортировки данных"]', '["Операция объединения данных из нескольких таблиц"]', 'SQL', 1, 0, 'JOIN используется для объединения данных из нескольких таблиц по определённым условиям.'),
                   ('Какой компонент Android отвечает за отображение пользовательского интерфейса?', '', '["Activity", "Service", "Broadcast Receiver", "Content Provider"]', '["Activity"]', 'ANDROID', 1, 0, 'Activity управляет пользовательским интерфейсом в Android приложениях.'),
                    ('Что такое база данных в контексте SQL?', '', '["Структурированный набор данных", "Функция для обработки данных", "Тип данных", "Механизм для потоковой передачи данных"]', '["Структурированный набор данных"]', 'BASE', 1, 0, 'База данных — это структурированный набор данных, который можно эффективно управлять и извлекать.'),
                   ('Что такое многопоточность в программировании?', '', '["Способ выполнения нескольких задач одновременно", "Метод оптимизации кода", "Инструмент для работы с базами данных", "Функция для управления памятью"]', '["Способ выполнения нескольких задач одновременно"]', 'THREADS', 1, 0, 'Многопоточность позволяет выполнять несколько потоков выполнения одновременно, что может улучшить производительность приложения.'),
                  ('Какая структура данных используется для хранения пар ключ-значение в Kotlin?', '', '["Map", "List", "Set", "Array"]', '["Map"]', 'KOTLIN', 1, 0, 'Map в Kotlin используется для хранения пар ключ-значение, позволяя эффективно искать значения по ключам.'),
                   ('Какой тип базы данных поддерживает SQL?', '', '["Реляционные базы данных", "Документные базы данных", "Графовые базы данных", "Ключ-значение базы данных"]', '["Реляционные базы данных"]', 'SQL', 1, 0, 'SQL используется для работы с реляционными базами данных, которые хранят данные в таблицах с фиксированной схемой.'),
                   ('Какой класс в Android используется для хранения данных в фоновом режиме?', '', '["Service", "Activity", "Broadcast Receiver", "Content Provider"]', '["Service"]', 'ANDROID', 1, 0, 'Service позволяет выполнять долгосрочные операции в фоновом режиме, не привязываясь к пользовательскому интерфейсу.'),
                   ('Что такое аннотации в Java?', '', '["Метаданные, которые можно добавлять к коду", "Специальные переменные", "Типы данных", "Методы для обработки ошибок"]', '["Метаданные, которые можно добавлять к коду"]', 'JAVA', 1, 0, 'Аннотации в Java используются для добавления метаданных к коду, которые могут использоваться на этапе компиляции или выполнения.'),
                ('Что такое объектно-ориентированное программирование?', '', '["Парадигма программирования, основанная на использовании объектов", "Метод структурного программирования", "Функциональный стиль программирования", "Подход к оптимизации кода"]', '["Парадигма программирования, основанная на использовании объектов"]', 'BASE', 1, 0, 'ООП — это парадигма программирования, основанная на концепциях объектов и классов, которые инкапсулируют состояние и поведение.')
        """
                )
                db.execSQL(
                    "UPDATE questionTable\n" +
                            "SET isCorrectAnswer = 0;"
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
                    //.fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_1_2)
                    .createFromAsset("databases/new_question_database.db")
                    .build()
                Log.d("QuestionDatabase", "База данных успешно загружена.")
                //Log.d("QuestionDatabase", "База данных: ${db.}")
                INSTANCE = db
                return db
            }
        }

    }

}