package ru.anura.androidinterviewtrainingapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Theme: Parcelable {
    JAVA,KOTLIN,SQL,ANDROID,BASE,THREADS,ALL
}