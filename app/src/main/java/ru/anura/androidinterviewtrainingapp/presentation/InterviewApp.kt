package ru.anura.androidinterviewtrainingapp.presentation

import android.app.Application
import ru.anura.androidinterviewtrainingapp.di.DaggerApplicationComponent
import ru.anura.androidinterviewtrainingapp.di.ThemeModule
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme


class InterviewApp:Application() {
    val component by lazy{
        val themeModule = ThemeModule(Theme.ALL)
        DaggerApplicationComponent.factory()
            .create(this, themeModule)
    }
}