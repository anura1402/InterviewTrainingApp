package ru.anura.androidinterviewtrainingapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Theory(
    val id: Int,
    val name: String,
    val text: String,
    val theme: Theme
) : Parcelable