package ru.anura.androidinterviewtrainingapp.data.database.questions

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme

@Dao
interface QuestionDao {
    // adds a new entry to our database.
    // if some data is same/conflict, it'll be replace with new data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question : QuestionDBModel)

    // deletes a question
    @Delete
    suspend fun deleteQuestion(question : QuestionDBModel)

    // updates a question.
    @Update
    suspend fun updateQuestion(question : QuestionDBModel)

    // read all the questions from questionTable
    // and arrange questions in ascending order
    // of their ids
    @Query("Select * from questionTable order by question_id ASC")
    fun getAllQuestion(): LiveData<List<QuestionDBModel>>
    // why not use suspend ? because Room does not support LiveData with suspended functions.
    // LiveData already works on a background thread and should be used directly without using coroutines

    @Query("Select * from questionTable WHERE question_id = :id")
    fun getQuestionById(id:Int): QuestionDBModel

    // delete all questions
    @Query("DELETE FROM questionTable")
    suspend fun clearQuestion()

    //you can use this too, to delete a question by id.
    @Query("DELETE FROM questionTable WHERE question_id = :id")
    suspend fun deleteQuestionById(id: Int)


    @Query("UPDATE questionTable SET isFavorite = :isFav WHERE question_id = :id")
    suspend fun changeIsFav(id: Int, isFav:Boolean)
    @Query("UPDATE questionTable SET isCorrectAnswer = :isCorrect WHERE question_id = :id")
    suspend fun changeIsCorrect(id: Int,isCorrect:Boolean)

    @Query("SELECT * FROM questionTable WHERE question_theme IN (:themes) ORDER BY RANDOM() LIMIT :num")
     suspend fun getQuestions(themes: List<String>,num:Int): List<QuestionDBModel>

    @Query("SELECT * FROM questionTable WHERE isCorrectAnswer = 0")
    suspend fun getWrongQuestions(): List<QuestionDBModel>
    @Query("SELECT * FROM questionTable WHERE isFavorite = 1")
    suspend fun getFavQuestions(): List<QuestionDBModel>

    @Query("SELECT COUNT(*) FROM questionTable")
    suspend fun getCountOfQuestions(): Int
    @Query("SELECT COUNT(*) FROM questionTable WHERE question_theme = :theme")
    suspend fun getCountOfQuestionsByCurrentTheme(theme: String): Int
}