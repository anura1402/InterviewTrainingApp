package ru.anura.androidinterviewtrainingapp.data

import ru.anura.androidinterviewtrainingapp.data.database.QuestionDBModel
import ru.anura.androidinterviewtrainingapp.domain.entity.Question

class QuestionMapper {
    fun mapEntityToDbModel(question: Question) = QuestionDBModel(
        id = question.id,
        text = question.text,
        answer = question.answer,
        theme = question.theme,
        isCorrectAnswer = question.isCorrectAnswer,
        isFavorite = question.isFavorite
        )

    fun mapDbModelToEntity(questionDBModel: QuestionDBModel) = Question(
        id = questionDBModel.id,
        text = questionDBModel.text,
        answer = questionDBModel.answer,
        theme = questionDBModel.theme,
        isCorrectAnswer = questionDBModel.isCorrectAnswer,
        isFavorite = questionDBModel.isFavorite
    )


//    fun mapListDbModelToListEntity(list: List<ShopItemDBModel>) = list.map {
//        mapDbModelToEntity(it)
//    }
}