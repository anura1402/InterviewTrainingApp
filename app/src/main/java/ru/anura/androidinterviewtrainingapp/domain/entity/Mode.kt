package ru.anura.androidinterviewtrainingapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Mode: Parcelable {
    INTERVIEW, MISTAKES, FAVORITES
}