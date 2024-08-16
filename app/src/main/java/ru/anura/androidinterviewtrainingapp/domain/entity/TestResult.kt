package ru.anura.androidinterviewtrainingapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TestResult(
    val countOfRightAnswers:Int,
    val countOfQuestions:Int
) : Parcelable