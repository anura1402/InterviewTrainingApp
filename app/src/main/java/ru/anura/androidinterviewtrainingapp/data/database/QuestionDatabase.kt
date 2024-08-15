package ru.anura.androidinterviewtrainingapp.data.database

import android.app.Application
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [QuestionDBModel::class], version = 2, exportSchema = false)
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

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Удаление всех данных из таблицы
                db.execSQL("DELETE FROM questionTable")

                // Вставка новых данных
                db.execSQL("""
            INSERT INTO questionTable (question_text, question_image, question_options, question_answer, question_theme, isCorrectAnswer, isFavorite)
            VALUES
            ('Какое значение по умолчанию у переменной типа boolean в Java?', '', '["true", "false"]', 'false', 'JAVA', 1, 0),
            ('Какое ключевое слово используется для создания экземпляра класса в Java?', '', '["create", "new", "instantiate"]', 'new', 'JAVA', 1, 0),
            ('В Kotlin, как объявить переменную, которую можно переназначить?', '', '["val", "var"]', 'var', 'KOTLIN', 1, 0),
            ('Какова видимость класса по умолчанию в Kotlin?', '', '["public", "private", "protected"]', 'public', 'KOTLIN', 1, 0),
            ('Какое SQL-ключевое слово используется для предотвращения дублирования записей в результирующем наборе?', '', '["DISTINCT", "UNIQUE"]', 'DISTINCT', 'SQL', 1, 0),
            ('Какая SQL-инструкция используется для обновления существующих записей в таблице?', '', '["UPDATE", "MODIFY", "SET"]', 'UPDATE', 'SQL', 1, 0),
            ('Какой основной компонент Android-приложения запускается на устройстве?', '', '["Activity", "Fragment", "Service"]', 'Activity', 'ANDROID', 1, 0),
            ('Какой метод в Activity используется для обработки ввода пользователя в Android?', '', '["onCreate", "onResume", "onClick"]', 'onClick', 'ANDROID', 1, 0),
            ('Какова основная цель первичного ключа в реляционной базе данных?', '', '["Уникальная идентификация записей", "Ограничения внешнего ключа"]', 'Уникальная идентификация записей', 'BASE', 1, 0),
            ('Что такое внешний ключ в базе данных?', '', '["Ключ, который уникально идентифицирует запись", "Ключ, который ссылается на другую таблицу"]', 'Ключ, который ссылается на другую таблицу', 'BASE', 1, 0),
            ('В многопоточности, что означает, когда поток ожидает выполнения определенного условия?', '', '["Ожидание", "Сон", "Блокировка"]', 'Блокировка', 'THREADS', 1, 0),
            ('Какой метод в Java используется для запуска нового потока?', '', '["start()", "run()", "execute()"]', 'start()', 'THREADS', 1, 0),
            ('Что делает ключевое слово "final", когда оно применяется к методу в Java?', '', '["Предотвращает переопределение метода", "Предотвращает перегрузку метода"]', 'Предотвращает переопределение метода', 'JAVA', 1, 0),
            ('Какова цель ключевого слова "super" в Java?', '', '["Доступ к методам и конструкторам суперкласса", "Доступ к методам подкласса"]', 'Доступ к методам и конструкторам суперкласса', 'JAVA', 1, 0),
            ('Какое главное преимущество Kotlin по сравнению с Java?', '', '["Безопасность от null", "Более многословный синтаксис"]', 'Безопасность от null', 'KOTLIN', 1, 0),
            ('Как в Kotlin обрабатываются значения null?', '', '["Использование безопасных вызовов (?.)", "Использование блоков try-catch"]', 'Использование безопасных вызовов (?.)', 'KOTLIN', 1, 0),
            ('Какая SQL-клауза используется для сортировки результата запроса?', '', '["ORDER BY", "SORT BY"]', 'ORDER BY', 'SQL', 1, 0),
            ('Что возвращает SQL-запрос `SELECT COUNT(*) FROM table_name;`?', '', '["Общее количество строк", "Общее количество столбцов"]', 'Общее количество строк', 'SQL', 1, 0),
            ('Что такое Intent в Android?', '', '["Связь между компонентами", "Доступ к сетевым ресурсам"]', 'Связь между компонентами', 'ANDROID', 1, 0),
            ('Что делает метод `onCreate` в Activity Android?', '', '["Инициализация компонентов активности", "Обработка ввода пользователя"]', 'Инициализация компонентов активности', 'ANDROID', 1, 0)
        """)
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
                    .addMigrations(MIGRATION_1_2)
                    .createFromAsset("databases/question_database.db")
                    .build()
                Log.d("QuestionDatabase", "База данных успешно загружена.")
                INSTANCE = db
                return db
            }
        }
    }
}