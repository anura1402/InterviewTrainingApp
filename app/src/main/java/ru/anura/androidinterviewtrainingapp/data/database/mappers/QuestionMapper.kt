package ru.anura.androidinterviewtrainingapp.data.database.mappers

import ru.anura.androidinterviewtrainingapp.data.database.questions.QuestionDBModel
import ru.anura.androidinterviewtrainingapp.domain.entity.Question
import javax.inject.Inject

class QuestionMapper @Inject constructor() {
    fun mapEntityToDbModel(question: Question) = QuestionDBModel(
        id = question.id,
        text = question.text,
        image = question.image,
        options = question.options,
        answer = question.answer,
        theme = question.theme,
        isCorrectAnswer = question.isCorrectAnswer,
        isFavorite = question.isFavorite,
        explanation = question.explanation
        )

    fun mapDbModelToEntity(questionDBModel: QuestionDBModel) = Question(
        id = questionDBModel.id,
        text = questionDBModel.text,
        image = questionDBModel.image,
        options = questionDBModel.options,
        answer = questionDBModel.answer,
        theme = questionDBModel.theme,
        isCorrectAnswer = questionDBModel.isCorrectAnswer,
        isFavorite = questionDBModel.isFavorite,
        explanation = questionDBModel.explanation
    )

    fun mapListDbModelToListEntity(list: List<QuestionDBModel>) = list.map {
        mapDbModelToEntity(it)
    }


//    fun mapListDbModelToListEntity(list: List<ShopItemDBModel>) = list.map {
//        mapDbModelToEntity(it)
//    }
}