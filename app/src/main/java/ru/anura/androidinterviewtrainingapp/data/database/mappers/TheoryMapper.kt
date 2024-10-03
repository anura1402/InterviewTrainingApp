package ru.anura.androidinterviewtrainingapp.data.database.mappers

import ru.anura.androidinterviewtrainingapp.data.database.questions.QuestionDBModel
import ru.anura.androidinterviewtrainingapp.data.database.themes.TheoryDBModel
import ru.anura.androidinterviewtrainingapp.domain.entity.Question
import ru.anura.androidinterviewtrainingapp.domain.entity.Theory
import javax.inject.Inject

class TheoryMapper @Inject constructor(){
    fun mapEntityToDbModel(theory: Theory) = TheoryDBModel(
        id = theory.id,
        text = theory.text,
        name = theory.name,
        theme = theory.theme
        )

    fun mapDbModelToEntity(theoryDBModel: TheoryDBModel) = Theory(
        id = theoryDBModel.id,
        text = theoryDBModel.text,
        name = theoryDBModel.name,
        theme = theoryDBModel.theme
    )

    fun mapListDbModelToListEntity(list: List<TheoryDBModel>) = list.map {
        mapDbModelToEntity(it)
    }


//    fun mapListDbModelToListEntity(list: List<ShopItemDBModel>) = list.map {
//        mapDbModelToEntity(it)
//    }
}